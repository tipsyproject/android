package com.tipsy.lib.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.tipsy.app.R;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by valoo on 22/01/14.
 */

public class EventArrayAdapter extends ArrayAdapter<Event> {
    private Context context;
    private ArrayList<Event> events;
    protected RelativeLayout Event;
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
                events.remove(position);
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
        Event = (RelativeLayout) viewEvent.findViewById(R.id.layout_frag_item);
        viewEvent.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                if (delete.getVisibility() == View.VISIBLE) {
                    Event.setBackgroundResource(R.color.background_menu);
                    Event.setAlpha(1f);
                    delete.setVisibility(View.GONE);
                } else {
                    Event.setBackgroundResource(R.drawable.event_fade);
                    Event.setAlpha(0.4f);
                    delete.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
        viewEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EventOrgaActivity.class);
                intent.putExtra("EVENT_ID", events.get(position).getObjectId());
                getContext().startActivity(intent);
            }});

        return viewEvent;
    }

    public void onBackPressed() {
        if (delete.getVisibility() == View.VISIBLE){
            Event.setBackgroundResource(R.color.background_menu);
            Event.setAlpha(1f);
            delete.setVisibility(View.GONE);
        }
    }
}