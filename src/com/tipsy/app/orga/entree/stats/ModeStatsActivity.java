package com.tipsy.app.orga.entree.stats;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeActivity;
import com.tipsy.app.orga.entree.EntreeMenuFragment;
import com.tipsy.lib.Achat;
import com.tipsy.lib.util.Bracelet;

/**
 * Created by valoo on 24/03/14.
 */
public class ModeStatsActivity extends EntreeActivity {

    private StatsFragment fragStats;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(ModeStatsActivity.class, savedInstanceState);
        /* Init des fragments */
        fragStats = new StatsFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mode_container, fragStats);
        ft.commit();
    }

    /* Permet au fragment EntreeMenuFragment d'afficher le bon onglet */
    public int getCurrentMode(){
        return EntreeMenuFragment.MODE_STATS;
    }

}
