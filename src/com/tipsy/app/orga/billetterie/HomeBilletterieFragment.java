package com.tipsy.app.orga.billetterie;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tipsy.app.R;

/**
 * Created by valoo on 25/01/14.
 */
public class HomeBilletterieFragment extends Fragment{

    private BilletterieListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BilletterieListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_billetterie_home, container, false);


        /* Bouton VENDRE BILLET */
        Button buttonVendre = (Button) view.findViewById(R.id.button_vendre);
        buttonVendre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToVendreBillet();
            }
        });

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_billetterie_home, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                callback.backToEventOrga();
                return true;
            case R.id.action_liste_billets:
                callback.goToListeBillets();
                return true;
            case R.id.action_liste_ventes:
                callback.goToListeVentes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}