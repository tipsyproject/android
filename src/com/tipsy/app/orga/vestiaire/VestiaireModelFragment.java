package com.tipsy.app.orga.vestiaire;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.app.orga.entree.EntreeFragment;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.Vestiaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by valoo on 27/12/13.
 */

public class VestiaireModelFragment extends VestiaireFragment {

    /* Progress Dialog */
    private ProgressDialog initDialog;
    private boolean loadingParticipants;
    private boolean loadingVestiaires;


    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        loadingParticipants = true;
        loadingVestiaires = true;
        initDialog = ProgressDialog.show(getActivity(), null, "Mise à jour des données...", true, true);

        /* CHARGEMENT DE:
        event, participants et tickets vestiaire
         */
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(callback.getEventId(), new GetCallback<Event>() {
            @Override
            public void done(Event ev, ParseException e) {
                if (ev != null) {
                    ev.findBilletterie(new FindCallback<Ticket>() {
                        @Override
                        public void done(List<Ticket> billets, ParseException e) {
                            if (e == null) {
                                Ticket.loadVentes(new ArrayList<Ticket>(billets), new FindCallback<Achat>() {
                                    @Override
                                    public void done(List<Achat> achats, ParseException e) {
                                        if (e == null) {
                                            callback.getParticipants().clear();
                                            for (Achat entree : achats) {
                                                callback.getParticipants().add(entree.getParticipant());
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        if (!loadingVestiaires)
                                            onModelUpdated();
                                        else
                                            loadingParticipants = false;
                                    }
                                });
                            }
                        }
                    });
                    ParseQuery<Vestiaire> query = ParseQuery.getQuery(Vestiaire.class);
                    query.include("participant");
                    query.whereEqualTo("event", ev);
                    query.findInBackground(new FindCallback<Vestiaire>() {
                        @Override
                        public void done(List<Vestiaire> vestiaires, ParseException e) {
                            if (e == null) {
                                callback.getTickets().clear();
                                callback.getTickets().addAll(vestiaires);
                                Collections.sort(callback.getTickets(), Vestiaire.SORT_BY_FULLNAME);
                            } else {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            if (!loadingParticipants)
                                onModelUpdated();
                            else
                                loadingVestiaires = false;

                        }
                    });
                } else {
                    try {
                        initDialog.dismiss();
                        initDialog = null;
                    } catch (Exception er) {
                    }
                }
            }
        });

    }

    private void onModelUpdated(){
        // Fermeture de la progressDialog si elle a été affichée depuis cette fonction
        try {
            initDialog.dismiss();
            initDialog = null;
        } catch (Exception er) {
        }
        callback.modelUpdated();
    }

    @Override
    public void onPause(){
        if(initDialog != null)
            initDialog.dismiss();
        super.onPause();
    }

}