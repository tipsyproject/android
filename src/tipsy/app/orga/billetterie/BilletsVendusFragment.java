package tipsy.app.orga.billetterie;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tipsy.app.R;
import tipsy.commun.Event;
import tipsy.commun.billetterie.Billetterie;
import tipsy.commun.billetterie.EntreeArrayAdapter;
import tipsy.commun.billetterie.ListeVentesFragment;
import tipsy.commun.commerce.Achat;

/**
 * Created by valoo on 27/12/13.
 */

public class BilletsVendusFragment extends ListeVentesFragment {

    protected BilletterieListener callback;
    protected ArrayList<Achat> entrees = new ArrayList<Achat>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BilletterieListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(R.layout.frag_billetterie_liste_ventes,inflater,container,savedInstanceState);
        Billetterie.refreshVentes(getEvent(), getActivity(), getEntrees(), adapter, nbVentes);
        return view;
    }

    @Override
    public Event getEvent(){
        return callback.getEvent();
    }
    @Override
    public ArrayList<Achat> getEntrees(){
        return entrees;
    }

}
