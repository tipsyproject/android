package com.tipsy.app.orga.entree.vente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeFragment;

/**
 * Created by tech on 12/02/14.
 */
public class WaitFragment extends EntreeFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_mode_vente, container, false);

        return view;
    }

}


