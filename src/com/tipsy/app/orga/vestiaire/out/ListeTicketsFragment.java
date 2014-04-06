package com.tipsy.app.orga.vestiaire.out;

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
import com.tipsy.app.orga.vestiaire.in.PanierVestiaireAdapter;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Vestiaire;

import java.util.ArrayList;

/**
 * Created by Alextoss on 13/03/2014.
 */
public class ListeTicketsFragment extends Fragment {

    private ListeTicketsListener listener;
    private PanierVestiaireAdapter panierAdapter;
    private LinearLayout layoutParticipant;
    private TextView textParticipant;
    private ImageButton buttonCancel;
    private Button buttonValider;

    private Participant participant = null;
    private ArrayList<Vestiaire> tickets = new ArrayList<Vestiaire>();

    public interface ListeTicketsListener {
        public void onReturnAll(ArrayList<Vestiaire> tickets);
        public void onReturn(Vestiaire ticket);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ListeTicketsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(" must implement ListeTicketsListener");
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
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(tickets.get(position).isRendu()) {
                    Toast.makeText(getActivity(),"Article déjà rendu",Toast.LENGTH_SHORT);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Rendre l'article");
                    builder.setMessage("Vous allez rendre l'article " + tickets.get(position).getNumber());
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            tickets.get(position).setRendu(true);
                            listener.onReturn(tickets.get(position));
                            panierAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder.show();
                }

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
                                tickets.clear();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();
            }
        });

        buttonValider = (Button) view.findViewById(R.id.buttonValider);
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Tout rendre");
                builder.setMessage("Vous allez rendre tous les articles");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for(Vestiaire ticket : tickets)
                            ticket.setRendu(true);
                        removeParticipant();
                        listener.onReturnAll(new ArrayList<Vestiaire>(tickets));
                        tickets.clear();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.show();
            }
        });

        disableValidation();
        hideParticipant();

        return view;
    }

    public void addAll(ArrayList<Vestiaire> tickets){
        this.tickets.addAll(tickets);
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
        buttonValider.setText("Tout rendre");
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
