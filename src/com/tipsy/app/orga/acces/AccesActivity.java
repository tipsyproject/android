package com.tipsy.app.orga.acces;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.billetterie.EntreeArrayAdapter;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Bracelet;
import com.tipsy.lib.Event;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.EventActivity;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vquefele on 20/01/14.
 */
public class AccesActivity extends EventActivity implements AccesListener {
    private ArrayList<Achat> entrees = new ArrayList<Achat>();
    EntreeArrayAdapter entreesAdapter;
    ProgressBar progressBar;
    TextView progressText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_access);
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Entrées");

        entreesAdapter = new EntreeArrayAdapter(AccesActivity.this, entrees);

        if (savedInstanceState == null) {
            final ProgressDialog wait = ProgressDialog.show(this, null, "Chargement...", true, true);
            wait.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                    overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
                }
            });
            loadEventBilletterie(getIntent().getStringExtra("EVENT_ID"),new QueryCallback() {
                @Override
                public void done(Exception e) {
                    if(e==null){
                        wait.dismiss();
                        if (savedInstanceState != null && savedInstanceState.containsKey("Entrees")) {
                            entrees = savedInstanceState.getParcelableArrayList("Entrees");
                        } else {
                            loadVentes(new QueryCallback() {
                                @Override
                                public void done(Exception e) {
                                    goToNFC(false);
                                }
                            });
                        }
                    }
                }
            });
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
        // Add variable to outState here
        super.onSaveInstanceState(outState);
    }

    // Redéfinition de l'actionBar: Bouton de validation
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
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStack();
                        } else {
                            finish();
                            overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.setMessage("Vous allez quittez le mode Entrées.");
                builder.show();
                return true;
            case R.id.action_refresh:
                loadVentes(new QueryCallback() {
                    @Override
                    public void done(Exception e) {
                        updateProgress();
                    }
                });

                return true;
            case R.id.action_add:
                //goToVendre();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void loadVentes(final QueryCallback cb) {
        Ticket.loadVentes(getBilletterie(), new FindCallback<Achat>() {
            @Override
            public void done(List<Achat> achats, ParseException e) {
                if (e == null) {
                    entrees.clear();
                    entrees.addAll(achats);
                    entreesAdapter.notifyDataSetChanged();
                    Toast.makeText(AccesActivity.this, "Liste mise à jour", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(AccesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        updateProgress();
                    }
                });
            }
            else
                message="Entrée déjà validée";
        }else message = "Entrée non autorisée";
        wait.dismiss();
        Toast.makeText(AccesActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void updateProgress(){
        int entreesValidees = 0;
        for(Achat entree : entrees)
            if(entree.isUsed())
                entreesValidees++;
        progressBar.setMax(entrees.size());
        progressBar.setProgress(entreesValidees);
        progressText.setText(""+entreesValidees+"/"+entrees.size());
    }

    public void goToNFC(boolean addTobackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new NFCAccesFragment());
        if (addTobackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void goToManualAccess() {
        ManualAccesFragment frag = new ManualAccesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        ft.addToBackStack(null);
        ft.commit();
    }

    public ArrayList<Achat> getEntrees() {
        return entrees;
    }

    public EntreeArrayAdapter getEntreesAdapter() {
        return entreesAdapter;
    }

    public ProgressBar getProgressBar(){
        return progressBar;
    }
    public void setProgressBar(ProgressBar pb){
        progressBar = pb;
    }

    public TextView getProgressText(){
        return progressText;
    }
    public void setProgressText(TextView tv){
        progressText = tv;
    }
}
