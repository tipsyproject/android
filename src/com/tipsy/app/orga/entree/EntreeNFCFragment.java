package com.tipsy.app.orga.entree;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tipsy.app.R;

/**
 * Created by vquefele on 20/01/14.
 */
public class EntreeNFCFragment extends EntreeFragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_scan_nfc, container, false);
        return view;
    }



}
