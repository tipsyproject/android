package tipsy.app.orga.acces;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.ArrayList;
import java.util.List;

import tipsy.app.R;
import tipsy.app.orga.OrgaActivity;
import tipsy.app.orga.event.EventOrgaActivity;
import tipsy.commun.Event;
import tipsy.commun.billetterie.Billet;
import tipsy.commun.billetterie.EntreeArrayAdapter;
import tipsy.commun.commerce.Achat;

/**
 * Created by vquefele on 20/01/14.
 */
public class AccesActivity extends FragmentActivity implements AccesListener {

    private Event event;
    private ArrayList<Achat> entrees= new ArrayList<Achat>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_access);
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Contrôle d'accès");

        if(savedInstanceState != null && savedInstanceState.containsKey("Entrees")){
            entrees =  savedInstanceState.getParcelableArrayList("Entrees");
            event =  savedInstanceState.getParcelable("Event");
        }else{
            event = getIntent().getParcelableExtra("Event");
            refresh(null);
            goToHome(false);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(outState==null)
            outState = new Bundle();
        outState.putParcelableArrayList("Entrees", entrees);
        outState.putParcelable("Event", event);
        // Add variable to outState here
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                backToEventOrga();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void backToEventOrga(){
        Intent intent = new Intent(this, EventOrgaActivity.class);
        intent.putExtra("Event", event);
        startActivity(intent);
    }

    public void goToHome(boolean addTobackStack){
        HomeAccesFragment frag = new HomeAccesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        if (addTobackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void goToManualAccess(){
        ManualAccesFragment frag = new ManualAccesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void refresh(final EntreeArrayAdapter adapter){
        final ProgressDialog wait = ProgressDialog.show(this,"","Chargement des entrées...",true,false);

        /* Récupération des IDs des billets de l'event */
        ArrayList<String> idBillets = new ArrayList<String>();
        for(Billet billet : event.getBilletterie())
            idBillets.add(billet.getID());

        /* On récupère tous les achats de ces billets */
        StackMobQuery query = new StackMobQuery().fieldIsIn("produit",idBillets);
        Achat.query(Achat.class, query, StackMobOptions.depthOf(2), new StackMobQueryCallback<Achat>() {
            @Override
            public void success(List<Achat> result) {
                entrees.clear();
                entrees.addAll(result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(adapter != null)
                            adapter.notifyDataSetChanged();
                        wait.dismiss();
                        Toast.makeText(AccesActivity.this,"Liste des entrées mise à jour.",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void failure(StackMobException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wait.dismiss();
                    }
                });
                Log.d("TOUTAFAIT", "erreur load entrées" + e.getMessage());
            }
        });

    };

    public Event getEvent(){
        return event;
    }

    public ArrayList<Achat> getEntrees(){
        return entrees;
    }
}
