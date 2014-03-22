package com.tipsy.app.orga.entree;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.liste.ListeFragment;

import java.lang.reflect.Field;


/**
 * Created by vquefele on 20/01/14.
 */
public abstract class ModeFragment extends Fragment {

    protected EntreeListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
    }

    /* Permet de corriger un bug dû aux fragments imbriqués */
    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_mode, container, false);
        return view;
    }

}
