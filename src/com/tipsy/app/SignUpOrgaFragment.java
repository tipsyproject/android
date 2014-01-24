package com.tipsy.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alexandre on 04/01/14.
 */
public class SignUpOrgaFragment extends Fragment {

    protected TipsyApp app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View View = inflater.inflate(R.layout.frag_signup_orga, container, false);
        View.findViewById(R.id.layout_signup_orga).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                app.hideKeyboard(getActivity());
                return false;
            }
        });
        return View;
    }
}
