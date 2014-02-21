package com.tipsy.lib.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseObject;
import com.tipsy.app.R;
import com.tipsy.lib.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by valoo on 22/01/14.
 */

public class EventArrayAdapter extends ArrayAdapter<Event> {
    private Context context;
    private ArrayList<Event> events;
    protected Button delete;

    public EventArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.frag_event_item, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View viewEvent = inflater.inflate(R.layout.frag_event_item, parent, false);
        delete = (Button) viewEvent.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ProgressDialog wait = ProgressDialog.show(getContext(), null, "Suppression...", true);
                ParseObject.createWithoutData("Event", events.get(position).getObjectId()).deleteEventually();
                events.remove(events.get(position));
                notifyDataSetChanged();
                wait.dismiss();
            }
        });
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