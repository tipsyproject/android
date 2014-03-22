package com.tipsy.app.orga.entree.liste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Achat;

import java.util.ArrayList;

/**
 * Created by valoo on 20/01/14.
 */
public class EntreeArrayAdapter extends ArrayAdapter<Achat> {
    private Context context;
    private ArrayList<Achat> entrees;

    public EntreeArrayAdapter(Context context, ArrayList<Achat> entrees) {
        super(context, R.layout.frag_entree, entrees);
        this.context = context;
        this.entrees = entrees;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.frag_entree, parent, false);

        Achat entree = entrees.get(position);

        TextView prenomParticipant = (TextView) view.findViewById(R.id.prenom_participant);
        TextView nomParticipant = (TextView) view.findViewById(R.id.nom_participant);

        if (entree.getPrenom().equals("") && entree.getNom().equals("")) {
            nomParticipant.setText("Participant");
            prenomParticipant.setText("Inconnu");
        } else {
            prenomParticipant.setText(entree.getPrenom());
            nomParticipant.setText(entree.getNom());
        }

        if (entree.isUsed()) {
            prenomParticipant.setTextColor(view.getResources().getColor(R.color.text_not_important_dark));
            nomParticipant.setTextColor(view.getResources().getColor(R.color.text_not_important_dark));
        }

        return view;
    }
}
