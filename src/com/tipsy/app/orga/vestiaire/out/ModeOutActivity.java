package com.tipsy.app.orga.vestiaire.out;

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
import com.tipsy.app.orga.vestiaire.in.PanierFragment;
import com.tipsy.lib.Vestiaire;
import com.tipsy.lib.util.Bracelet;

import java.util.ArrayList;

/**
 * Created by tech on 10/03/14.
 */
public class ModeOutActivity extends VestiaireActivity implements
        ListeVestiaireFragment.VestiaireListener,
        ListeTicketsFragment.ListeTicketsListener
{

    private ListeVestiaireFragment fragListe;
    private ListeTicketsFragment fragPanier;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(ModeOutActivity.class,savedInstanceState);
        setContentView(R.layout.act_vestiaire_sortie);
        /* Chargement des fragments */
        FragmentManager fm = getSupportFragmentManager();
        fragListe = (ListeVestiaireFragment) fm.findFragmentById(R.id.fragListe);
        fragPanier = (ListeTicketsFragment) fm.findFragmentById(R.id.fragPanier);
    }


    @Override
    protected void onNewIntent(Intent intent) {
       if(fragPanier.hasParticipant())
            Toast.makeText(this, "Un participant est déjà en attente", Toast.LENGTH_SHORT).show();
        else{
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            final Bracelet bracelet = new Bracelet(tag);
            int found = findParticipant(bracelet);
            if (found > -1) {
                fragListe.select(participants.get(found));
            } else {
                onModelUpdated = new ModelCallback() {
                    @Override
                    public void updated() {
                        int found = findParticipant(bracelet);
                        if (found > -1) {
                            fragListe.select(participants.get(found));
                        } else {
                            Toast.makeText(ModeOutActivity.this, "Bracelet inconnu", Toast.LENGTH_LONG).show();
                        }
                        onModelUpdated = null;
                    }
                };
                loadModel();
            }
        }
    }


    public void onSelected(ArrayList<Vestiaire> tickets){
        if(tickets.size() > 0) {
            fragPanier.setParticipant(tickets.get(0).getParticipant());
            fragPanier.addAll(tickets);
        }else{
            Toast.makeText(ModeOutActivity.this, "Aucun article", Toast.LENGTH_SHORT).show();
        }
    }

    public void onReturnAll(ArrayList<Vestiaire> tickets){
        Vestiaire.saveAllInBackground((ArrayList) tickets, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null) {
                    Toast.makeText(ModeOutActivity.this, "Articles rendus", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onReturn(Vestiaire ticket){
        ticket.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(ModeOutActivity.this, "Article rendu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        onModelUpdated = new ModelCallback() {
            @Override
            public void updated() {
                fragListe.update();
            }
        };
    }

    public boolean isModeEntree(){
        return false;
    }
}
