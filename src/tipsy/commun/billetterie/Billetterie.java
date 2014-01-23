package tipsy.commun.billetterie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tipsy.commun.Event;
import tipsy.commun.commerce.Achat;
import tipsy.commun.commerce.Item;
import tipsy.commun.commerce.Panier;
import tipsy.commun.commerce.Produit;

/**
 * Created by valoo on 27/12/13.
 */
public class Billetterie {// extends ArrayList<Billet> {

    public static void refreshVentes(Event event, final Activity act, final ArrayList<Achat> output, final EntreeArrayAdapter adapter, final TextView viewVentes){
        // Seulement si des billets sont définis
        if(!event.getBilletterie().isEmpty()){
            final ProgressDialog wait = ProgressDialog.show(act,"","Chargement...",true,false);
            ArrayList<String> idBillets = new ArrayList<String>();
            for(Billet billet : event.getBilletterie())
                idBillets.add(billet.getID());

            StackMobQuery query = new StackMobQuery().fieldIsIn("produit",idBillets);
            Achat.query(Achat.class, query, StackMobOptions.depthOf(2), new StackMobQueryCallback<Achat>() {
                @Override
                public void success(List<Achat> result) {
                    output.clear();
                    output.addAll(result);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (adapter != null)
                                adapter.notifyDataSetChanged();
                            if (viewVentes != null)
                                viewVentes.setText(Integer.toString(output.size()));
                            wait.dismiss();
                            Toast.makeText(act, "Liste mise à jour.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void failure(StackMobException e) {
                    wait.dismiss();
                    Log.d("TOUTAFAIT", "erreur load entrees" + e.getMessage());
                }
            });
        }
    }
}
