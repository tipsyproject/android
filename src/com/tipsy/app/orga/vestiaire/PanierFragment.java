package com.tipsy.app.orga.vestiaire;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.app.orga.bar.BarListener;
import com.tipsy.app.orga.bar.PanierArrayAdapter;
import com.tipsy.lib.util.Commerce;
import com.tipsy.lib.util.Item;

/**
 * Created by Alextoss on 13/03/2014.
 */
public class PanierFragment extends VestiaireFragment {

    private PanierArrayAdapter panierAdapter;
    private ListView listView;
    private LinearLayout layoutParticipant;
    private TextView textParticipant;
    private Button buttonValider;
    private boolean validation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_vestiaire_panier, container, false);
        //panierAdapter = new PanierArrayAdapter(getActivity(), callback.getPanier());
        listView = (ListView) view.findViewById(R.id.listPanier);
        //listView.setAdapter(panierAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //callback.decreaseConso(callback.getPanier().get(position));
            }
        });

        layoutParticipant = (LinearLayout) view.findViewById(R.id.layoutParticipant);
        textParticipant = (TextView) view.findViewById(R.id.textParticipant);


        buttonValider = (Button) view.findViewById(R.id.buttonValider);
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation){

                }
            }
        });

        allowValidation(false);

        return view;
    }

    public void update(){
        //((ArrayAdapter<Item>) listView.getAdapter()).notifyDataSetChanged();

    }

    public void allowValidation(boolean allow){
        validation = allow;
        buttonValider.setBackgroundResource(allow ? R.color.success : R.color.danger);
        buttonValider.setText(allow ? "Valider" : "Bracelet en attente");
        if(allow)
            showParticipant();
        else
            hideParticipant();

    }

    private void showParticipant(){
        textParticipant.setText(callback.getCurrentParticipant().getFullName());
        layoutParticipant.setVisibility(View.VISIBLE);
    }

    private void hideParticipant(){
        layoutParticipant.setVisibility(View.GONE);
    }
}
