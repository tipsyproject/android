package tipsy.app.membre;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.api.StackMobQueryField;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import tipsy.app.R;
import tipsy.commun.Event;

/**
 * Created by Valentin on 30/12/13.
 */
public class SearchEventFragment extends Fragment{

    private EventsArrayAdapter adapter;
    private ListView listView;
    private MembreListener callback;
    private ArrayList<Event> eventResults = new ArrayList<Event>();

    public SearchEventFragment(ArrayList<Event> results){
        super();
        eventResults = results;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MembreListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search_event, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        if(eventResults.size()==0){
            TextView test = (TextView) view.findViewById(R.id.no_result);
            test.setText("Aucun événement");
        }
        adapter = new EventsArrayAdapter(getActivity(),eventResults);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callback.goToEvent(eventResults.get(position));
            }
        });
        return view;
    }

    // Adapter BILLETS
    public class EventsArrayAdapter extends ArrayAdapter<Event> {
        private Context context;
        private ArrayList<Event> events;
        public EventsArrayAdapter(Context context, ArrayList<Event> events) {
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

            return viewEvent;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        callback.setMenuTitle("Résultats de la recherche");
    }
}