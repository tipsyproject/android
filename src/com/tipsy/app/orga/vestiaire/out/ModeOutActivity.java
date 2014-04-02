package com.tipsy.app.orga.vestiaire.out;

import android.os.Bundle;

import com.tipsy.app.R;
import com.tipsy.app.orga.vestiaire.VestiaireActivity;

/**
 * Created by tech on 10/03/14.
 */
public class ModeOutActivity extends VestiaireActivity {



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.act_vestiaire_sortie);
        super.onCreate(ModeOutActivity.class,savedInstanceState);
    }

    public boolean isModeEntree(){
        return false;
    }
}
