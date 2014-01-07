package tipsy.app.billetterie;

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
import tipsy.commun.Event;

/**
 * Created by valoo on 27/12/13.
 */
public class BilletterieActivity extends FragmentActivity implements BilletterieListener {

    private Event event = new Event();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_billetterie);
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        // On récupère la billetterie de l'event courant
        Bundle bundle = getIntent().getExtras();
        event.setID(bundle.getString("EVENT_ID"));

        /* Requete Stackmob */
        event.fetch(StackMobOptions.depthOf(2),new StackMobModelCallback() {
            @Override
            public void success() {
                showListBillets(false);
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT", "Erreur billetterie:" + e.getMessage());
                Toast.makeText(BilletterieActivity.this, "Erreur Billetterie", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // IMPLEMENTATION DES FONCTIONS DE l'INTERFACE BilletterieListener

    public void showListBillets(boolean addTobackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new ListBilletsFragment(event));
        if (addTobackStack)
            ft.addToBackStack(null);
        ft.commit();
    }
}
