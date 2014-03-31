package com.tipsy.app.orga.vestiaire;

import android.os.Bundle;

import com.tipsy.app.R;

/**
 * Created by tech on 10/03/14.
 */
public class ModeSortieActivity extends VestiaireActivity{


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.act_vestiaire_sortie);
        super.onCreate(ModeSortieActivity.class,savedInstanceState);
    }



    public void modelUpdated(){
    };

    public boolean isModeEntree(){
        return false;
    }
}
