package com.tipsy.app.orga.entree.stats;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeActivity;
import com.tipsy.app.orga.entree.EntreeMenuFragment;

/**
 * Created by valoo on 24/03/14.
 */
public class ModeStatsActivity extends EntreeActivity {

    private StatsFragment fragStats;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(ModeStatsActivity.class, savedInstanceState);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        /* Init des fragments */
        fragStats = new StatsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mode_container, fragStats);
        ft.commit();
    }

    public void modelUpdated(){
        fragStats.updateProgress();
    }

    /* Permet au fragment EntreeMenuFragment d'afficher le bon onglet */
    public int getCurrentMode(){
        return EntreeMenuFragment.MODE_STATS;
    }

}
