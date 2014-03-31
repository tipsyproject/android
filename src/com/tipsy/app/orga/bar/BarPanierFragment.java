package com.tipsy.app.orga.bar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.util.Commerce;
import com.tipsy.lib.util.Item;

/**
 * Created by Alextoss on 13/03/2014.
 */
public class BarPanierFragment extends Fragment {

    private BarListener callback;
    private PanierArrayAdapter panierAdapter;
    private ListView listView;
    private TextView textTotal;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BarListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bar_panier, container, false);
        panierAdapter = new PanierArrayAdapter(getActivity(), callback.getPanier());
        listView = (ListView) view.findViewById(R.id.listPanier);
        textTotal = (TextView) view.findViewById(R.id.text_total);
        listView.setAdapter(panierAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callback.decreaseConso(callback.getPanier().get(position));
            }
        });

        Button buttonValider = (Button) view.findViewById(R.id.button_valider);
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.validerPanier();
            }
        });

        return view;
    }

    public void update(){
        ((ArrayAdapter<Item>) listView.getAdapter()).notifyDataSetChanged();
        textTotal.setText(Commerce.prixToString(callback.getPanier().getPrixTotal()));
    }
}
