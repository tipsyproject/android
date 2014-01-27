package com.tipsy.lib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tipsy.app.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by valoo on 04/01/14.
 */

// Adapter Item
public class ItemArrayAdapter extends ArrayAdapter<Item> implements Serializable {
    private Context context;
    private ArrayList<Item> items;
    private TextView totalView;

    public ItemArrayAdapter(Context context, ArrayList<Item> items, TextView total) {
        super(context, R.layout.frag_item, items);
        this.context = context;
        this.items = items;
        this.totalView = total;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Définition de la vue
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.frag_item, parent, false);

        // Initialisation des conteneurs
        TextView nom = (TextView) view.findViewById(R.id.nom);
        TextView prix = (TextView) view.findViewById(R.id.prix);
        TextView quantite = (TextView) view.findViewById(R.id.quantite);

        // Item courant
        Item item = items.get(position);

        // Définition des valeurs
        nom.setText(item.getTicket().getNom());
        prix.setText(Commerce.prixToString(item.getTicket().getPrix(), item.getTicket().getDevise()));
        quantite.setText(Integer.toString(item.getQuantite()));

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Panier p = new Panier(items);
        totalView.setText(Commerce.prixToString(p.getPrixTotal(), p.getDevise()));
    }
}
