package tipsy.app.billetterie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ListView;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import tipsy.app.R;
import tipsy.commun.Event;
import tipsy.commun.billetterie.Billet;
import tipsy.commun.commerce.Commande;
import tipsy.commun.commerce.Commerce;
import tipsy.commun.commerce.Item;
import tipsy.commun.commerce.Produit;
import tipsy.commun.commerce.Transaction;

/**
 * Created by valoo on 27/12/13.
 */
public class ListeVentesFragment extends Fragment {

    private BilletArrayAdapter adapter;
    private Event event;
    private TextView nbVentes;
    private ArrayList<Item> ventes = new ArrayList<Item>();
    private ListView listView;
    private BilletterieListener callback;


    public static ListeVentesFragment init(Event e) {
        ListeVentesFragment frag = new ListeVentesFragment();
        Bundle args = new Bundle();
        args.putParcelable("Event", e);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BilletterieListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_billetterie_liste_ventes, container, false);
        /* On récupère l'event courant */
        event = getArguments().getParcelable("Event");

        listView = (ListView) view.findViewById(R.id.list);
        nbVentes = (TextView) view.findViewById(R.id.nb_billets);
        adapter = new BilletArrayAdapter(getActivity(), ventes);
        listView.setAdapter(adapter);

        Log.d("TOUTAFAIT", "loadVentes");
        loadVentes();

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editBillet(event.getBilletterie().get(position), false);
            }
        });*/
        return view;
    }


    public void loadVentes(){
        StackMobQuery query =
                new StackMobQuery().fieldIsEqualTo("titre", "Test x1");


        Commande.query(Commande.class, query, StackMobOptions.depthOf(1),new StackMobQueryCallback<Commande>() {
            @Override
            public void success(List<Commande> result) {
                Log.d("TOUTAFAIT", "nb" + Integer.toString(result.size()));
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT", "erreur" + e.getMessage());
            }
        });
        /*
        ArrayList<String> idBillets = new ArrayList<String>();
        Iterator it = event.getBilletterie().iterator();
        Billet billet;
        while(it.hasNext()){
            billet = (Billet) it.next();
            idBillets.add(billet.getID());
            Log.d("TOUTAFAIT", "billet:"+billet.getID());
        }
        //StackMobQuery query = new StackMobQuery().fieldIsIn("produit",idBillets);
        StackMobQuery query = new StackMobQuery().fieldIsEqualTo("produit","5dd76c82cd79459b926574c92519e6a8");
        Item.query(Item.class, query, new StackMobQueryCallback<Item>(){
            @Override
            public void success(List<Item> result) {
                Log.d("TOUTAFAIT", "nb ventes" + Integer.toString(result.size()));
                Iterator it = result.iterator();
                while (it.hasNext()) {
                    ventes.add((Item) it.next());
                }
                adapter.notifyDataSetChanged();
                nbVentes.setText(Integer.toString(result.size()));
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT", "erreur load ventes" + e.getMessage());
            }
        });*/

    }

}
