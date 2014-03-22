package com.tipsy.app.orga.entree.liste;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeListener;
import com.tipsy.app.orga.entree.EntreeNFCFragment;
import com.tipsy.app.orga.entree.ModeFragment;
import com.tipsy.app.orga.entree.NFCCallback;
import com.tipsy.app.orga.entree.liste.EntreeArrayAdapter;
import com.tipsy.lib.Achat;
import com.tipsy.lib.util.Bracelet;

import java.lang.reflect.Field;


/**
 * Created by vquefele on 20/01/14.
 */
public class ModeListeFragment extends ModeFragment {

    private ListeFragment fragListe;
    private EntreeNFCFragment fragNFC;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragNFC = callback.getFragNFC();
        fragListe = new ListeFragment();
        modeListe();
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    /* AFFICHAGE DE L'ECOUTE NFC */
    public void modeListe(){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.frag_container, fragListe);
        ft.commit();
    }

    /* AFFICHAGE DE L'ECOUTE NFC */
    public void modeNFC(){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.frag_container, fragNFC);
        ft.commit();
    }

    public void activationManuelle(final Achat entree){
        entree.setUsed(true);
        modeNFC();
        callback.setNFCCallback(new NFCCallback() {
            @Override
            public void onScan(Bracelet bracelet) {
                    /* Bracelet déjà associé à une entrée */
                if (callback.findBracelet(bracelet) > -1)
                    callback.KO("Bracelet déjà utilisé", "Veuillez en utiliser un autre");
                else {
                    entree.getParticipant().setBracelet(bracelet.getTag());
                    entree.setUsed(true);
                    entree.saveInBackground();
                    callback.setNFCCallback(null);
                    Toast.makeText(getActivity(), "Bracelet activé", Toast.LENGTH_SHORT).show();
                    modeListe();
                }
            }
        });
        //entree.saveInBackground();
        ((ArrayAdapter) fragListe.getListAdapter()).notifyDataSetChanged();
    }

}
