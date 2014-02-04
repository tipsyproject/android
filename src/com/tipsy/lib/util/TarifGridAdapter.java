package com.tipsy.lib.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Ticket;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by valoo on 04/02/14.
 */
public class TarifGridAdapter extends BaseAdapter implements Serializable {
    private Context context;
    private ArrayList<Ticket> billets;

    public TarifGridAdapter(Context context, ArrayList<Ticket> billets) {
        this.context = context;
        this.billets = billets;
    }

    public int getCount() {
        return billets.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewBillet = inflater.inflate(R.layout.frag_tarif, parent, false);
        TextView nomBillet = (TextView) viewBillet.findViewById(R.id.nom_billet);
        TextView prixBillet = (TextView) viewBillet.findViewById(R.id.prix_billet);
        Ticket b = billets.get(position);
        nomBillet.setText(b.getNom());
        prixBillet.setText(Commerce.prixToString(b.getPrix(), b.getDevise()));
        return viewBillet;
    }
}
