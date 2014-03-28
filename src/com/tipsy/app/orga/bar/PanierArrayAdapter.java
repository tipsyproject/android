package com.tipsy.app.orga.bar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.util.Item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kef on 28/03/14.
 */
public class PanierArrayAdapter  extends ArrayAdapter<Item> {
    private Context context;
    private ArrayList<Item> consos;

    public PanierArrayAdapter(Context context, ArrayList<Item> consos) {
        super(context, R.layout.frag_bar_panier_item, consos);
        this.context = context;
        this.consos = consos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewConso = inflater.inflate(R.layout.frag_bar_panier_item, parent, false);
        TextView nomConso = (TextView) viewConso.findViewById(R.id.nom_conso);
        TextView quantiteConso = (TextView) viewConso.findViewById(R.id.quantite_conso);
        Item c = consos.get(position);
        nomConso.setText(c.getTicket().getNom());
        quantiteConso.setText(Integer.toString(c.getQuantite()));
        return viewConso;
    }
}
