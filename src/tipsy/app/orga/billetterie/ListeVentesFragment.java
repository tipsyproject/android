package tipsy.app.orga.billetterie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tipsy.app.R;
import tipsy.commun.Event;
import tipsy.commun.billetterie.Billet;
import tipsy.commun.commerce.Achat;
import tipsy.commun.commerce.AchatArrayAdapter;

/**
 * Created by valoo on 27/12/13.
 */
public class ListeVentesFragment extends Fragment {

    private AchatArrayAdapter adapter;
    private Event event;
    private TextView nbVentes;
    private ArrayList<Achat> ventes = new ArrayList<Achat>();
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
        adapter = new AchatArrayAdapter(getActivity(), ventes);
        listView.setAdapter(adapter);

        loadVentes();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //editBillet(event.getBilletterie().get(position), false);
            }
        });
        return view;
    }



    // Redéfinition de l'actionBar: Bouton de validation
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_billetterie_liste_ventes, menu);
    }

    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_update:
                loadVentes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void loadVentes(){

        final ProgressDialog wait = ProgressDialog.show(getActivity(),"","Chargement...",true,false);
        ArrayList<String> idBillets = new ArrayList<String>();
        Iterator it = event.getBilletterie().iterator();
        Billet billet;
        while(it.hasNext()){
            billet = (Billet) it.next();
            idBillets.add(billet.getID());
        }
        StackMobQuery query = new StackMobQuery().fieldIsIn("produit",idBillets);
        Achat.query(Achat.class, query, StackMobOptions.depthOf(2), new StackMobQueryCallback<Achat>() {
            @Override
            public void success(List<Achat> result) {
                ventes.clear();
                ventes.addAll(result);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        nbVentes.setText(Integer.toString(ventes.size()));
                        wait.dismiss();
                    }
                });
            }

            @Override
            public void failure(StackMobException e) {
                wait.dismiss();
                Log.d("TOUTAFAIT", "erreur load ventes" + e.getMessage());
            }
        });

    }

}
