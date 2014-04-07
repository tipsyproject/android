/* package com.tipsy.app.orga.Alcotips;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.app.orga.bar.PanierArrayAdapter;
import com.tipsy.lib.util.Commerce;
import com.tipsy.lib.util.Item;
/*
/**
 * Created by Bastien on 06/04/2014.
 */
/*public class ATResultFragment extends Fragment {

        private ATListener callback;
        private PanierArrayAdapter panierAdapter;
        private ListView listView;
        private TextView textTotal;


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            callback = (ATListener) activity;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.frag_at_result, container, false);
            panierAdapter = new PanierArrayAdapter(getActivity(), callback.getAlcool());
            listView = (ListView) view.findViewById(R.id.listPanier);
            textTotal = (TextView) view.findViewById(R.id.text_total);
            listView.setAdapter(panierAdapter);

            return view;
        }

        public void update(){
            ((ArrayAdapter<Item>) listView.getAdapter()).notifyDataSetChanged();
            textTotal.setText(callback.getAlcool());
        }
    }
*/

