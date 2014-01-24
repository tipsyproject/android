package com.tipsy.lib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.tipsy.app.R;

/**
 * Created by valoo on 22/01/14.
 */

public class EventArrayAdapter extends ArrayAdapter<Event> {
    private Context context;
    private ArrayList<Event> events;

    public EventArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.frag_event_item, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewEvent = inflater.inflate(R.layout.frag_event_item, parent, false);

        TextView nomEvent = (TextView) viewEvent.findViewById(R.id.nom_event);
        nomEvent.setText(events.get(position).getNom());

        TextView lieuEvent = (TextView) viewEvent.findViewById(R.id.lieu_event);
        lieuEvent.setText(events.get(position).getLieu());


        SimpleDateFormat f = new SimpleDateFormat("EEE dd MMM - kk:mm");
        TextView dateEvent = (TextView) viewEvent.findViewById(R.id.date_event);
        dateEvent.setText(f.format(events.get(position).getDebut()));

        return viewEvent;
    }
}