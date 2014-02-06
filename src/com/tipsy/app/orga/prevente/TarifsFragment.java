package com.tipsy.app.orga.prevente;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.tipsy.app.R;
import com.tipsy.app.membre.event.EventParticiperFragment;
import com.tipsy.app.orga.entree.EntreeListener;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Commerce;
import com.tipsy.lib.util.TarifGridAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by valoo on 25/01/14.
 */
public class TarifsFragment extends Fragment {

    private PreventeListener callback;
    private GridView gridView;
    private TarifGridAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (PreventeListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_vendre, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setEmptyView(view.findViewById(R.id.empty_liste));
        adapter = new TarifGridAdapter(getActivity(), callback.getBilletterie());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TOUTAFAIT", "click listener");
                callback.getPrevente().setTicket(callback.getBilletterie().get(i));
                callback.goToParticipant();
            }
        });
        return view;
    }
}
