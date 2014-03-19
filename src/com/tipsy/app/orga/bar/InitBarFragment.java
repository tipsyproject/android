package com.tipsy.app.orga.bar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.lib.util.QueryCallback;

/**
 * Created by Alextoss on 19/03/2014.
 */
public class InitBarFragment extends Fragment

{
    private BarListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BarListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        /* Chargement de l'event et de la billetterie */
        callback.loadEventConso(getArguments().getString("EVENT_ID"), new QueryCallback() {
            @Override
            public void done(Exception e) {
                if (e == null) {

                } else {
                    Toast.makeText(getActivity(), getString(R.string.erreur_interne), Toast.LENGTH_SHORT).show();
                    Log.d(TipsyApp.TAG, e.getMessage());
                }
            }
        });
    }
}
