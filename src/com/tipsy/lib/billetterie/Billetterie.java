package com.tipsy.lib.billetterie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.lib.Event;
import com.tipsy.lib.commerce.Achat;

/**
 * Created by valoo on 27/12/13.
 */
public class Billetterie extends ArrayList<Billet> {

    public void refreshVentes(final Activity act, final ArrayList<Achat> output, final EntreeArrayAdapter adapter, final TextView viewVentes){
        // Seulement si des billets sont définis
        if(!isEmpty()){
            final ProgressDialog wait = ProgressDialog.show(act,"","Chargement...",true,false);
            ArrayList<String> idBillets = new ArrayList<String>();
            for(Billet billet : this)
                idBillets.add(billet.getObjectId());

            ParseQuery<Achat> query = ParseQuery.getQuery(Achat.class);
            query.include("participant");
            query.include("produit");
            query.whereEqualTo("produit",idBillets);
            query.findInBackground(new FindCallback<Achat>() {
                public void done(List<Achat> ventes, ParseException e) {
                    if (e == null) {
                        output.clear();
                        output.addAll(ventes);
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                        if (viewVentes != null)
                            viewVentes.setText(Integer.toString(output.size()));
                        wait.dismiss();
                        Toast.makeText(act, "Liste mise à jour.", Toast.LENGTH_SHORT).show();
                    } else {
                        wait.dismiss();
                        Toast.makeText(act, "Erreur lors de la mise à jour.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
