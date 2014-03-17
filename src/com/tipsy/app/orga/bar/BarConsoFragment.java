package com.tipsy.app.orga.bar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Commerce;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tech on 11/03/14.
 */
public class BarConsoFragment extends Fragment {

    protected BarListener callback;
    protected GridView gridView;
    protected static ConsosArrayAdapter gridAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BarListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridAdapter = new ConsosArrayAdapter(getActivity(), callback.getConso());
        if (gridView != null) {
            gridView.setAdapter(gridAdapter);
            //gridView.setEmptyView();
        }
        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //callback.goToQuantity();
            }

        });*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bar_conso, container, false);

        gridView = (GridView) view.findViewById(R.id.gridConso);
        //conso = callback.getConso().get(Ticket.CONSO);

        return view;
    }

    // Adapter CONSOS
    public class ConsosArrayAdapter extends ArrayAdapter<Ticket> implements Serializable {
        private Context context;
        private ArrayList<Ticket> consos;

        public ConsosArrayAdapter(Context context, ArrayList<Ticket> consos) {
            super(context, R.layout.frag_bar_conso_item, consos);
            this.context = context;
            this.consos = consos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewConso = inflater.inflate(R.layout.frag_bar_conso_item, parent, false);
            TextView nomConso = (TextView) viewConso.findViewById(R.id.nom_conso);
            TextView prixConso = (TextView) viewConso.findViewById(R.id.prix_conso);
            Ticket c = consos.get(position);
            nomConso.setText(c.getNom());
            prixConso.setText(Commerce.prixToString(c.getPrix(), c.getDevise()));
            return viewConso;
        }
    }

}
