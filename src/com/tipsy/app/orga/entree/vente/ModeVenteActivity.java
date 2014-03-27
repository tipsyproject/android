package com.tipsy.app.orga.entree.vente;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeActivity;
import com.tipsy.app.orga.entree.EntreeMenuFragment;

/**
 * Created by valoo on 24/03/14.
 */
public class ModeVenteActivity extends EntreeActivity {


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(ModeVenteActivity.class, savedInstanceState);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mode_container, new WaitFragment());
        ft.commit();
    }

    /* Permet au fragment EntreeMenuFragment d'afficher le bon onglet */
    public int getCurrentMode(){
        return EntreeMenuFragment.MODE_VENTE;
    }

    /* Rien à actualiser */
    public void modelUpdated(){}
}
