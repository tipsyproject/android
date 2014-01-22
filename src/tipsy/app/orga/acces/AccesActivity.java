package tipsy.app.orga.acces;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.orga.OrgaActivity;
import tipsy.app.orga.event.EventOrgaActivity;
import tipsy.commun.Bracelet;
import tipsy.commun.Event;
import tipsy.commun.billetterie.Billet;
import tipsy.commun.billetterie.EntreeArrayAdapter;
import tipsy.commun.commerce.Achat;

/**
 * Created by vquefele on 20/01/14.
 */
public class AccesActivity extends FragmentActivity implements AccesListener {

    private Event event;
    private int index;
    private ArrayList<Achat> entrees = new ArrayList<Achat>();
    NfcAdapter adapter;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_access);
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Contrôle d'accès");
        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        TipsyApp app = (TipsyApp) getApplication();
        index = getIntent().getIntExtra("EVENT_INDEX",-1);
        event = app.getOrga().getEvents().get(index);
        if(savedInstanceState != null && savedInstanceState.containsKey("Entrees")){
            entrees =  savedInstanceState.getParcelableArrayList("Entrees");
        }else{
            refresh(null);
            goToHome(false);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(outState==null)
            outState = new Bundle();
        outState.putParcelableArrayList("Entrees", entrees);
        // Add variable to outState here
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    public void onPause(){
        adapter.disableForegroundDispatch(this);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent){

        final ProgressDialog wait = ProgressDialog.show(this,"","Vérification en cours...",true,false);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagID = Bracelet.bytesToHex(tag.getId());

        Bracelet.query(Bracelet.class, new StackMobQuery().fieldIsEqualTo("tagid", tagID), StackMobOptions.depthOf(1),
                new StackMobQueryCallback<Bracelet>() {
                    @Override
                    public void success(List<Bracelet> bracelets) {
                        Log.d("TOUTAFAIT", "success bracelet");
                        /* Bracelet inconnu */
                        if (bracelets.isEmpty()) {
                            Log.d("TOUTAFAIT", "bracelet inconnu");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AccesActivity.this, "Bracelet inconnu !", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            final Bracelet bracelet = bracelets.get(0);
                            Log.d("TOUTAFAIT", "recherche entrée");

                            Log.d("TOUTAFAIT", "entrees size: " + Integer.toString(entrees.size()));
                            Iterator it = entrees.iterator();
                            Achat entree;
                            boolean found = false;
                            while(it.hasNext() && !found) {
                                entree = (Achat) it.next();
                                if(bracelet.isMembre() && entree.getParticipant().isMembre())
                                    found = bracelet.getMembre().getID().equals(entree.getParticipant().getMembre().getID());
                                else if(bracelet.isParticipant())
                                    found = bracelet.getParticipant().getID().equals(entree.getParticipant().getID());
                            }

                            if(found){
                                Log.d("TOUTAFAIT","found");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wait.dismiss();
                                        Toast.makeText(AccesActivity.this, "Entrée OK", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                Log.d("TOUTAFAIT","not found");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wait.dismiss();
                                        Toast.makeText(AccesActivity.this, "Entrée non autorisée", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void failure(StackMobException e) {
                        Log.d("TOUTAFAIT", "erreur query bracelet:" + e.getMessage());
                    }
                });


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
        intent.putExtra("EVENT_INDEX", index);
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
