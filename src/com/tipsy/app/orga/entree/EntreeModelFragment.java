package com.tipsy.app.orga.entree;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Ticket;
import java.util.Collections;
import java.util.List;

/**
 * Created by valoo on 27/12/13.
 */

public class EntreeModelFragment extends EntreeFragment {

    /* Progress Dialog */
    private ProgressDialog initDialog;


    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        initDialog = ProgressDialog.show(getActivity(), null, "Mise à jour des entrées...", true, true);

        /* CHARGEMENT DE:
        event, billetterie et entrees
         */
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(callback.getEventId(), new GetCallback<Event>() {
            @Override
            public void done(Event ev, ParseException e) {
                if (ev != null) {
                    callback.setEvent(ev);
                    callback.getEvent().findBilletterie(new FindCallback<Ticket>() {
                        @Override
                        public void done(List<Ticket> billets, ParseException e) {
                            if (e == null) {
                                callback.getBilletterie().clear();
                                callback.getBilletterie().addAll(billets);
                                Ticket.loadVentes(callback.getBilletterie(), new FindCallback<Achat>() {
                                    @Override
                                    public void done(List<Achat> achats, ParseException e) {
                                        if (e == null) {
                                            callback.getEntrees().clear();
                                            callback.getEntrees().addAll(achats);
                                            Collections.sort(callback.getEntrees(), Achat.SORT_BY_FIRSTNAME);
                                            Toast.makeText(getActivity(), "Mise à jour effectuée", Toast.LENGTH_SHORT).show();
                                            callback.modelUpdated();
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

    }

    @Override
    public void onPause(){
        if(initDialog != null)
            initDialog.dismiss();
        super.onPause();
    }

}