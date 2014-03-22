package com.tipsy.app.orga.bar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.lib.util.Item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alextoss on 13/03/2014.
 */
public class BarPanierFragment extends ListFragment {

    private BarListener callback;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BarListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PanierArrayAdapter adapter = new PanierArrayAdapter(getActivity(), callback.getPanier());
        setListAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bar_panier, container, false);

        return view;
    }

    // Adapter PANIER
    public class PanierArrayAdapter extends ArrayAdapter<Item> implements Serializable {
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
            LinearLayout layoutItem = (LinearLayout) viewConso.findViewById(R.id.layout_item);
            TextView nomConso = (TextView) viewConso.findViewById(R.id.nom_conso);
            TextView quantiteConso = (TextView) viewConso.findViewById(R.id.quantite_conso);
            ImageButton remove = (ImageButton)  viewConso.findViewById(R.id.remove);
            Item c = consos.get(position);
            nomConso.setText(c.getTicket().getNom());
            quantiteConso.setText(Integer.toString(c.getQuantite()));
            remove.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    callback.onRemove(position);
                }});
            layoutItem.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    callback.goToQuantity(callback.getPanier().get(position).getTicket());
                }});
            return viewConso;
        }
    }
}
