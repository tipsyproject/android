package com.tipsy.app.orga.event;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private LinearLayout buttonBar;
    private LinearLayout buttonVestiaire;
    private LinearLayout buttonAcces;
    private LinearLayout buttonInfos;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EventOrgaListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_event_home, container, false);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        TextView nomEvent = (TextView) view.findViewById(R.id.nom_event);
        nomEvent.setText(callback.getEvent().getNom());

        TextView lieuEvent = (TextView) view.findViewById(R.id.lieu_event);
        lieuEvent.setText(callback.getEvent().getLieu());

        SimpleDateFormat f_date = new SimpleDateFormat("EEE dd MMM");
        TextView dateEvent = (TextView) view.findViewById(R.id.date_event);
        dateEvent.setText(f_date.format(callback.getEvent().getDebut()));

        SimpleDateFormat f_hour = new SimpleDateFormat("kk:mm");
        TextView hourEvent = (TextView) view.findViewById(R.id.debut_event);
        hourEvent.setText(f_hour.format(callback.getEvent().getDebut()));

        /* CONTRÔLE D'ACCES */
        buttonAcces = (LinearLayout) view.findViewById(R.id.button_access);
        buttonAcces.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToAcces();
            }
        });


        /* VESTIAIRE */
        buttonVestiaire = (LinearLayout) view.findViewById(R.id.button_vestiaire);
        buttonVestiaire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Fonctionnalité à venir.", Toast.LENGTH_LONG).show();
            }
        });

        /* BAR */
        buttonBar = (LinearLayout) view.findViewById(R.id.button_bar);
        buttonBar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToBar();
            }
        });



        /* SETTINGS EVENT */
        buttonInfos = (LinearLayout) view.findViewById(R.id.button_infos);
        buttonInfos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToEditEvent();
            }
        });

        return view;
    }


}