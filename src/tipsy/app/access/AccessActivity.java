package tipsy.app.access;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.app.R;
import tipsy.commun.Event;

/**
 * Created by vquefele on 20/01/14.
 */
public class AccessActivity extends FragmentActivity implements AccessListener{

    private Event event;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_access);
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Contrôle d'accès");

        if (savedInstanceState == null) {
            event = getIntent().getParcelableExtra("Event");

            final ProgressDialog wait = ProgressDialog.show(this,"","Mode Contrôle d'accès...",true,false);

            /* Requete Stackmob pour récupérer la billetterie */
            event.fetch(StackMobOptions.depthOf(1), new StackMobModelCallback() {
                @Override
                public void success() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wait.dismiss();
                        }
                    });
                    goToHome(false);
                }

                @Override
                public void failure(StackMobException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wait.dismiss();
                        }
                    });
                    NavUtils.navigateUpFromSameTask(AccessActivity.this);
                }
            });
        }
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

    public void goToHome(boolean addTobackStack){
        HomeAccessFragment frag = new HomeAccessFragment();//HomeAccessFragment.init(event);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        if (addTobackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void goToManualAccess(Event e){
        ManualAccessFragment frag = new ManualAccessFragment();//ManualAccessFragment.init(event);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void refresh(Event e){};

    public Event getEvent(){
        return event;
    }
}
