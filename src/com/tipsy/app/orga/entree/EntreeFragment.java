package com.tipsy.app.orga.entree;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tipsy.app.R;

/**
 * Created by tech on 12/02/14.
 */
public abstract class EntreeFragment extends Fragment {

    protected EntreeListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
    }

}


