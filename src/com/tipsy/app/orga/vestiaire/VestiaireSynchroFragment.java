package com.tipsy.app.orga.vestiaire;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
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

public class VestiaireSynchroFragment extends Fragment {

    private boolean loadingParticipants;
    private boolean loadingVestiaires;
    private ArrayList<Vestiaire> tickets;
    private ArrayList<Participant> participants;
    private VestiaireSynchroListener listener;

    public interface VestiaireSynchroListener {
        public void onSynchro(ArrayList<Vestiaire> tickets, ArrayList<Participant> participants);
    }

    @Override
     public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (VestiaireSynchroListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement VestiaireSynchroListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        String eventId = getArguments().getString("eventId");
        loadingParticipants = true;
        loadingVestiaires = true;


        /* CHARGEMENT DE:
        event, participants et tickets vestiaire
         */
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(eventId, new GetCallback<Event>() {
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
                                            participants = new ArrayList<Participant>();
                                            for (Achat entree : achats) {
                                                participants.add(entree.getParticipant());
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
                    /* Tickets de vestiaire */
                    ParseQuery<Vestiaire> query = ParseQuery.getQuery(Vestiaire.class);
                    query.include("participant");
                    query.whereEqualTo("event", ev.getObjectId());
                    query.setLimit(1000);
                    query.findInBackground(new FindCallback<Vestiaire>() {
                        @Override
                        public void done(List<Vestiaire> vestiaires, ParseException e) {
                            if (e == null) {
                                tickets = new ArrayList<Vestiaire>();
                                tickets.addAll(vestiaires);
                                Collections.sort(tickets, Vestiaire.SORT_BY_FULLNAME);
                            } else {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            if (!loadingParticipants)
                                onModelUpdated();
                            else
                                loadingVestiaires = false;

                        }
                    });
                }
            }
        });

    }

    private void onModelUpdated(){
        listener.onSynchro(tickets,participants);
    }

}