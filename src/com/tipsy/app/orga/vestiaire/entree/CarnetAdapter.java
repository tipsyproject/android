package com.tipsy.app.orga.vestiaire.entree;

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
public class CarnetAdapter extends ArrayAdapter<Vestiaire> {
    private Context context;
    private ArrayList <Vestiaire> nextTickets = new ArrayList<Vestiaire>();

    public CarnetAdapter(Context context,ArrayList<Vestiaire> carnet) {
        super(context, R.layout.frag_vestiaire_ticket, carnet);
        this.context = context;
        this.nextTickets = carnet;
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

    public void useCarnet(Carnet carnet){
        nextTickets = carnet.getNextTickets();
        notifyDataSetChanged();
    }
}