package com.tipsy.app.orga.bar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.app.orga.billetterie.BilletterieListener;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Commerce;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tech on 11/03/14.
 */
public class BarConsoFragment  extends ListFragment {

    private BarListener callback;
    private int index;
    private Ticket conso;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BarListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BilletsArrayAdapter adapter = new BilletsArrayAdapter(getActivity(), callback.getBilletterie());
        setListAdapter(adapter);
        setEmptyText("Aucune consommation définie.");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bar_conso, container, false);

        if (savedInstanceState == null) {
            if (getArguments() != null && getArguments().containsKey("BILLET_INDEX")) {
                index = getArguments().getInt("BILLET_INDEX");
                conso = callback.getBilletterie().get(index);
            }
        } else {
            index = savedInstanceState.getInt("index");
            conso = savedInstanceState.getParcelable("Billet");
        }
        TextView nomConso = (TextView) view.findViewById(R.id.nom_conso);
        TextView prixConso = (TextView) view.findViewById(R.id.prix_conso);
        nomConso.setText(conso.getNom());
        prixConso.setText(Commerce.prixToString(conso.getPrix()));

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
        callback.goToHomeBar();
    }

    // Adapter BILLETS
    public class BilletsArrayAdapter extends ArrayAdapter<Ticket> implements Serializable {
        private Context context;
        private ArrayList<Ticket> consos;

        public BilletsArrayAdapter(Context context, ArrayList<Ticket> consos) {
            super(context, R.layout.frag_bar_conso, consos);
            this.context = context;
            this.consos = consos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewBillet = inflater.inflate(R.layout.frag_bar_conso, parent, false);
            TextView nomConso = (TextView) viewBillet.findViewById(R.id.nom_conso);
            TextView prixConso = (TextView) viewBillet.findViewById(R.id.prix_conso);
            Ticket c = consos.get(position);
            nomConso.setText(c.getNom());
            prixConso.setText(Commerce.prixToString(c.getPrix(), c.getDevise()));
            return viewBillet;
        }
    }
}
