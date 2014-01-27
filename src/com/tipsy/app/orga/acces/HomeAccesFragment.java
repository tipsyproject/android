package com.tipsy.app.orga.acces;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tipsy.app.R;

/**
 * Created by vquefele on 20/01/14.
 */
public class HomeAccesFragment extends Fragment {

    private AccesListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (AccesListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_access_home, container, false);
        LinearLayout buttonSearch = (LinearLayout) view.findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToManualAccess();
            }
        });
        return view;
    }

}
