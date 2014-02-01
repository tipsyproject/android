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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.billetterie.EntreeArrayAdapter;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.Achat;
import com.tipsy.lib.util.Bracelet;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.EventActivity;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_access);
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Mode Entrée");


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
        Ticket.loadVentes(getBilletterie(), new FindCallback<Achat>() {
            @Override
            public void done(List<Achat> achats, ParseException e) {
                if (e == null) {
                    entrees.clear();
                    entrees.addAll(achats);
                    if(controleNfcFragment != null)
                        controleNfcFragment.updateProgress();
                    if(controleManuelFragment != null){
                        EntreeArrayAdapter adapter = (EntreeArrayAdapter) controleManuelFragment.getListAdapter();
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(EntreeActivity.this, "Mise à jour effectuée", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(EntreeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if(cb!=null){
                    cb.done(e);
                }
            }
        });
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
                        if(controleNfcFragment != null)
                            controleNfcFragment.updateProgress();
                    }
                });
            }
            else
                message="Entrée déjà validée";
        }else message = "Entrée non autorisée";
        wait.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    public void goToNFC() {
        controleNfcFragment = new ControleNFCFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, controleNfcFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void goToManualAccess() {
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
