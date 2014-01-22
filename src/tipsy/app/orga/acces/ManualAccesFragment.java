package tipsy.app.orga.acces;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import tipsy.app.R;
import tipsy.commun.Event;
import tipsy.commun.billetterie.EntreeArrayAdapter;
import tipsy.commun.commerce.Achat;

/**
 * Created by vquefele on 20/01/14.
 */
public class ManualAccesFragment extends Fragment {

    private Event event;
    private ArrayList<Achat> entrees;
    private AccesListener callback;
    private ListView listView;
    private EntreeArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (AccesListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_access_manual, container, false);


        listView = (ListView) view.findViewById(R.id.list);
        adapter = new EntreeArrayAdapter(getActivity(), callback.getEntrees());
        listView.setAdapter(adapter);
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //editBillet(event.getBilletterie().get(position), false);
            }
        });*/

        return view;
    }


    // Redéfinition de l'actionBar: Bouton de validation
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_access_manual, menu);
    }

    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_refresh:
                callback.refresh(adapter);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
