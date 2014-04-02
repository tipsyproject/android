package com.tipsy.app.orga.vestiaire.in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Vestiaire;
import com.tipsy.lib.util.Item;

import java.util.ArrayList;

/**
 * Created by kef on 02/04/14.
 */
public class PanierVestiaireAdapter extends ArrayAdapter<Vestiaire> {
    private Context context;
    private ArrayList<Vestiaire> tickets;

    public PanierVestiaireAdapter(Context context, ArrayList<Vestiaire> tickets) {
        super(context, R.layout.frag_vestiaire_item, tickets);
        this.context = context;
        this.tickets = tickets;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.frag_vestiaire_item, parent, false);
        TextView textNumber = (TextView) view.findViewById(R.id.textNumber);
        Vestiaire ticket = tickets.get(position);
        textNumber.setText(Integer.toString(ticket.getNumber()));
        return view;
    }
}
