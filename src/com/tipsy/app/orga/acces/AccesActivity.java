package com.tipsy.app.orga.acces;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.billetterie.EntreeArrayAdapter;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Bracelet;
import com.tipsy.lib.Event;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.TipsyUser;
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
    NfcAdapter adapter;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_access);
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Contrôle d'accès");
        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, AccesActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

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
            ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
            loadEventBilletterie(getIntent().getStringExtra("EVENT_ID"),new QueryCallback() {
                @Override
                public void done(Exception e) {
                    if(e==null){
                        wait.dismiss();
                        if (savedInstanceState != null && savedInstanceState.containsKey("Entrees")) {
                            entrees = savedInstanceState.getParcelableArrayList("Entrees");
                        } else {
                            loadVentes();
                            goToHome(false);
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
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                    overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
                }
                return true;
            case R.id.action_refresh:
                loadVentes();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void loadVentes() {
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
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    public void onPause() {
        adapter.disableForegroundDispatch(this);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        final ProgressDialog wait = ProgressDialog.show(this, "", "Vérification en cours...", true, true);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagID = Bracelet.bytesToHex(tag.getId());

        Bracelet.isKnown(tagID,new Bracelet.GetBraceletCallback() {
            @Override
            public void done(Bracelet bracelet, ParseException e) {
                // BRACELET ACTIVE ? //
                if (bracelet != null) {
                    Iterator it = entrees.iterator();
                    Achat entree = null;
                    boolean found = false;
                    while (it.hasNext() && !found) {
                        entree = (Achat) it.next();
                        if (bracelet.getParticipant() != null && entree.getParticipant() != null)
                            found = bracelet.getParticipant().getObjectId().equals(entree.getParticipant().getObjectId());
                        else if (bracelet.getUser() != null && entree.getUser() != null)
                            found = bracelet.getUser().getObjectId().equals(entree.getUser().getObjectId());
                    }
                    String message;
                    if(found){
                        if(entree != null && !entree.isUsed()){
                            message="Entrée OK";
                            entree.setUsed(true);
                            entree.saveInBackground();
                        }
                        else
                            message="Entrée déjà validée";
                    }else message = "Entrée non autorisée";
                    wait.dismiss();
                    Toast.makeText(AccesActivity.this, message, Toast.LENGTH_SHORT).show();

                } else { // BRACELET INCONNU
                    wait.dismiss();
                    Toast.makeText(AccesActivity.this, "Bracelet inconnu !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToHome(boolean addTobackStack) {
        HomeAccesFragment frag = new HomeAccesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
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
}
