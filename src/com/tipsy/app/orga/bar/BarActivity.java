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
    private ArrayList<Participant> entrees = new ArrayList<Participant>();
    protected Panier panier = new Panier();
    protected ArrayList<Ticket> conso = new ArrayList<Ticket>();
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

        panier = new Panier(new ArrayList<Item>());
        FragmentManager fm = getSupportFragmentManager();
        fragPanier = (BarPanierFragment) fm.findFragmentById(R.id.frag_panier);
        fragConsos = (BarConsoFragment) fm.findFragmentById(R.id.frag_conso);
        fragNFC = (BarNFCFragment) fm.findFragmentById(R.id.frag_nfc);

        fragNFC.hide();

        InitBarFragment initEntreeFragment = (InitBarFragment) fm.findFragmentByTag("init");
        if (savedInstanceState == null && initEntreeFragment == null) {
            final ProgressDialog wait = ProgressDialog.show(this, null, "Chargement...", true, true);
            InitBarFragment initBarFragment = new InitBarFragment();
            Bundle args = new Bundle();
            args.putString("EVENT_ID", getIntent().getStringExtra("EVENT_ID"));
            initBarFragment.setArguments(args);
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(initBarFragment, "init");
            ft.commit();
            wait.dismiss();
        } else {
            entrees = savedInstanceState.getParcelableArrayList("Entrees");
            conso = savedInstanceState.getParcelableArrayList("Conso");
            panier = savedInstanceState.getParcelable("Panier");
            fragPanier.update();
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
        if (!panier.isEmpty()) {
            activerNFC = true;
            fragNFC.show();
        }
        else
            Toast.makeText(BarActivity.this, "Commande vide", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(activerNFC){
            activerNFC = false;
            final ProgressDialog wait = ProgressDialog.show(this, "", "Vérification en cours...", true, true);

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            final String tagID = Bracelet.bytesToHex(tag.getId());

            Ticket.loadParticipants(tagID, new FindCallback<Participant>() {
                @Override
                public void done(List<Participant> participants, ParseException e) {
                    if (e == null && participants.size()>0) {
                        Commande commande = new Commande();
                        for (Item item : panier)
                            for (int i = 0; i < item.getQuantite(); ++i) {
                                Achat achat = new Achat(item.getTicket());
                                achat.setParticipant(participants.get(0));
                                achat.setUsed(true);
                                commande.add(achat);
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

                    } else {
                        Toast.makeText(BarActivity.this, "Participant inexistant", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            activerNFC = false;
        }
    }


    public void findConso(Event ev, FindCallback cb) {
        ParseQuery<Ticket> query = ParseQuery.getQuery(Ticket.class);
        query.include("event");
        query.whereEqualTo("event", ev);
        query.whereEqualTo("type", Ticket.CONSO);
        query.findInBackground(cb);
    }

    public void loadEventConso(String eventId, final QueryCallback callback_e) {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(eventId, new GetCallback<Event>() {
            @Override
            public void done(Event ev, ParseException e) {
                if (ev != null) {
                    findConso(ev, new FindCallback<Ticket>() {
                        @Override
                        public void done(List<Ticket> consos, ParseException e) {
                            if (e == null) {
                                getConso().clear();
                                getConso().addAll(consos);
                                BarConsoFragment.gridAdapter.notifyDataSetChanged();
                            }
                            callback_e.done(e);
                        }
                    });
                } else {
                    callback_e.done(e);
                }
            }
        });
    }

    /* Verrouillage du bouton retour */
    @Override
    public void onBackPressed() {
    }




    public Panier getPanier() {
        return panier;
    }

    public ArrayList<Ticket> getConso() {
        return conso;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelableArrayList("Entrees", entrees);
        outState.putParcelableArrayList("Conso", conso);
        outState.putParcelable("Panier", panier);
        super.onSaveInstanceState(outState);
    }

    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Confirmation avant de quitter le mode Entrées
            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
                        Intent intent = new Intent(BarActivity.this, EventOrgaActivity.class);
                        intent.putExtra("EVENT_ID", eventId);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.setMessage("Vous allez quitter le mode Bar.");
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
