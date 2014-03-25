package com.tipsy.app.orga.entree.stats;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeListener;
import com.tipsy.lib.Achat;

/**
 * Created by tech on 12/02/14.
 */
public class TestFragment extends Fragment {

    private EntreeListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_mode_stats, container, false);

        return view;
    }

}


