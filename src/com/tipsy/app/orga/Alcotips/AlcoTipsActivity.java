package com.tipsy.app.orga.Alcotips;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import com.tipsy.app.R;

import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.app.orga.vestiaire.ModelCallback;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Bracelet;


/**

 * Created by Bastien on 28/03/2014.
 */
public class AlcoTipsActivity extends FragmentActivity implements ATListener{


    private String eventId;
    private Event event;
    protected ArrayList<Participant> participants = new ArrayList<Participant>();
    protected ArrayList<Achat> consosParticipant = new ArrayList<Achat>();

    /* Modèle */
    protected ATModelFragment model;
    protected ModelCallback onModelUpdated = null;
    protected ATNFCFragment fragNFC;
    protected boolean activerNFC = true;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_alcotips);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("AlcoTips");

        FragmentManager fm = getSupportFragmentManager();
        fragNFC = (ATNFCFragment) fm.findFragmentById(R.id.frag_nfc);
        fragNFC.show();

        /* On récupère les participants et l'eventId
           si ce n'est pas le premier lancement  */
        if(savedInstanceState != null && savedInstanceState.containsKey("participants")){
            eventId= savedInstanceState.getString("eventId");
            participants = savedInstanceState.getParcelableArrayList("participants");
        }
        // Sinon l'activité reçoit l'id de l'event à charger dans le fragment model */
        else{

            eventId = getIntent().getStringExtra("EVENT_ID");

            loadModel();
        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        if(activerNFC) {
            activerNFC = false;
            final ProgressDialog wait = ProgressDialog.show(this, "", "Vérification en cours...", true, true);

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            final Bracelet bracelet = new Bracelet(tag);
            int found = findParticipant(bracelet);

            if (found > -1) {
                findConsos(participants.get(found));
                fragNFC.hide();

            } else {
                /* Si le participant n'a pas été trouvé,
                on met à jour la liste pour vérifier à nouveau
                 */
                onModelUpdated = new ModelCallback() {
                    @Override
                    public void updated() {
                        int found = findParticipant(bracelet);
                        int alcoolemie = 0;
                        if (found > -1) {

                            findConsos(participants.get(found));

                            fragNFC.hide();
                      //      Toast.makeText(AlcoTipsActivity.this, Integer.toString(alcoolemie) , Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(AlcoTipsActivity.this, "Bracelet inconnu", Toast.LENGTH_LONG).show();
                            activerNFC = true;
                            wait.dismiss();
                        }
                        onModelUpdated = null;
                    }
                };
                loadModel();

            }
            wait.dismiss();
        }


    }

    public int findParticipant(Bracelet bracelet){
        Participant p = null;
        for(int i=0; i<participants.size(); ++i){
            p = participants.get(i);
            if (p.hasBracelet() && p.getBracelet().equals(bracelet.getTag()))
                return i;
        }
        return -1;
    }

    public double calculTaux(){
     int TDisipation =15; // taux d'alcool disipé/h
     double AlcoParticipant=0;  // taux d'alcool du participant
     int TAlcool= 25;     // taux d'alcool moyen contenu dans le sang après une conso
     double TCParticipant=0; // Taux d'alcool restant de la conso
     long Hscan =  TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
     long TmaxAbsorb= 90; // temps d'absorbtion max en minute

       // Alcoolémie (g/L)= (10* Nombre d'Ua ingérées)/(Masse corporelle (kg)* coeficient de diffusion) - 0,15*temps écoulé après le premier verre (h)


        for (int i=0; i<consosParticipant.size(); ++i){

          long Hconso = TimeUnit.MILLISECONDS.toMinutes(consosParticipant.get(i).getCreatedAt().getTime());
          long Hdif  = Hscan - Hconso;
            if (TmaxAbsorb > (Hdif)) {
                TCParticipant = TAlcool;

                Toast.makeText(AlcoTipsActivity.this,Long.toString(Hdif) , Toast.LENGTH_SHORT).show();

            }
            else {

                TCParticipant = TAlcool - ((Hscan - Hconso - TmaxAbsorb) * (TDisipation / 60));
                if (TCParticipant < 0)
                TCParticipant=0;
            }

            AlcoParticipant = AlcoParticipant + TCParticipant;

        }
        return AlcoParticipant;
    }



    public void findConsos(Participant currentParticipant) {


        ParseQuery<Ticket> innerQuery = ParseQuery.getQuery(Ticket.class);
        innerQuery.whereEqualTo("event", event);
        innerQuery.whereEqualTo("type", Ticket.CONSO);
        ParseQuery<Achat> query = ParseQuery.getQuery(Achat.class);
        query.include("ticket");
        query.whereEqualTo("participant", currentParticipant);
        query.whereMatchesQuery("ticket", innerQuery);
        query.findInBackground(new FindCallback<Achat>() {
            @Override
            public void done(List<Achat> achats, ParseException e) {
                if (e == null) {
                        for (Achat conso : achats) {
                          String nomConso = conso.getTitre().toLowerCase();

                        if(!nomConso.equals("soft") && !nomConso.equals("orange") && !nomConso.equals("pomme") && !nomConso.equals("coca"))

                            consosParticipant.add(conso);

                        }
                   double alcoolemie = calculTaux();
                    Toast.makeText(AlcoTipsActivity.this, Double.toString(alcoolemie) , Toast.LENGTH_LONG).show();

                } else {
                        Toast.makeText(AlcoTipsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


            }
        });

    }





    public void loadModel(){
        /* Lancement du fragment d'init */
        model = new ATModelFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        /* Suppression de l'ancien model */
        ATModelFragment oldModel = (ATModelFragment) getSupportFragmentManager().findFragmentByTag("init");
        if(oldModel != null)
            ft.remove(oldModel);

        ft.add(model, "init");
        ft.commit();
    }

    public void modelUpdated(){
        if(onModelUpdated != null){
            onModelUpdated.updated();
        }
    };


    public String getEventId(){
        return eventId;
    }
    public void setEvent(Event ev){
        event=ev;
    }
    public ArrayList<Participant> getParticipants() {
        return participants;
    }


    @Override
    public void onBackPressed() {
        quitMode();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelableArrayList("participants", participants);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Confirmation avant de quitter le mode Entrées
            case android.R.id.home:
                quitMode();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void quitMode(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
                Intent intent = new Intent(AlcoTipsActivity.this, EventOrgaActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setMessage("Vous allez quitter le mode AlcoTips.");
        builder.show();
    }



}

