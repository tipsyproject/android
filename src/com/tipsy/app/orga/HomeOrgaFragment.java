package com.tipsy.app.orga;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tipsy.app.R;
import com.tipsy.lib.EventArrayAdapter;

/**
 * Created by Alexandre on 23/12/13.
 */
public class HomeOrgaFragment extends Fragment {
    private OrgaListener callback;
    private ListView listView;
    private EventArrayAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OrgaListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_orga_home, container, false);

        listView = (ListView) view.findViewById(R.id.list);
        adapter = new EventArrayAdapter(getActivity(), callback.getEvents());
        listView.setAdapter(adapter);
        listView.setEmptyView(inflater.inflate(R.layout.empty_liste_event, null));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callback.goToEvent(position);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setMenuTitle(MenuOrga.ACCUEIL);

    }

    // Redéfinition de l'actionBar: Bouton de validation
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_orga, menu);
    }

    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_new_event:
                callback.goToNewEvent();
                return true;
            case R.id.action_stats:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}