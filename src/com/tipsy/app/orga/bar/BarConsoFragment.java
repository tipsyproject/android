package com.tipsy.app.orga.bar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.app.R;
import com.tipsy.app.orga.billetterie.BilletterieListener;
import com.tipsy.app.orga.billetterie.HomeBilletterieFragment;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Commerce;
import com.tipsy.lib.util.QueryCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tech on 11/03/14.
 */
public class BarConsoFragment  extends ListFragment {

    private BarListener callback;
    private int index;
    private String event;
    private Ticket conso;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BarListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ConsosArrayAdapter adapter = new ConsosArrayAdapter(getActivity(), callback.getConso());
        setListAdapter(adapter);
        setEmptyText("Aucune consommation définie.");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bar_conso, container, false);

        if (savedInstanceState == null) {
            if (getArguments() != null && getArguments().containsKey("BILLET_INDEX")) {
                index = getArguments().getInt("CONSO_INDEX");
                conso = callback.getConso().get(index);
            }
        } else {
            index = savedInstanceState.getInt("CONSO_INDEX");
            conso = savedInstanceState.getParcelable("Conso");
            event = savedInstanceState.getString("EVENT_ID");
        }
        /*TextView nomConso = (TextView) view.findViewById(R.id.nom_conso);
        TextView prixConso = (TextView) view.findViewById(R.id.prix_conso);
        nomConso.setText(conso.getNom());
        prixConso.setText(Commerce.prixToString(conso.getPrix()));
        */
        final ProgressDialog wait = ProgressDialog.show(getActivity(), null, "Chargement...", true, true);
        loadEventConso(event, index, new QueryCallback() {
            @Override
            public void done(Exception e) {
                if (e == null) {

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.content, new HomeBilletterieFragment());
                    ft.commit();

                } else {
                    Toast.makeText(getActivity(), getString(R.string.erreur_interne), Toast.LENGTH_SHORT).show();
                    wait.dismiss();
                }
            }
        });
        return view;
    }

    public void findConso(int index, FindCallback cb) {
        ParseQuery<Ticket> query = ParseQuery.getQuery(Ticket.class);
        query.include("event");
        query.whereEqualTo("event", this);
        query.whereEqualTo("type", index);
        query.findInBackground(cb);
    }

    public void loadEventConso(String eventId, final int index, final QueryCallback callback_e) {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(eventId, new GetCallback<Event>() {
            @Override
            public void done(Event ev, ParseException e) {
                if (ev != null) {
                    findConso(index, new FindCallback<Ticket>() {
                        @Override
                        public void done(List<Ticket> consos, ParseException e) {
                            if (e == null) {
                                callback.getConso().clear();
                                callback.getConso().addAll(consos);
                            }
                            callback_e.done(e);
                        }
                    });
                } else {
                    callback_e.done(e);
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
        callback.goToHomeBar();
    }

    // Adapter BILLETS
    public class ConsosArrayAdapter extends ArrayAdapter<Ticket> implements Serializable {
        private Context context;
        private ArrayList<Ticket> consos;

        public ConsosArrayAdapter(Context context, ArrayList<Ticket> consos) {
            super(context, R.layout.frag_bar_conso, consos);
            this.context = context;
            this.consos = consos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewConso = inflater.inflate(R.layout.frag_bar_conso, parent, false);
            TextView nomConso = (TextView) viewConso.findViewById(R.id.nom_conso);
            TextView prixConso = (TextView) viewConso.findViewById(R.id.prix_conso);
            Ticket c = consos.get(position);
            nomConso.setText(c.getNom());
            prixConso.setText(Commerce.prixToString(c.getPrix(), c.getDevise()));
            return viewConso;
        }
    }
}
