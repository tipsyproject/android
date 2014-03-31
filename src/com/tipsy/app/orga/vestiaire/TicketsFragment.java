package com.tipsy.app.orga.vestiaire;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.app.orga.bar.BarListener;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.Vestiaire;
import com.tipsy.lib.util.Commerce;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by tech on 11/03/14.
 */
public class TicketsFragment extends VestiaireFragment {

    private ListView listView;
    private static TicketsArrayAdapter gridAdapter;
    private ArrayList<Vestiaire> nextTickets = new ArrayList<Vestiaire>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_vestiaire_tickets, container, false);

        listView = (ListView) view.findViewById(R.id.listTickets);
        gridAdapter = new TicketsArrayAdapter(getActivity());
        listView.setAdapter(gridAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //callback.increaseConso(callback.getConso().get(position));
            }
        });

        genTicketList();
        return view;
    }

    // Adapter CONSOS
    public class TicketsArrayAdapter extends ArrayAdapter<Vestiaire> implements Serializable {
        private Context context;

        public TicketsArrayAdapter(Context context) {
            super(context, R.layout.frag_vestiaire_ticket, nextTickets);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.frag_vestiaire_ticket, parent, false);

            TextView textTicket = (TextView) view.findViewById(R.id.textTicket);

            Vestiaire ticket = nextTickets.get(position);

            textTicket.setText(Integer.toString(ticket.getNumber()));

            return view;
        }
    }

    public int getNextTicketNumber(){
        if(callback.getTickets().isEmpty())
            return 1;
        else {
            ArrayList<Vestiaire> tickets = new ArrayList<Vestiaire>();
            tickets.addAll(callback.getTickets());
            Collections.sort(tickets, Vestiaire.SORT_BY_TICKET);
            return tickets.get(tickets.size() - 1).getNumber() + 1;
        }
    }

    private void genTicketList(){
        int offset = getNextTicketNumber();
        for(int i=0; i<8; ++i){
            nextTickets.add(new Vestiaire(offset + i));
        }
        gridAdapter.notifyDataSetChanged();
    }

}
