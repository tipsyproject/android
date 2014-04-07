package com.tipsy.app.orga.entree.liste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Participant;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by valoo on 20/01/14.
 */
public class EntreeArrayAdapter extends ArrayAdapter<Achat> implements Filterable{
    private Context context;
    private ArrayList<Achat> entrees;
    private ArrayList<Achat> entreesNonAnonymes;

    public EntreeArrayAdapter(Context context, ArrayList<Achat> entrees) {
        super(context, R.layout.frag_entree, entrees);
        this.context = context;
        this.entrees = entrees;
        this.entreesNonAnonymes = new ArrayList<Achat>();
    }

    public Achat get(int position){
        return entreesNonAnonymes.get(position);
    }

    @Override
    public int getCount() {
        return entreesNonAnonymes.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.frag_entree, parent, false);


        Achat entree = entreesNonAnonymes.get(position);

        TextView nomParticipant = (TextView) view.findViewById(R.id.participant);

        nomParticipant.setText(entree.getParticipant().getFullName());

        if (entree.isUsed())
            nomParticipant.setTextColor(view.getResources().getColor(R.color.text_not_important_dark));

        return view;
    }
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                entreesNonAnonymes = (ArrayList<Achat>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<Achat> filteredEntrees = new ArrayList<Achat>();

                for(Achat entree : entrees){
                    if(!entree.getParticipant().isAnonymous())
                        filteredEntrees.add(entree);
                }


                Collections.sort(filteredEntrees, Achat.SORT_BY_FULLNAME);
                results.count = filteredEntrees.size();
                results.values = filteredEntrees;

                return results;
            }
        };

        return filter;
    }
}
