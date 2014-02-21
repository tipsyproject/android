package com.tipsy.app.orga.entree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tipsy.app.R;
import com.tipsy.lib.Achat;


/**
 * Created by vquefele on 20/01/14.
 */
public class ModeManuelFragment extends ListFragment {

    private EntreeListener callback;
    private EntreeArrayAdapter entreesAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        entreesAdapter = new EntreeArrayAdapter(getActivity(), callback.getEntrees());
        setListAdapter(entreesAdapter);
        setEmptyText(getString(R.string.empty_liste_ventes));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        final Achat entree = callback.getEntrees().get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Dialog));
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                entree.setUsed(true);
                entree.saveInBackground();
                callback.updateProgress();
                entreesAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.setIcon(R.drawable.ic_action_person);

        if (entree.getPrenom().equals("") && entree.getNom().equals(""))
            builder.setTitle("Participant inconnu");
        else builder.setTitle(entree.getNom() + " " + entree.getPrenom());
        builder.setTitle(entree.getNom() + " " + entree.getPrenom());
        builder.setMessage(entree.getTicket().getNom());
        builder.create().show();
    }


    public void updateListe() {
        entreesAdapter.notifyDataSetChanged();
    }

}
