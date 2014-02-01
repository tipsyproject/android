package com.tipsy.app.orga.event;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.lib.Event;

/**
 * Created by valoo on 27/12/13.
 */

public class InitEventFragment extends Fragment {
    private EventOrgaListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EventOrgaListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        /* Chargement de l'event */
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.getInBackground(getArguments().getString("EVENT_ID"), new GetCallback<Event>() {
            @Override
            public void done(Event event, ParseException e) {
                if (event != null)
                    callback.init(event);
                else{
                    getActivity().getSupportFragmentManager().popBackStack();
                    callback.backToOrga();
                }
            }
        });
    }
}