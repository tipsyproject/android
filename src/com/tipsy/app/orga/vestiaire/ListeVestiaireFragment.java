package com.tipsy.app.orga.vestiaire;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.tipsy.lib.Participant;
import com.tipsy.lib.Vestiaire;

import java.util.ArrayList;

/**
 * Created by valoo on 06/04/14.
 */
public class ListeVestiaireFragment extends ListFragment {

    private VestiaireAdapter adapter;
    private VestiaireListener listener;


    public interface VestiaireListener {
        public void onSelected(ArrayList<Vestiaire> tickets);
        public ArrayList<Vestiaire> getTickets();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (VestiaireListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement VestiaireListener");
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new VestiaireAdapter(getActivity(),listener.getTickets());
        setListAdapter(adapter);
        setEmptyText("Vestiaire vide");
        update();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        listener.onSelected( loadTickets(adapter.get(position)) );

    }

    public void select(Participant p){
        listener.onSelected( loadTickets(p) );
    }

    private ArrayList<Vestiaire> loadTickets(Participant p){
        ArrayList<Vestiaire> tickets = new ArrayList<Vestiaire>();
        for(Vestiaire ticket : listener.getTickets())
            if(ticket.getParticipant().equals(p))
                tickets.add(ticket);
        return tickets;
    }

    public void update(){
        adapter.getFilter().filter("");
    }
}
