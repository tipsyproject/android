package com.tipsy.app.orga.bar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.app.orga.vestiaire.ModelCallback;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Bracelet;
import com.tipsy.lib.util.Commande;
import com.tipsy.lib.util.Commerce;
import com.tipsy.lib.util.Item;
import com.tipsy.lib.util.Panier;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tech on 10/03/14.
 */
public class BarActivity extends FragmentActivity implements BarListener {

    private String eventId;
    protected ArrayList<Ticket> consos = new ArrayList<Ticket>();
    protected ArrayList<Participant> participants = new ArrayList<Participant>();

    protected Panier panier = new Panier(new ArrayList<Item>());

    /* Modèle */
    protected BarModelFragment model;
    protected ModelCallback onModelUpdated = null;

    protected BarConsoFragment fragConsos;
    protected BarPanierFragment fragPanier;
    protected BarNFCFragment fragNFC;

    protected boolean activerNFC = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_bar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Bar");

        FragmentManager fm = getSupportFragmentManager();
        fragPanier = (BarPanierFragment) fm.findFragmentById(R.id.frag_panier);
        fragConsos = (BarConsoFragment) fm.findFragmentById(R.id.frag_conso);
        fragNFC = (BarNFCFragment) fm.findFragmentById(R.id.frag_nfc);

        fragNFC.hide();

        /* On récupère la liste des tickets, les participants et l'eventId
           si ce n'est pas le premier lancement  */
        if(savedInstanceState != null && savedInstanceState.containsKey("consos")){
            eventId = savedInstanceState.getString("eventId");
            consos = savedInstanceState.getParcelableArrayList("consos");
            participants = savedInstanceState.getParcelableArrayList("participants");
            participants = savedInstanceState.getParcelableArrayList("panier");
            fragPanier.update();
        }
        // Sinon l'activité reçoit l'id de l'event à charger dans le fragment model */
        else{
            eventId = getIntent().getStringExtra("EVENT_ID");
            loadModel();
        }
    }

    public void increaseConso(Ticket ticket){
        int index = -1;
        // Init item à quantité = 1
        Item item = new Item(ticket, 0);
        /* On récupère l'index de l'item dans le panier s'il y est présent (sinon -1) */
        index = panier.indexOf(item);
        /* Si l'item n'est pas encore dans le panier , on l'ajoute */
        if (index == -1) {
            item.setQuantite(1);
            panier.add(item);
        } else { // Sinon passage de l'item du panier
            panier.get(index).setQuantite(panier.get(index).getQuantite() + 1);
        }
        fragPanier.update();
    }

    public void decreaseConso(Item item){
        /* Suppression de l'item s'il n'en restait qu'un */
        if(item.getQuantite() == 1)
            panier.remove(item);
        else
            item.setQuantite(item.getQuantite() - 1);

        fragPanier.update();
    }


    public void validerPanier(){
        if (panier.isEmpty()) {
            Toast.makeText(BarActivity.this, "Commande vide", Toast.LENGTH_SHORT).show();
        }
        else {
            activerNFC = true;
            fragNFC.show();
        }
    }

    public void saveCommande(Participant p, final ProgressDialog wait){
        Commande commande = new Commande();
        for (Item item : panier) {
            for (int i = 0; i < item.getQuantite(); ++i) {
                Achat achat = new Achat(item.getTicket());
                achat.setParticipant(p);
                achat.setUsed(true);
                commande.add(achat);
            }
        }
        Achat.saveAllInBackground(((ArrayList) commande), new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    /* On cache le fragment NFC */
                    fragNFC.hide();
                    panier.clear();
                    fragPanier.update();
                    Toast.makeText(BarActivity.this, "Paiement effectué", Toast.LENGTH_SHORT).show();
                } else {
                    done(e);
                    Log.d(TipsyApp.TAG, e.getMessage());
                    Toast.makeText(BarActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                    activerNFC = true;
                }
                wait.dismiss();
            }
        });
    }



    @Override
    protected void onNewIntent(Intent intent) {
        if(activerNFC) {
            activerNFC = false;
            final ProgressDialog wait = ProgressDialog.show(this, "", "Vérification en cours...", true, true);

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            final Bracelet bracelet = new Bracelet(tag);
            int found = findParticipant(bracelet);
            if (found > -1) {
                saveCommande(participants.get(found), wait);
            } else {
                /* Si le participant n'a pas été trouvé,
                on met à jour la liste pour vérifier à nouveau
                 */
                onModelUpdated = new ModelCallback() {
                    @Override
                    public void updated() {
                        int found = findParticipant(bracelet);
                        if (found > -1) {
                            saveCommande(participants.get(found), wait);
                        } else {
                            Toast.makeText(BarActivity.this, "Bracelet inconnu", Toast.LENGTH_LONG).show();
                            activerNFC = true;
                            wait.dismiss();
                        }
                        onModelUpdated = null;
                    }
                };
                loadModel();
            }
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


    public void loadModel(){
        /* Lancement du fragment d'init */
        model = new BarModelFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        /* Suppression de l'ancien model */
        BarModelFragment oldModel = (BarModelFragment) getSupportFragmentManager().findFragmentByTag("init");
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
    public Panier getPanier() {
        return panier;
    }
    public ArrayList<Ticket> getConsos() {
        return consos;
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
        outState.putParcelableArrayList("consos", consos);
        outState.putParcelable("panier", panier);
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
                Intent intent = new Intent(BarActivity.this, EventOrgaActivity.class);
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
        builder.setMessage("Vous allez quitter le mode Bar.");
        builder.show();
    }

}
