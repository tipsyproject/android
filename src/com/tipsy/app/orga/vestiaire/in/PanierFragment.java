package com.tipsy.app.orga.vestiaire.in;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tipsy.app.R;
import com.tipsy.app.orga.bar.PanierArrayAdapter;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Vestiaire;

import java.util.ArrayList;

/**
 * Created by Alextoss on 13/03/2014.
 */
public class PanierFragment extends Fragment {

    private PanierListener listener;
    private PanierVestiaireAdapter panierAdapter;
    private LinearLayout layoutParticipant;
    private TextView textParticipant;
    private ImageButton buttonCancel;
    private Button buttonValider;

    private Participant participant = null;
    private ArrayList<Vestiaire> tickets = new ArrayList<Vestiaire>();

    public interface PanierListener {
        public void onPanierValidated(ArrayList<Vestiaire> tickets);
        public void onRemoved(Vestiaire ticket);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (PanierListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PanierListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_vestiaire_panier, container, false);
        panierAdapter = new PanierVestiaireAdapter(getActivity(), tickets);
        ListView listView = (ListView) view.findViewById(R.id.listPanier);
        listView.setAdapter(panierAdapter);
        /* Suppression des tickets du panier */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onRemoved(tickets.get(position));
                tickets.remove(tickets.get(position));
                panierAdapter.notifyDataSetChanged();
            }
        });

        layoutParticipant = (LinearLayout) view.findViewById(R.id.layoutParticipant);
        textParticipant = (TextView) view.findViewById(R.id.textParticipant);
        buttonCancel = (ImageButton) view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Annuler le bracelet");
                builder.setMessage("Le participant ne sera plus associé aux tickets")
                        .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeParticipant();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();
            }
        });

        buttonValider = (Button) view.findViewById(R.id.buttonValider);
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tickets.isEmpty())
                    Toast.makeText(getActivity(), "Aucun ticket défini", Toast.LENGTH_SHORT).show();
                else {
                    for(Vestiaire ticket : tickets)
                        ticket.setParticipant(participant);
                    removeParticipant();
                    listener.onPanierValidated(new ArrayList<Vestiaire>(tickets));
                    tickets.clear();
                }
            }
        });

        disableValidation();
        hideParticipant();

        return view;
    }

    public void add(Vestiaire ticket){
        tickets.add(ticket);
        panierAdapter.notifyDataSetChanged();
    }

    public void setParticipant(Participant p){
        participant = p;
        enableValidation();
        showParticipant();
    }

    public void removeParticipant(){
        disableValidation();
        hideParticipant();
        participant = null;
    }

    public boolean hasParticipant(){
        return participant != null;
    }


    private void enableValidation(){
        buttonValider.setBackgroundResource(R.color.success);
        buttonValider.setText("Valider");
        buttonValider.setEnabled(true);
    }

    private void disableValidation(){
        buttonValider.setBackgroundResource(R.color.danger);
        buttonValider.setText("Attente bracelet");
        buttonValider.setEnabled(false);
    }

    private void showParticipant(){
        textParticipant.setText(participant.getFullName());
        layoutParticipant.setVisibility(View.VISIBLE);
    }

    private void hideParticipant(){
        layoutParticipant.setVisibility(View.GONE);
    }
}
