package tipsy.app.membre;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import tipsy.app.R;
import tipsy.commun.Event;

/**
 * Created by Valentin on 30/12/13.
 */
public class EventFragment extends Fragment{

    private Event event;
    private MembreListener callback;

    public EventFragment(Event event){
        super();
        this.event = event;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MembreListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_event, container, false);

        TextView dateEvent = (TextView) view.findViewById(R.id.date_event);
        TextView debutEvent = (TextView) view.findViewById(R.id.debut_event);
        TextView nomEvent = (TextView) view.findViewById(R.id.nom_event);
        TextView lieuEvent = (TextView) view.findViewById(R.id.lieu_event);

        SimpleDateFormat date = new SimpleDateFormat("EEE dd MMM");
        SimpleDateFormat debut = new SimpleDateFormat("kk:mm");
        dateEvent.setText(date.format(event.getDebut()));
        debutEvent.setText(debut.format(event.getDebut()));
        nomEvent.setText(event.getNom());
        lieuEvent.setText(event.getLieu());
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        callback.setMenuTitle(event.getNom());
    }
}
