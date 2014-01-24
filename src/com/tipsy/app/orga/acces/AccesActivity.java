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
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tipsy.app.R;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.Bracelet;
import com.tipsy.lib.Event;
import com.tipsy.lib.billetterie.Billet;
import com.tipsy.lib.billetterie.Billetterie;
import com.tipsy.lib.billetterie.EntreeArrayAdapter;
import com.tipsy.lib.commerce.Achat;

/**
 * Created by vquefele on 20/01/14.
 */
public class AccesActivity extends FragmentActivity implements AccesListener {

    private Event event;
    private Billetterie billetterie;
    private ArrayList<Achat> entrees = new ArrayList<Achat>();
    NfcAdapter adapter;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.act_access);
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Contrôle d'accès");
        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, AccesActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);


        final ProgressDialog wait = ProgressDialog.show(this,null,"Chargement...",true,true);
        wait.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                backToEventOrga();
            }
        });
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(getIntent().getStringExtra("EVENT_ID"), new GetCallback<Event>() {
            @Override
            public void done(Event ev, ParseException e) {
                if (ev != null) {
                    event = ev;
                    ParseQuery<Billet> query = ParseQuery.getQuery(Billet.class);
                    query.whereEqualTo("event",event.getObjectId());
                    query.findInBackground(new FindCallback<Billet>() {
                        @Override
                        public void done(List<Billet> billets, ParseException e) {
                            billetterie.clear();
                            billetterie.addAll(billets);
                            wait.dismiss();
                            if(savedInstanceState != null && savedInstanceState.containsKey("Entrees")){
                                entrees =  savedInstanceState.getParcelableArrayList("Entrees");
                            }else{
                                refresh(null);
                                goToHome(false);
                            }
                        }
                    });
                }
                else{
                    wait.dismiss();
                    Log.d("TOUTAFAIT", "Erreur fetch event/ EventOrgaActivity:Oncreate: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(outState==null)
            outState = new Bundle();
        outState.putParcelableArrayList("Entrees", entrees);
        // Add variable to outState here
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    public void onPause(){
        adapter.disableForegroundDispatch(this);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent){

        final ProgressDialog wait = ProgressDialog.show(this,"","Vérification en cours...",true,true);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagID = Bracelet.bytesToHex(tag.getId());

        ParseQuery<Bracelet> query = ParseQuery.getQuery(Bracelet.class);
        query.include("user");
        query.include("participant");
        query.getInBackground(tagID,new GetCallback<Bracelet>() {
            @Override
            public void done(Bracelet bracelet, ParseException e) {
                /* BRACELET ACTIVE ? */
                if(bracelet != null){
                    Iterator it = entrees.iterator();
                    Achat entree;
                    boolean found = false;
                    while (it.hasNext() && !found) {
                        entree = (Achat) it.next();
                        if (bracelet.isMembre() && entree.getParticipant().isMembre())
                            found = bracelet.getUser().getObjectId().equals(entree.getParticipant().getUser().getObjectId());
                        else if (bracelet.isParticipant())
                            found = bracelet.getParticipant().getObjectId().equals(entree.getParticipant().getObjectId());
                    }

                    String message = found ? "Entrée OK" : "Entrée non autorisée";
                    wait.dismiss();
                    Toast.makeText(AccesActivity.this, message, Toast.LENGTH_SHORT).show();

                }else{ /* BRACELET INCONNU */
                    wait.dismiss();
                    Log.d("TOUTAFAIT", "bracelet inconnu");
                    Toast.makeText(AccesActivity.this, "Bracelet inconnu !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                backToEventOrga();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void backToEventOrga(){
        Intent intent = new Intent(this, EventOrgaActivity.class);
        intent.putExtra("EVENT_ID", event.getObjectId());
        startActivity(intent);
    }

    public void goToHome(boolean addTobackStack){
        HomeAccesFragment frag = new HomeAccesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        if (addTobackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void goToManualAccess(){
        ManualAccesFragment frag = new ManualAccesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void refresh(EntreeArrayAdapter adapter){
        billetterie.refreshVentes(this, entrees, adapter, null);
    }

    public Billetterie getBilletterie(){
        return billetterie;
    }
    public Event getEvent(){
        return event;
    }

    public ArrayList<Achat> getEntrees(){
        return entrees;
    }
}
