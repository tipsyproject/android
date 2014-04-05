package com.tipsy.app.orga.vestiaire.out;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.tipsy.app.R;
import com.tipsy.app.orga.vestiaire.ModelCallback;
import com.tipsy.app.orga.vestiaire.VestiaireActivity;

/**
 * Created by tech on 10/03/14.
 */
public class ModeOutActivity extends VestiaireActivity implements
        ListeVestiaireFragment.ListeVestiaireListener
{

    private ListeVestiaireFragment fragListe;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(ModeOutActivity.class,savedInstanceState);
        setContentView(R.layout.act_vestiaire_sortie);
        /* Chargement des fragments */
        FragmentManager fm = getSupportFragmentManager();
        fragListe = (ListeVestiaireFragment) fm.findFragmentById(R.id.fragListe);
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
