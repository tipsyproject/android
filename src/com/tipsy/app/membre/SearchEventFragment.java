package com.tipsy.app.membre;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.tipsy.app.R;
import com.tipsy.lib.Event;
import com.tipsy.lib.EventArrayAdapter;

/**
 * Created by Valentin on 30/12/13.
 */
public class SearchEventFragment extends Fragment {

    private EventArrayAdapter adapter;
    private ListView listView;
    private MembreListener callback;
    private ArrayList<Event> eventResults;


    public static SearchEventFragment init(ArrayList<Event> events) {
        SearchEventFragment frag = new SearchEventFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("Events", events);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MembreListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        eventResults = getArguments().getParcelableArrayList("Events");
        View view = inflater.inflate(R.layout.frag_orga_home, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        if (eventResults.size() == 0) {
            TextView test = (TextView) view.findViewById(R.id.no_result);
            test.setText("Aucun événement");
        }
        adapter = new EventArrayAdapter(getActivity(), eventResults);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callback.goToEvent(eventResults.get(position));
            }
        });
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        callback.setMenuTitle("Résultats de la recherche");
    }
}
