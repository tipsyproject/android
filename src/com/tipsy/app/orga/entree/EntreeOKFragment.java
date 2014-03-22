package com.tipsy.app.orga.entree;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Achat;


/**
 * Created by valoo on 21/03/14.
 */
public class EntreeOKFragment extends Fragment {
    private TextView textTicket;
    private TextView textParticipant;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_ok, container, false);
        textTicket = (TextView) view.findViewById(R.id.text_ticket);
        textParticipant = (TextView) view.findViewById(R.id.text_participant);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        return view;
    }

    public void show(Achat entree){
        textTicket.setText(entree.getTicket().getNom());
        textParticipant.setText(entree.getPrenom() + " " + entree.getNom());
        this.getView().setVisibility(View.VISIBLE);
    }

    public void hide(){
        this.getView().setVisibility(View.GONE);
    }
}
