package com.tipsy.app.membre;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tipsy.app.R;
import com.tipsy.lib.util.EventArrayAdapter;

/**
 * Created by Valentin on 30/12/13.
 */
public class SearchEventFragment extends ListFragment {

    private MembreListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MembreListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventArrayAdapter adapter = new EventArrayAdapter(getActivity(), callback.getSearchResults());
        setListAdapter(adapter);
        setEmptyText(getString(R.string.empty_liste_event));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        callback.goToEvent(callback.getSearchResults().get(position));
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setMenuTitle("Résultats de la recherche");
    }
}
