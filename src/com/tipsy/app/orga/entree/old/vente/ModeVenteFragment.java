package com.tipsy.app.orga.entree.old.vente;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.entree.old.EntreeNFCFragment;
import com.tipsy.app.orga.entree.old.ModeFragment;
import com.tipsy.app.orga.entree.old.NFCCallback;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Bracelet;

/**
 * Created by vquefele on 20/01/14.
 */
public class ModeVenteFragment extends ModeFragment {

    private EntreeNFCFragment fragNFC;
    private TarifsFragment fragTarifs;
    private ParticipantFragment fragParticipant;

    private Achat entree;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragNFC = callback.getFragNFC();
        fragTarifs = new TarifsFragment();
        fragParticipant = new ParticipantFragment();
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onResume();
        entree = new Achat();
        entree.setParticipant(new Participant());
        modeNFC();
        callback.setNFCCallback(new NFCCallback() {
            @Override
            public void onScan(Bracelet bracelet) {
                    /* Bracelet déjà associé à une entrée */
                if (callback.findBracelet(bracelet) > -1)
                    callback.KO("Bracelet déjà utilisé", "Veuillez en utiliser un autre");
                else {
                    entree.getParticipant().setBracelet(bracelet.getTag());
                    Toast.makeText(getActivity(), "Bracelet activé", Toast.LENGTH_SHORT).show();
                    callback.setNFCCallback(null);
                    modeTarifs();
                }
            }
        });
        Log.d("TOUTAFAIT", "Vente resumed");
    }

    @Override
    public void onStop(){
        entree = null;
        super.onPause();
    }

    public void setTarifVente(Ticket t){
        entree.setTicket(t);
        modeParticipant();
    }

    public void setParticipantInfos(Participant p){
        entree.getParticipant().setPrenom(p.getPrenom());
        entree.getParticipant().setNom(p.getNom());
        entree.getParticipant().setEmail(p.getEmail());

        entree.setUsed(true);
        entree.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    callback.getEntrees().add(entree);
                    modeNFC();
                    Toast.makeText(getActivity(), "Entrée activée", Toast.LENGTH_SHORT).show();
                }
            }
        });
        modeParticipant();
    }



    /* AFFICHAGE DE L'ECOUTE NFC */
    public void modeNFC(){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.frag_container, fragNFC);
        ft.commit();
    }

    /* CHOIX DU TICKET */
    public void modeTarifs(){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.frag_container, fragTarifs);
        ft.commit();
    }

    /* IDENTITE PARTICIPANT */
    public void modeParticipant(){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.frag_container, fragParticipant);
        ft.commit();
    }




}
