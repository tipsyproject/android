package com.tipsy.app.orga.entree.liste;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeFragment;
import com.tipsy.lib.Achat;


/**
 * Created by vquefele on 20/01/14.
 */
public class ListeFragment extends EntreeFragment {

    private EntreeArrayAdapter entreesAdapter;
    private ListView listView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_mode_liste, container, false);
        entreesAdapter = new EntreeArrayAdapter(getActivity(), callback.getEntrees());
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(entreesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Achat entree = callback.getEntrees().get(position);
                /* Action possible uniquement pour les participants identifiables */
                if(!entree.getParticipant().isAnonymous()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    /* Entrée pas encore utilisée = activation + association avec un bracelet */
                    if (!entree.isUsed()) {
                        builder.setPositiveButton("Activer", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                entree.setUsed(true);
                                entree.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            entree.setUsed(true);
                                            Toast.makeText(getActivity(), "Entrée activée", Toast.LENGTH_SHORT).show();
                                            entreesAdapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(getActivity(), "Activation annulée", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                entree.setUsed(false);
                            }
                        });
                    } else {
                        builder.setPositiveButton("Désactiver", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                entree.setUsed(false);
                                entree.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            entree.setUsed(false);
                                            Toast.makeText(getActivity(), "Entrée désactivée", Toast.LENGTH_SHORT).show();
                                            entreesAdapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(getActivity(), "Désactivation annulée", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                entree.setUsed(true);
                            }
                        });
                    }

                    /* Annuler */
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    builder.setIcon(R.drawable.ic_action_labels);
                    builder.setTitle(entree.getTicket().getNom());
                    String action = (!entree.isUsed()) ? "activer" : "désactiver";
                    String message = "Vous allez " + action + " l'entrée pour: " + entree.getParticipant().getFullName();
                    builder.setMessage(message);
                    builder.create().show();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        entreesAdapter.notifyDataSetChanged();
    }

    public EntreeArrayAdapter getEntreeAdapter(){
        return entreesAdapter;
    }



}
