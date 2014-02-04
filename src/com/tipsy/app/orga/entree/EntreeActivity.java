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
import android.support.v4.view.ViewPager;
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
import com.tipsy.app.orga.prevente.PreventeActivity;
import com.tipsy.lib.Achat;
import com.tipsy.lib.util.Bracelet;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.EventActivity;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vquefele on 20/01/14.
 */
public class EntreeActivity extends EventActivity implements EntreeListener {
    private ArrayList<Achat> entrees = new ArrayList<Achat>();
    private ProgressDialog initDialog;
    private ModeNFCFragment modeNfcFragment;
    private ModeManuelFragment modeManuelFragment;
    private ProgressBar progressBar;
    private TextView progressText;
    private static int MODE_NFC = 0;
    private static int MODE_QRCODE = 1;
    private static int MODE_MANUEL = 2;
    private Button buttonNFC;
    private Button buttonQRCode;
    private Button buttonManuel;
    private int currentMode = MODE_NFC;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_entree);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Mode Entrée");

        /* BARRE DE SUIVI DES ENTREES */
        progressText = (TextView) findViewById(R.id.progressText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        /*  INITIALISATION MENU MODE CONTROLE */
        buttonNFC = (Button) findViewById(R.id.button_nfc);
        buttonQRCode = (Button) findViewById(R.id.button_qrcode);
        buttonManuel = (Button) findViewById(R.id.button_manuel);
        buttonNFC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modeNFC();
            }
        });
        buttonManuel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modeManuel();
            }
        });


        /* Chargement des données */
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
            if(getIntent().hasExtra("PREVENTE"))
                args.putParcelable("PREVENTE",getIntent().getParcelableExtra("PREVENTE"));
            initEntreeFragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(initEntreeFragment,"init");
            ft.commit();
        }else{
            entrees = savedInstanceState.getParcelableArrayList("Entrees");
            currentMode = savedInstanceState.getInt("MODE");
            setMode(currentMode);
        }

        /* Mise à jour de la barre de progression */
        if(entrees != null) updateProgress();
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
        outState.putInt("MODE", currentMode);
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
                goToPrevente();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        if(initDialog != null)
            initDialog.dismiss();
        setMode(MODE_NFC);
        modeNfcFragment = new ModeNFCFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, modeNfcFragment);
        ft.commit();
    }


    public void updateEntrees(final QueryCallback cb) {
        final boolean closeDialog;
        if(initDialog == null){
            initDialog = ProgressDialog.show(this,null,getString(R.string.update_entrees),true,true);
            closeDialog = true;
        }
        else if(!initDialog.isShowing()){
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
                    if(modeManuelFragment != null){
                        EntreeArrayAdapter adapter = (EntreeArrayAdapter) modeManuelFragment.getListAdapter();
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(EntreeActivity.this, "Mise à jour effectuée", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(EntreeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // Fermeture de la progressDialog si elle a été affichée depuis cette fonction
                if(closeDialog && initDialog != null)
                    initDialog.dismiss();
                if(cb!=null)
                    cb.done(e);
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


    /* Mise à jour de l'onglet courant du menu Mode */
    public void setMode(int mode){
        currentMode = mode;
        buttonNFC.setBackgroundResource( mode==MODE_NFC ? R.color.primary : R.color.secondary);
        buttonQRCode.setBackgroundResource( mode==MODE_QRCODE ? R.color.primary : R.color.secondary);
        buttonManuel.setBackgroundResource( mode==MODE_MANUEL ? R.color.primary : R.color.secondary);
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
        setMode(MODE_NFC);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new ModeNFCFragment());
        ft.commit();
    }

    public void modeManuel() {
        setMode(MODE_MANUEL);
        modeManuelFragment = new ModeManuelFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, modeManuelFragment);
        ft.commit();
    }

    public void goToPrevente(){
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
        Intent intent = new Intent(this, PreventeActivity.class);
        intent.putExtra("EVENT_ID",event.getObjectId());
        intent.putExtra("Billetterie",billetterie);
        startActivity(intent);
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
