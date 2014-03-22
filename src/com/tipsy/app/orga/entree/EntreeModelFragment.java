package com.tipsy.app.orga.entree;

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
import com.tipsy.lib.Ticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by valoo on 27/12/13.
 */

public class EntreeModelFragment extends Fragment {
    /* Données */
    private Event event;
    private ArrayList<Ticket> billetterie = new ArrayList<Ticket>();
    private ArrayList<Achat> entrees = new ArrayList<Achat>();

    private EntreeListener callback;
    /* Progress Dialog */
    private ProgressDialog initDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if(savedInstanceState == null) {

            initDialog = ProgressDialog.show(getActivity(), null, "Chargement...", true, true);

            /* CHARGEMENT DE:
            event, billetterie et entrees
             */

            ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
            query.getInBackground(getArguments().getString("EVENT_ID"), new GetCallback<Event>() {
                @Override
                public void done(Event ev, ParseException e) {
                    if (ev != null) {
                        event = ev;
                        event.findBilletterie(new FindCallback<Ticket>() {
                            @Override
                            public void done(List<Ticket> billets, ParseException e) {
                                if (e == null) {
                                    billetterie.clear();
                                    billetterie.addAll(billets);
                                    Ticket.loadVentes(getBilletterie(), new FindCallback<Achat>() {
                                        @Override
                                        public void done(List<Achat> achats, ParseException e) {
                                            if (e == null) {
                                                entrees.clear();
                                                entrees.addAll(achats);
                                                Collections.sort(entrees, Achat.SORT_BY_NAME);
                                                callback.updateProgress();
                                                Toast.makeText(getActivity(), "Mise à jour effectuée", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            // Fermeture de la progressDialog si elle a été affichée depuis cette fonction
                                            try {
                                                initDialog.dismiss();
                                                initDialog = null;
                                            } catch (Exception er) {
                                            }
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
                    } else {
                        try {
                            initDialog.dismiss();
                            initDialog = null;
                        } catch (Exception er) {
                        }
                    }
                }
            });
        }else{
            event = savedInstanceState.getParcelable("Event");
            billetterie = savedInstanceState.getParcelableArrayList("Billetterie");
            entrees = savedInstanceState.getParcelableArrayList("Entrees");
        }

    }

    @Override
    public void onPause(){
        if(initDialog != null)
            initDialog.dismiss();
        super.onPause();
    }


    public Event getEvent(){
        return event;
    }
    public ArrayList<Achat> getEntrees(){
        return entrees;
    }
    public ArrayList<Ticket> getBilletterie(){
        return billetterie;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelable("Event",event);
        outState.putParcelableArrayList("Billetterie", billetterie);
        outState.putParcelableArrayList("Entrees", entrees);
        super.onSaveInstanceState(outState);
    }

}