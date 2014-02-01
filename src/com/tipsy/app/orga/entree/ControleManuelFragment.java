package com.tipsy.app.orga.entree;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tipsy.app.R;
import com.tipsy.app.orga.billetterie.EntreeArrayAdapter;


/**
 * Created by vquefele on 20/01/14.
 */
public class ControleManuelFragment extends ListFragment {

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

    /* DEFINITION DE L'ACTION BAR */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Rechercher").setIcon(R.drawable.ic_action_search);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_update:
                callback.updateEntrees(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateListe(){
        entreesAdapter.notifyDataSetChanged();
    }

}
