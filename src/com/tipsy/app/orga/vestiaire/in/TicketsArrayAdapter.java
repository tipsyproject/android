package com.tipsy.app.orga.vestiaire.in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Vestiaire;

import java.util.ArrayList;

/**
 * Created by kef on 02/04/14.
 */
// Adapter CONSOS
public class TicketsArrayAdapter extends ArrayAdapter<Vestiaire> {
    private Context context;
    private ArrayList <Vestiaire> nextTickets;

    public TicketsArrayAdapter(Context context,ArrayList <Vestiaire> nextTickets) {
        super(context, R.layout.frag_vestiaire_ticket, nextTickets);
        this.nextTickets = nextTickets;
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