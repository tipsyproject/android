package com.tipsy.app.orga.entree;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tipsy.app.R;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Participant;

/**
 * Created by vquefele on 20/01/14.
 */
public class EntreeVenteFragment extends Fragment {

    private EntreeListener callback;
    private EntreeNFCFragment fragNFC;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_scan_nfc, container, false);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Achat entree = new Achat();
        entree.setParticipant(new Participant());
        callback.setCurrentEntree(entree);
        callback.modeNFC();

    }



    /* AFFICHAGE DE L'ECOUTE NFC */
    public void modeNFC(){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.frag_vente, fragNFC);
        ft.commit();
    }




}
