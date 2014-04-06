package com.tipsy.app.orga.vestiaire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Vestiaire;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by valoo on 06/04/14.
 */
public class VestiaireAdapter extends ArrayAdapter<Vestiaire> implements Filterable {
    private Context context;
    private ArrayList<Vestiaire> tickets;
    private ArrayList<Participant> participants;

    public VestiaireAdapter(Context context, ArrayList<Vestiaire> tickets) {
        super(context, R.layout.frag_entree, tickets);
        this.context = context;
        this.tickets = tickets;
        this.participants = new ArrayList<Participant>();
    }

    public Participant get(int position){
        return participants.get(position);
    }

    @Override
    public int getCount() {
        return participants.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.frag_entree, parent, false);

        Participant participant = participants.get(position);

        TextView nomParticipant = (TextView) view.findViewById(R.id.participant);

        nomParticipant.setText(participant.getFullName());

        return view;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                participants = (ArrayList<Participant>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<Participant> filteredParticipants = new ArrayList<Participant>();

                for(Vestiaire ticket : tickets){
                    if(!ticket.getParticipant().isAnonymous() &&
                            !filteredParticipants.contains(ticket.getParticipant()))
                        filteredParticipants.add(ticket.getParticipant());
                }


                Collections.sort(filteredParticipants, Participant.SORT_BY_FULLNAME);
                results.count = filteredParticipants.size();
                results.values = filteredParticipants;

                return results;
            }
        };

        return filter;
    }
}
