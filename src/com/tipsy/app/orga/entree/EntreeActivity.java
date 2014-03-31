package com.tipsy.app.orga.entree;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Bracelet;

import java.util.ArrayList;

/**
 * Created by vquefele on 20/01/14.
 */
public abstract class EntreeActivity extends FragmentActivity implements EntreeListener {

    /* Données */
    protected Event event;
    protected String eventId;
    protected ArrayList<Ticket> billetterie = new ArrayList<Ticket>();
    protected ArrayList<Achat> entrees = new ArrayList<Achat>();
    protected boolean modePrevente;

    /* Modèle */
    protected EntreeModelFragment model;
    protected OKFragment fragOK;
    protected KOFragment fragKO;

    /* Scan NFC */
    protected NfcAdapter adapter;
    protected PendingIntent pendingIntent;
    protected NFCCallback nfcCallback;


    protected void onCreate(Class<?> child, final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_entree);
        /* Home Button */
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Entrée");

        /* Ecran constamment allumé */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        /* Initialisation écoute NFC */
        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, child).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        /* Chargement des fragments */
        FragmentManager fm = getSupportFragmentManager();
        fragOK = (OKFragment) fm.findFragmentById(R.id.frag_ok);
        fragKO = (KOFragment) fm.findFragmentById(R.id.frag_ko);
        fragOK.hide();
        fragKO.hide();


        /* On récupère la liste des entrées, la billetterie et l'event
           si ce n'est pas le premier lancement  */
        if(getIntent().hasExtra("entrees")){
            eventId = getIntent().getStringExtra("eventId");
            event = getIntent().getParcelableExtra("event");
            billetterie = getIntent().getParcelableArrayListExtra("billetterie");
            entrees = getIntent().getParcelableArrayListExtra("entrees");
        }
        else if(savedInstanceState != null && savedInstanceState.containsKey("entrees")){
            eventId = savedInstanceState.getString("eventId");
            event = savedInstanceState.getParcelable("event");
            billetterie = savedInstanceState.getParcelableArrayList("billetterie");
            entrees = savedInstanceState.getParcelableArrayList("entrees");
        }
        // Sinon l'activité reçoit l'id de l'event à charger dans le fragment model */
        else{
            eventId = getIntent().getStringExtra("EVENT_ID");
            loadModel();
        }
    }

    public void loadModel(){
        /* Lancement du fragment d'init */
        model = new EntreeModelFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        /* Suppression de l'ancien model */
        EntreeModelFragment oldModel = (EntreeModelFragment) getSupportFragmentManager().findFragmentByTag("init");
        if(oldModel != null)
            ft.remove(oldModel);

        ft.add(model, "init");
        ft.commit();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Bracelet bracelet = new Bracelet(tag);
        /* Bracelet utilisé pour une autre action que le controle d'accès */
        if(nfcCallback != null){
            nfcCallback.onScan(bracelet);
        }
        /* CONTROLE D'ACCES */
        else{
            int found = findBracelet(bracelet);
            /* Bracelet inconnu */
            if(found == -1){
                KO("Entrée non autorisée", "Bracelet inconnu");
            }else{
                final Achat entree = getEntrees().get(found);
                // Bracelet autorisé
                if (!entree.isUsed()) {
                    entree.setUsed(true);
                    entree.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null) {
                                //updateProgress();
                            }else{
                                entree.setUsed(false);
                            }
                        }
                    });
                    OK(entree.getTicket().getNom(),entree.getParticipant().getFullName());
                }
                // Bracelet déjà passé
                else {
                    KO("Entrée déjà validée", entree.getParticipant().getFullName());
                }
            }
        }
    }



    /* Retourne l'index de l'entree correspondante au bracelet dans la liste des entrees
    *  Retourne -1 si le bracelet n'est associé à aucune entree  */
    public int findBracelet(Bracelet bracelet){
        Achat entree = null;
        for(int i=0; i<getEntrees().size(); ++i){
            entree = getEntrees().get(i);
            if (entree.getParticipant() != null && entree.getParticipant().getBracelet() != null)
                if(entree.getParticipant().getBracelet().equals(bracelet.getTag()))
                    return i;
        }
        return -1;
    }

    /* Getters & Setters des données */
    public String getEventId(){
        return eventId;
    }
    public Event getEvent(){
        return event;
    }
    public void setEvent(Event event){
        this.event = event;
    }
    public ArrayList<Achat> getEntrees(){
        return entrees;
    }
    public ArrayList<Ticket> getBilletterie(){
        return billetterie;
    }

    public void OK(String m1, String m2){
        fragOK.show(m1, m2);
    }
    public void KO(String m1, String m2){
        fragKO.show(m1, m2);
    }


    public void setNFCCallback(NFCCallback cb){
        nfcCallback = cb;
    }


    /* Retour à l'activity Event */
    public void quitMode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
                Intent intent = new Intent(EntreeActivity.this, EventOrgaActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setMessage("Vous allez quitter le mode Entrée.");
        builder.show();
    }

    @Override
    public void onBackPressed() {
        quitMode();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putString("eventId", eventId);
        outState.putParcelable("event", event);
        outState.putParcelableArrayList("billetterie",billetterie);
        outState.putParcelableArrayList("entrees",entrees);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entree, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Confirmation avant de quitter le mode Entrées
            case android.R.id.home:
                quitMode();
                return true;
            case R.id.action_refresh:
                loadModel();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /* Activation de l'écoute NFC */
    @Override
    public void onResume() {
        super.onResume();
        adapter.enableForegroundDispatch(this, pendingIntent, null, null);
        Log.d("TOUTAFAIT", "NFC Actif");
    }

    /* Désactivation de l'écoute NFC */
    @Override
    public void onPause() {
        adapter.disableForegroundDispatch(this);
        Log.d("TOUTAFAIT", "NFC Inactif");
        super.onPause();
    }


}
