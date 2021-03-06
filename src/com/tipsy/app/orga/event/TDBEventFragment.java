package com.tipsy.app.orga.event;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tipsy.app.R;

import java.text.SimpleDateFormat;

/**
 * Created by valoo on 27/12/13.
 */

public class TDBEventFragment extends Fragment {
    private EventOrgaListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EventOrgaListener) activity;
    }
    /*
    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_event_home, container, false);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        TextView nomEvent = (TextView) view.findViewById(R.id.nom_event);
        nomEvent.setText(callback.getEvent().getNom());


        if(callback.getEvent().getLieu() != null) {
            TextView lieuEvent = (TextView) view.findViewById(R.id.lieu_event);
            lieuEvent.setText(callback.getEvent().getLieu());
        }

        if(callback.getEvent().getDebut() != null) {
            SimpleDateFormat f_date = new SimpleDateFormat("EEE dd MMM");
            TextView dateEvent = (TextView) view.findViewById(R.id.date_event);
            dateEvent.setText(f_date.format(callback.getEvent().getDebut()));

            SimpleDateFormat f_hour = new SimpleDateFormat("kk:mm");
            TextView hourEvent = (TextView) view.findViewById(R.id.debut_event);
            hourEvent.setText(f_hour.format(callback.getEvent().getDebut()));
        }


        LinearLayout buttonEntree = (LinearLayout) view.findViewById(R.id.buttonEntree);
        buttonEntree.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToEntree();
            }
        });


        LinearLayout buttonBar = (LinearLayout) view.findViewById(R.id.buttonBar);
        buttonBar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToBar();
            }
        });



        LinearLayout buttonVestiaire = (LinearLayout) view.findViewById(R.id.buttonVestiaire);
        buttonVestiaire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToVestiaire();
            }
        });


        LinearLayout buttonAlcoTips = (LinearLayout) view.findViewById(R.id.buttonAlcoTips);
        buttonAlcoTips.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToAlcoTips();
            }
        });

        return view;
    }


}