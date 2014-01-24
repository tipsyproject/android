package com.tipsy.app.orga.acces;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import com.tipsy.app.R;
import com.tipsy.lib.billetterie.Billetterie;
import com.tipsy.lib.billetterie.ListeVentesFragment;
import com.tipsy.lib.commerce.Achat;

/**
 * Created by vquefele on 20/01/14.
 */
public class ManualAccesFragment extends ListeVentesFragment {

    private AccesListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (AccesListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(R.layout.frag_access_manual,inflater,container,savedInstanceState);
        return view;
    }

    @Override
    public Billetterie getBilletterie(){
        return callback.getBilletterie();
    }
    @Override
    public ArrayList<Achat> getEntrees(){
        return callback.getEntrees();
    }

}
