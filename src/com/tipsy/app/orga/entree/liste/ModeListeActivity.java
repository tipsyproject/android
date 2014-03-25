package com.tipsy.app.orga.entree.liste;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeActivity;
import com.tipsy.app.orga.entree.EntreeMenuFragment;

/**
 * Created by valoo on 24/03/14.
 */
public class ModeListeActivity extends EntreeActivity {

    private ListeFragment fragListe;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(ModeListeActivity.class, savedInstanceState);
        fragListe = new ListeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mode_container, fragListe);
        ft.commit();
    }

    /* Permet au fragment EntreeMenuFragment d'afficher le bon onglet */
    public int getCurrentMode(){
        return EntreeMenuFragment.MODE_LISTE;
    }
}