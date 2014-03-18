package com.tipsy.app.orga.bar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.util.Item;

/**
 * Created by valoo on 18/03/14.
 */
public class BarQuantiteFragment extends Fragment {

    private BarListener callback;
    private Button buttonPlus;
    private Button buttonMoins;
    private Button buttonValidate;
    private TextView textQuantite;
    private Item item;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BarListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bar_quantite, container, false);

        buttonPlus = (Button) view.findViewById(R.id.button_plus);
        buttonMoins = (Button) view.findViewById(R.id.button_moins);
        buttonValidate = (Button) view.findViewById(R.id.button_validate);
        textQuantite = (TextView) view.findViewById(R.id.text_quantite);

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setQuantite(item.getQuantite()+1);
                textQuantite.setText(Integer.toString(item.getQuantite()));
            }
        });

        buttonMoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* La quantité doit être supérieure à 0 */
                if(item.getQuantite()>1) {
                    item.setQuantite(item.getQuantite() - 1);
                    textQuantite.setText(Integer.toString(item.getQuantite()));
                }
            }
        });


        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.addItemToPanier(item);
            }
        });
        return view;
    }

    /* Passage de l'item courant */
    public void setItem(Item item){
        this.item = item;
        this.textQuantite.setText(Integer.toString(item.getQuantite()));
    }

}
