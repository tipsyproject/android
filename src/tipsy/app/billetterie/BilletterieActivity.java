package tipsy.app.billetterie;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;


import tipsy.app.R;
import tipsy.app.orga.OrgaActivity;
import tipsy.commun.Event;

/**
 * Created by valoo on 27/12/13.
 */
public class BilletterieActivity extends FragmentActivity implements BilletterieListener {

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_billetterie);
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        event = getIntent().getParcelableExtra("Event");

        if (savedInstanceState == null) {
            /* Requete Stackmob pour récupérer la billetterie */

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
                OrgaActivity.backToEventHome(this, event);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
