package com.tipsy.lib.billetterie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.tipsy.app.R;
import com.tipsy.lib.Event_old;
import com.tipsy.lib.commerce.Achat;

/**
 * Created by valoo on 27/12/13.
 */
public abstract class ListeVentesFragment extends Fragment {

    protected EntreeArrayAdapter adapter;
    protected TextView nbVentes;
    protected ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(int layout,LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout, container, false);
        nbVentes = (TextView) view.findViewById(R.id.nb_billets);
        adapter = new EntreeArrayAdapter(getActivity(), getEntrees());
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setEmptyView(inflater.inflate(R.layout.empty_liste_billets, null));
        return view;
    }



    // Redéfinition de l'actionBar: Bouton de validation
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_liste_billets_vendus, menu);
    }

    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                return true;
            case R.id.action_update:
                Billetterie.refreshVentes(getEvent(), getActivity(), getEntrees(), adapter, nbVentes);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public abstract Event_old getEvent();
    public abstract ArrayList<Achat> getEntrees();

}
