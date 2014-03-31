package com.tipsy.app.orga.vestiaire;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by tech on 12/02/14.
 */
public abstract class VestiaireFragment extends Fragment {

    protected VestiaireListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (VestiaireListener) activity;
    }

}


