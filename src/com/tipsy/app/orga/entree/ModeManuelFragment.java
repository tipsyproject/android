package com.tipsy.app.orga.entree;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tipsy.app.R;


/**
 * Created by vquefele on 20/01/14.
 */
public class ModeManuelFragment extends ListFragment {

    private EntreeListener callback;
    private EntreeArrayAdapter entreesAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        entreesAdapter = new EntreeArrayAdapter(getActivity(), callback.getEntrees());
        setListAdapter(entreesAdapter);
        setEmptyText(getString(R.string.empty_liste_ventes));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public void updateListe(){
        entreesAdapter.notifyDataSetChanged();
    }

}
