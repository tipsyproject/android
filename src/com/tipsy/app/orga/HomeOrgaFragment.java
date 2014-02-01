package com.tipsy.app.orga;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.parse.ParseObject;
import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.app.UserActivity;
import com.tipsy.lib.TipsyUser;
import com.tipsy.lib.util.EventArrayAdapter;

/**
 * Created by Alexandre on 23/12/13.
 */
public class HomeOrgaFragment extends ListFragment {
    private OrgaListener callback;
    protected EventArrayAdapter adapter;
    protected Button delete;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OrgaListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new EventArrayAdapter(getActivity(), callback.getEvents());
        setListAdapter(adapter);
        //delete = (Button) getListView().findViewById(R.id.delete);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Button delete = (Button) view.findViewById(R.id.delete);
                if (delete.getVisibility() == View.VISIBLE) {
                    view.setBackgroundResource(R.color.background_menu);
                    view.setAlpha(1f);
                    delete.setVisibility(View.GONE);
                } else {
                    view.setBackgroundResource(R.drawable.event_fade);
                    view.setAlpha(0.4f);
                    delete.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        setEmptyText(getString(R.string.empty_liste_event));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //final View viewEvent = inflater.inflate(R.layout.frag_event_item, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
        //if(delete.getVisibility() == View.GONE)
            callback.goToEvent(position);
    }

    public void onBackPressed() {
        //if (delete.getVisibility() == View.VISIBLE)
            onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setMenuTitle(MenuOrga.ACCUEIL);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
                callback.stats();
                UserActivity.getMenu().getDrawerList().setItemChecked(MenuOrga.STATS, true);
                OrgaActivity.getMenu().getDrawerList().setSelection(MenuOrga.STATS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}