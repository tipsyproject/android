package com.tipsy.app.membre;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;

/**
 * Created by Alexandre on 04/01/14.
 */
public class SignUpMembreFragment extends Fragment {

    protected TipsyApp app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View View = inflater.inflate(R.layout.frag_signup_membre, container, false);
        View.findViewById(R.id.layout_signup_membre).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                app.hideKeyboard(getActivity());
                return false;
            }
        });
        return View;
    }
}
