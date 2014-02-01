package com.tipsy.app.orga.entree;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tipsy.lib.util.QueryCallback;

/**
 * Created by valoo on 27/12/13.
 */

public class InitEntreeFragment extends Fragment {
    private EntreeListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
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
        /* Chargement de l'event et de la billetterie */
        callback.loadEventBilletterie(getArguments().getString("EVENT_ID"), new QueryCallback() {
            @Override
            public void done(Exception e) {
                if (e == null) {
                    callback.updateEntrees(new QueryCallback() {
                        @Override
                        public void done(Exception e) {
                            if(e==null) callback.init();
                            else{
                                getActivity().getSupportFragmentManager().popBackStack();
                                callback.backToEvent();
                            }
                        }
                    });
                }else{
                    getActivity().getSupportFragmentManager().popBackStack();
                    callback.backToEvent();
                }
            }
        });
    }
}