package com.tipsy.app.orga.event.edit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tipsy.app.R;

/**
 * Created by Valoo on 05/12/13.
 */

public class EditEventDescFragment extends Fragment {

    private EditEventListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EditEventListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_edit_event_desc, container, false);
        callback.setInputNom((EditText) view.findViewById(R.id.input_nom));
        callback.getInputNom().setText(callback.getEvent().getNom());
        return view;
    }
}
