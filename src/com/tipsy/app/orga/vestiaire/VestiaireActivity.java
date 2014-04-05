package com.tipsy.app.orga.vestiaire;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.tipsy.app.R;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.app.orga.vestiaire.in.ModeInActivity;
import com.tipsy.app.orga.vestiaire.out.ModeOutActivity;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Vestiaire;
import com.tipsy.lib.util.Bracelet;

import java.util.ArrayList;

/**
 * Created by tech on 10/03/14.
 */
public abstract class VestiaireActivity extends FragmentActivity {

    /* Données */
    protected String eventId;
    protected ArrayList<Vestiaire> tickets = new ArrayList<Vestiaire>();
    protected ArrayList<Participant> participants = new ArrayList<Participant>();

    /* Modèle */
    protected VestiaireModelFragment model;
    protected ModelCallback onModelUpdated = null;

    /* Scan NFC */
    protected NfcAdapter adapter;
    protected PendingIntent pendingIntent;


    protected void onCreate(Class<?> child, final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Home Button */
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Vestiaire");

        /* Ecran constamment allumé */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /* Initialisation écoute NFC */
        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, child).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        /* On récupère la liste des tickets, les participants et l'eventId
           si ce n'est pas le premier lancement  */
        if(getIntent().hasExtra("tickets")){
            eventId = getIntent().getStringExtra("eventId");
            tickets = getIntent().getParcelableArrayListExtra("tickets");
            participants = getIntent().getParcelableArrayListExtra("participants");
        }
        else if(savedInstanceState != null && savedInstanceState.containsKey("tickets")){
            eventId = savedInstanceState.getString("eventId");
            tickets = savedInstanceState.getParcelableArrayList("tickets");
            participants = savedInstanceState.getParcelableArrayList("participants");
        }
        // Sinon l'activité reçoit l'id de l'event à charger dans le fragment model */
        else{
            eventId = getIntent().getStringExtra("EVENT_ID");
            loadModel();
        }
    }


    /* Retourne l'index du participant correspondant au bracelet dans la liste des participants
    *  Retourne -1 si le bracelet n'est associé à aucun participant */
    public int findParticipant(Bracelet bracelet){
        Participant p = null;
        for(int i=0; i<participants.size(); ++i){
            p = participants.get(i);
            if (p.hasBracelet() && p.getBracelet().equals(bracelet.getTag()))
                return i;
        }
        return -1;
    }


    public void modeEntree(){
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        Intent intent = new Intent(this, ModeInActivity.class);
        intent.putExtra("eventId",eventId);
        intent.putExtra("tickets", tickets);
        intent.putExtra("participants", participants);
        startActivity(intent);
    }

    public void modeSortie(){
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        Intent intent = new Intent(this, ModeOutActivity.class);
        intent.putExtra("eventId",eventId);
        intent.putExtra("tickets", tickets);
        intent.putExtra("participants", participants);
        startActivity(intent);
    }



    public void loadModel(){
        /* Lancement du fragment d'init */
        model = new VestiaireModelFragment();
        Bundle args = new Bundle();
        args.putString("eventId",eventId);
        model.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        /* Suppression de l'ancien model */
        VestiaireModelFragment oldModel = (VestiaireModelFragment) getSupportFragmentManager().findFragmentByTag("init");
        if(oldModel != null)
            ft.remove(oldModel);

        ft.add(model, "init");
        ft.commit();
    }


    public void onModelUpdated(ArrayList<Vestiaire> tickets, ArrayList<Participant> participants){
        this.tickets = tickets;
        this.participants = participants;
        if(onModelUpdated != null){
            onModelUpdated.updated();
        }
    };

    public ArrayList<Vestiaire> getTickets(){
        return tickets;
    }
    public ArrayList<Participant> getParticipants(){
        return participants;
    }
    public String getEventId(){
        return eventId;
    }

    /* Retour à l'activity Event */
    public void quitMode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
                Intent intent = new Intent(VestiaireActivity.this, EventOrgaActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setTitle("Quitter");
        builder.setMessage("Vous allez quitter le mode Vestiaire.");
        builder.show();
    }

    @Override
    public void onBackPressed() {
        quitMode();
    }

    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vestiaire, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Confirmation avant de quitter le mode Vestiaire
            case android.R.id.home:
                quitMode();
                return true;
            case R.id.action_switch:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(isModeEntree())
                            modeSortie();
                        else
                            modeEntree();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                if(isModeEntree()){
                    builder.setTitle("Mode Sortie");
                    builder.setMessage("Vous allez basculer vers le mode Sortie");
                }else{
                    builder.setTitle("Mode Entrée");
                    builder.setMessage("Vous allez basculer vers le mode Entrée");
                }
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putString("eventId", eventId);
        outState.putParcelableArrayList("tickets",tickets);
        outState.putParcelableArrayList("participants", participants);
        super.onSaveInstanceState(outState);
    }

    public abstract boolean isModeEntree();


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

    @Override
    public void onStop(){
        onModelUpdated = null;
        super.onStop();
    }
}
