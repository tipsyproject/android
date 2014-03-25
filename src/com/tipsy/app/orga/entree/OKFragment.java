package com.tipsy.app.orga.entree;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tipsy.app.R;

/**
 * Created by valoo on 21/03/14.
 */
public class OKFragment extends EntreeFragment {
    private TextView text1;
    private TextView text2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_ok, container, false);
        text1 = (TextView) view.findViewById(R.id.text_1);
        text2 = (TextView) view.findViewById(R.id.text_2);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        return view;
    }

    public void show(String m1, String m2){
        //text1.setText(m1);
        //text2.setText(m2);
    }

    public void hide(){
    }
}
