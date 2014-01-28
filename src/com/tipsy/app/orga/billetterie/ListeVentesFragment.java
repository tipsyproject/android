package com.tipsy.app.orga.billetterie;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.tipsy.app.R;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Ticket;

import java.util.List;

/**
 * Created by valoo on 27/12/13.
 */

public class ListeVentesFragment extends ListFragment {

    protected BilletterieListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BilletterieListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(callback.getVentesAdapter());
        setEmptyText(getString(R.string.empty_liste_ventes));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /* DEFINITION DE L'ACTION BAR */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_billetterie_liste_ventes, menu);
    }

    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                callback.backToHome();
                return true;
            case R.id.action_search:
                return true;
            case R.id.action_update:
                Ticket.loadVentes(callback.getBilletterie(), new FindCallback<Achat>() {
                    @Override
                    public void done(List<Achat> achats, ParseException e) {
                        if(achats != null){
                            callback.getVentes().clear();
                            callback.getVentes().addAll(achats);
                            callback.getVentesAdapter().notifyDataSetChanged();
                        }
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
