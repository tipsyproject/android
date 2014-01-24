package com.tipsy.app.orga.event;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tipsy.app.R;

/**
 * Created by valoo on 27/12/13.
 */

public class HomeEventOrgaFragment extends Fragment {
    private EventOrgaListener callback;
    private LinearLayout buttonBilleterie;
    private LinearLayout buttonBar;
    private LinearLayout buttonAcces;
    private LinearLayout buttonInfos;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EventOrgaListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_event_home, container, false);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        /* CONTRÔLE D'ACCES */
        buttonAcces = (LinearLayout) view.findViewById(R.id.button_access);
        buttonAcces.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToAcces();
            }
        });


        /* BAR */
        buttonBar = (LinearLayout) view.findViewById(R.id.button_bar);
        buttonBar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Fonctionnalité à venir.", Toast.LENGTH_LONG).show();
            }
        });

        /* BILLETTERIE */
        buttonBilleterie = (LinearLayout) view.findViewById(R.id.button_billetterie);
        buttonBilleterie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToBilletterie();
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