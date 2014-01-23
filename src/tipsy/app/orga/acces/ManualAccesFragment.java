package tipsy.app.orga.acces;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import tipsy.app.R;
import tipsy.commun.Event;
import tipsy.commun.billetterie.ListeVentesFragment;
import tipsy.commun.commerce.Achat;

/**
 * Created by vquefele on 20/01/14.
 */
public class ManualAccesFragment extends ListeVentesFragment {

    private AccesListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (AccesListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(R.layout.frag_access_manual,inflater,container,savedInstanceState);
        return view;
    }

    @Override
    public Event getEvent(){
        return callback.getEvent();
    }
    @Override
    public ArrayList<Achat> getEntrees(){
        return callback.getEntrees();
    }

}
