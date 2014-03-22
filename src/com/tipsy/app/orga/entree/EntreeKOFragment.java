package com.tipsy.app.orga.entree;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Achat;

/**
 * Created by valoo on 21/03/14.
 */
public class EntreeKOFragment extends Fragment {
    private TextView textTicket;
    private TextView textParticipant;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_ko, container, false);
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
        if(entree == null){
            textTicket.setText("Entrée non autorisée");
            textParticipant.setText("Bracelet inconnu");
        }else {
            textTicket.setText("Entrée déjà validée");
            textParticipant.setText(entree.getPrenom() + " " + entree.getNom());
        }
        this.getView().setVisibility(View.VISIBLE);
    }

    public void hide(){
        this.getView().setVisibility(View.GONE);
    }
}
