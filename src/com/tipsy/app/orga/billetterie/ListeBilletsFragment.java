package com.tipsy.app.orga.billetterie;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Commerce;
import com.tipsy.lib.Ticket;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by valoo on 27/12/13.
 */
public class ListeBilletsFragment extends ListFragment {

    private BilletterieListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BilletterieListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BilletsArrayAdapter adapter = new BilletsArrayAdapter(getActivity(), callback.getBilletterie());
        setListAdapter(adapter);
        setEmptyText(getString(R.string.empty_liste_billets));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_billetterie_liste_billets, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                callback.backToHome();
                return true;
            case R.id.action_new:
                callback.goToNouveauBillet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
        callback.goToEditBillet(position);
    }

    // Adapter BILLETS
    public class BilletsArrayAdapter extends ArrayAdapter<Ticket> implements Serializable {
        private Context context;
        private ArrayList<Ticket> billets;

        public BilletsArrayAdapter(Context context, ArrayList<Ticket> billets) {
            super(context, R.layout.frag_billet_list, billets);
            this.context = context;
            this.billets = billets;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewBillet = inflater.inflate(R.layout.frag_billet_list, parent, false);
            TextView nomBillet = (TextView) viewBillet.findViewById(R.id.nom_billet);
            TextView prixBillet = (TextView) viewBillet.findViewById(R.id.prix_billet);
            Ticket b = billets.get(position);
            nomBillet.setText(b.getNom());
            prixBillet.setText(Commerce.prixToString(b.getPrix(), b.getDevise()));
            return viewBillet;
        }
    }
}
