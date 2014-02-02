package com.tipsy.app.orga.entree;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.Achat;
import com.tipsy.lib.util.Bracelet;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.EventActivity;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vquefele on 20/01/14.
 */
public class EntreeActivity extends EventActivity implements EntreeListener {
    private ArrayList<Achat> entrees = new ArrayList<Achat>();
    private ProgressDialog initDialog;
    private ControleNFCFragment controleNfcFragment;
    private ControleManuelFragment controleManuelFragment;
    private Button buttonControleNFC;
    private Button buttonControleQRCode;
    private Button buttonControleManuel;
    private ProgressBar progressBar;
    private TextView progressText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        //overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_entree);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Mode Entrée");

        /* BARRE DE SUIVI DES ENTREES */
        progressText = (TextView) findViewById(R.id.progressText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if(entrees != null) updateProgress();

        /*  INITIALISATION MENU MODE CONTROLE */
        buttonControleNFC = (Button) findViewById(R.id.button_nfc);
        buttonControleQRCode = (Button) findViewById(R.id.button_qrcode);
        buttonControleManuel = (Button) findViewById(R.id.button_manuel);
        buttonControleNFC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modeNFC();
            }
        });
        buttonControleManuel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modeManuel();
            }
        });



        FragmentManager fm = getSupportFragmentManager();
        InitEntreeFragment initEntreeFragment = (InitEntreeFragment) fm.findFragmentByTag("init");
        if(initEntreeFragment == null){
            initDialog = ProgressDialog.show(this, null, "Chargement...", true, true);
            initDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    getSupportFragmentManager().popBackStack();
                    backToEvent();
                }
            });
            initEntreeFragment = new InitEntreeFragment();
            Bundle args = new Bundle();
            args.putString("EVENT_ID",getIntent().getStringExtra("EVENT_ID"));
            initEntreeFragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(initEntreeFragment,"init");
            ft.commit();
        }else
            entrees = savedInstanceState.getParcelableArrayList("Entrees");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelableArrayList("Entrees", entrees);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_access, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Confirmation avant de quitter le mode Entrées
            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        backToEvent();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.setMessage("Vous allez quitter le mode Entrée.");
                builder.show();
                return true;
            case R.id.action_refresh:
                updateEntrees(null);
                return true;
            case R.id.action_add:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        if(initDialog != null)
            initDialog.dismiss();
        controleNfcFragment = new ControleNFCFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, controleNfcFragment);
        ft.commit();
    }


    public void updateEntrees(final QueryCallback cb) {
        final boolean closeDialog;
        if(!initDialog.isShowing()){
            initDialog = ProgressDialog.show(this,null,getString(R.string.update_entrees),true,true);
            closeDialog = true;
        } else{
            initDialog.setMessage(getString(R.string.update_entrees));
            closeDialog = false;
        }
        Ticket.loadVentes(getBilletterie(), new FindCallback<Achat>() {
            @Override
            public void done(List<Achat> achats, ParseException e) {
                if (e == null) {
                    entrees.clear();
                    entrees.addAll(achats);
                    Collections.sort(entrees,Achat.SORT_BY_NAME);
                    // Mise à jour de la progressBar
                    updateProgress();
                    if(controleManuelFragment != null){
                        EntreeArrayAdapter adapter = (EntreeArrayAdapter) controleManuelFragment.getListAdapter();
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(EntreeActivity.this, "Mise à jour effectuée", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(EntreeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // Fermeture de la progressDialog si elle a été affichée depuis cette fonction
                if(closeDialog && initDialog != null) initDialog.dismiss();
                if(cb!=null){
                    cb.done(e);
                }
            }
        });
    }


    /* MISE A JOUR DE LA BARRE DE PROGRESSION */
    public void updateProgress(){
        int entreesValidees = 0;
        for(Achat entree : entrees)
            if(entree.isUsed())
                entreesValidees++;
        progressBar.setMax(entrees.size());
        progressBar.setProgress(entreesValidees);
        progressText.setText("" + entreesValidees + "/" + entrees.size());
    }



    @Override
    protected void onNewIntent(Intent intent) {

        final ProgressDialog wait = ProgressDialog.show(this, "", "Vérification en cours...", true, true);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        final String tagID = Bracelet.bytesToHex(tag.getId());

        Iterator it = entrees.iterator();
        Achat entree = null;
        boolean found = false;
        while (it.hasNext() && !found) {
            entree = (Achat) it.next();
            if (entree.getParticipant() != null && entree.getParticipant().getBracelet() != null)
                found = entree.getParticipant().getBracelet().equals(tagID);
            else if (entree.getUser() != null && entree.getUser().getBracelet() != null )
                found = entree.getUser().getBracelet().equals(tagID);
        }
        String message;
        if(found){
            if(entree != null && !entree.isUsed()){
                message="Entrée OK";
                entree.setUsed(true);
                entree.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        updateProgress();
                    }
                });
            }
            else
                message="Entrée déjà validée";
        }else message = "Entrée non autorisée";
        wait.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    public void modeNFC() {
        buttonControleNFC.setBackgroundResource(R.color.primary);
        buttonControleQRCode.setBackgroundResource(R.color.secondary);
        buttonControleManuel.setBackgroundResource(R.color.secondary);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new ControleNFCFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void modeManuel() {
        buttonControleNFC.setBackgroundResource(R.color.secondary);
        buttonControleQRCode.setBackgroundResource(R.color.secondary);
        buttonControleManuel.setBackgroundResource(R.color.primary);
        controleManuelFragment = new ControleManuelFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, controleManuelFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void backToEvent(){
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
        Intent intent = new Intent(this, EventOrgaActivity.class);
        intent.putExtra("EVENT_ID",event.getObjectId());
        startActivity(intent);
    }

    public ArrayList<Achat> getEntrees() {
        return entrees;
    }
}
