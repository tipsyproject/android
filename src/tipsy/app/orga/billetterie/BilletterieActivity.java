package tipsy.app.orga.billetterie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;


import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.orga.event.EventOrgaActivity;
import tipsy.commun.Event;

/**
 * Created by valoo on 27/12/13.
 */
public class BilletterieActivity extends FragmentActivity implements BilletterieListener {

    private Event event;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_billetterie);
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Billetterie");


        TipsyApp app = (TipsyApp) getApplication();
        index = getIntent().getIntExtra("EVENT_INDEX",-1);
        event = app.getOrga().getEvents().get(index);

        if(savedInstanceState == null){
            final ProgressDialog wait = ProgressDialog.show(this,"","Mode Billetterie...",true,false);
            event.fetch(StackMobOptions.depthOf(1), new StackMobModelCallback() {
                @Override
                public void success() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wait.dismiss();
                        }
                    });
                    showListeBillets(false);
                }

                @Override
                public void failure(StackMobException e) {
                    Log.d("TOUTAFAIT", "Erreur billetterie:" + e.getMessage());
                    BilletterieActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wait.dismiss();
                            Toast.makeText(BilletterieActivity.this, "Erreur Billetterie", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
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

    // IMPLEMENTATION DES FONCTIONS DE l'INTERFACE BilletterieListener

    public void showListeBillets(boolean addTobackStack) {
        ListeBilletsFragment frag = ListeBilletsFragment.init(event);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        if (addTobackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void showListeVentes(){
        ListeVentesFragment frag = ListeVentesFragment.init(event);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        ft.addToBackStack(null);
        ft.commit();
    }
}
