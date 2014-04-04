package com.tipsy.app.orga.vestiaire.in;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.vestiaire.ModelCallback;
import com.tipsy.app.orga.vestiaire.VestiaireActivity;
import com.tipsy.app.orga.vestiaire.VestiaireModelFragment;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Vestiaire;
import com.tipsy.lib.util.Bracelet;

import java.util.ArrayList;

/**
 * Created by tech on 10/03/14.
 */
public class ModeInActivity extends VestiaireActivity implements
        VestiaireModelFragment.VestiaireModelListener,
        PanierFragment.PanierListener,
        CarnetTicketsFragment.CarnetListener,
        TicketNumberFragment.TicketNumberListener
{

    private CarnetTicketsFragment fragCarnet;
    private PanierFragment fragPanier;
    private boolean blockNFC = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(ModeInActivity.class, savedInstanceState);
        setContentView(R.layout.act_vestiaire_entree);

        /* Chargement des fragments */
        FragmentManager fm = getSupportFragmentManager();
        fragCarnet = (CarnetTicketsFragment) fm.findFragmentById(R.id.fragTickets);
        fragPanier = (PanierFragment) fm.findFragmentById(R.id.fragPanier);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(!blockNFC) {

            if(fragPanier.hasParticipant())
                Toast.makeText(this, "Un participant est déjà en attente", Toast.LENGTH_SHORT).show();
            else{
                blockNFC = true;
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                final Bracelet bracelet = new Bracelet(tag);
                int found = findParticipant(bracelet);
                if (found > -1) {
                    fragPanier.setParticipant(participants.get(found));
                    blockNFC = false;
                } else {
                    onModelUpdated = new ModelCallback() {
                        @Override
                        public void updated() {
                            int found = findParticipant(bracelet);
                            if (found > -1) {
                                fragPanier.setParticipant(participants.get(found));
                            } else {
                                Toast.makeText(ModeInActivity.this, "Bracelet inconnu", Toast.LENGTH_LONG).show();
                            }
                            onModelUpdated = null;
                            blockNFC = false;
                        }
                    };
                    loadModel();
                }
            }
        }
    }

    public void onTicketChoosen(Vestiaire ticket){
        fragPanier.add(ticket);
    }

    public void onPanierValidated(final ArrayList<Vestiaire> commande){
        final ProgressDialog wait = ProgressDialog.show(this, "", "Enregistrement...", true, true);
        Vestiaire.saveAllInBackground((ArrayList) commande, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                wait.dismiss();
                if(e==null){
                    tickets.addAll(commande);
                    fragCarnet.clearCommande();
                    Toast.makeText(ModeInActivity.this, "Vestiaire enregistré", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ModeInActivity.this, "Erreur sauvegarde", Toast.LENGTH_SHORT).show();
                    for(Vestiaire ticket : commande)
                        fragCarnet.add(ticket);
                }
            }
        });
    }

    public void onRemoved(Vestiaire ticket){
        fragCarnet.add(ticket);
    }

    public void onNumberDefined(int num){
        fragCarnet.numberDefined(num);
    }

    public boolean isModeEntree(){
        return true;
    }

    @Override
    public void onStart(){
        super.onStart();
        onModelUpdated = new ModelCallback() {
            @Override
            public void updated() {
                fragCarnet.updateCarnet();
            }
        };
    }
}
