package com.tipsy.app.orga.entree.old;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tipsy.app.R;

/**
 * Created by vquefele on 20/01/14.
 */
public class EntreeNFCFragment extends Fragment {

    private EntreeListener callback;

    private LinearLayout fragAlreadyUsed;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_scan_nfc, container, false);
        return view;
    }



}
