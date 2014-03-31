package com.tipsy.app.orga.vestiaire;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.KOFragment;
import com.tipsy.app.orga.entree.OKFragment;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Vestiaire;
import com.tipsy.lib.util.Bracelet;

import java.util.ArrayList;

/**
 * Created by tech on 10/03/14.
 */
public class ModeEntreeActivity extends VestiaireActivity{

    private TicketsFragment fragTickets;
    private PanierFragment fragPanier;
    private boolean blockNFC = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(ModeEntreeActivity.class, savedInstanceState);
        setContentView(R.layout.act_vestiaire_entree);


        /* Chargement des fragments */
        FragmentManager fm = getSupportFragmentManager();
        fragTickets = (TicketsFragment) fm.findFragmentById(R.id.fragTickets);
        fragPanier = (PanierFragment) fm.findFragmentById(R.id.fragPanier);

    }

    @Override
    protected void onNewIntent(Intent intent) {

        if(!blockNFC) {
            /* Il ne doit pas y avoir un participant déjà en attente */
            if (currentParticipant != null) {
                Toast.makeText(this, "Un bracelet est déjà en attente", Toast.LENGTH_LONG).show();
            } else {
                blockNFC = true;
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                final Bracelet bracelet = new Bracelet(tag);
                int found = findParticipant(bracelet);
                if (found > -1) {
                    setCurrentParticipant(participants.get(found));
                    blockNFC = false;
                } else {
                    onModelUpdated = new ModelCallback() {
                        @Override
                        public void updated() {
                            int found = findParticipant(bracelet);
                            if (found > -1) {
                                setCurrentParticipant(participants.get(found));
                            } else {
                                Toast.makeText(ModeEntreeActivity.this, "Bracelet inconnu", Toast.LENGTH_LONG).show();
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

    public void setCurrentParticipant(Participant p){
        currentParticipant = p;
        Toast.makeText(ModeEntreeActivity.this, "Bracelet enregistré", Toast.LENGTH_LONG).show();
        fragPanier.allowValidation(true);
    }


    public void modelUpdated(){
        if(onModelUpdated != null){
            onModelUpdated.updated();
        }
    };

    public boolean isModeEntree(){
        return true;
    }
}
