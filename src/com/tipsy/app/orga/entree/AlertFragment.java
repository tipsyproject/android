package com.tipsy.app.orga.entree;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tipsy.app.R;

import java.util.Stack;

/**
 * Created by valoo on 21/03/14.
 */
public abstract class AlertFragment extends EntreeFragment {
    protected TextView text1;
    protected TextView text2;
    protected Stack queue = new Stack();


    public View onCreateView(int layoutId, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
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
        text1.setText(m1);
        text2.setText(m2);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.show(this);
        ft.commit();


        /* On indique que le delai est à nouveau rappelé */
        queue.push(1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Au cas où on change d'activité avant la fin */
                try {
                /* On ne cache l'alerte que si elle n'a pas été réaffichée entre temps */
                    if (queue.size() < 2)
                        hide();
                    queue.pop();
                }catch(Exception e){}
            }
        }, 3000);

    }

    public void hide(){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.hide(this);
        ft.commit();
    }

}
