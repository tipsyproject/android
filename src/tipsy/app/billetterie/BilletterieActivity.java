package tipsy.app.billetterie;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.UserActivity;
import tipsy.app.orga.HomeOrgaFragment;
import tipsy.app.orga.MenuOrga;
import tipsy.commun.Billetterie;
import tipsy.commun.Event;
import tipsy.commun.Organisateur;

/**
 * Created by valoo on 27/12/13.
 */
public class BilletterieActivity extends FragmentActivity implements BilletterieListener{

    private Billetterie billetterie = new Billetterie();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_billetterie);
        super.onCreate(savedInstanceState);

        // On récupère la billetterie de l'event courant
        Bundle bundle = getIntent().getExtras();
        billetterie.setID(bundle.getString("BILLETTERIE_ID"));
        billetterie.fetch(new StackMobModelCallback() {
            @Override
            public void success() {
                showListBillets();
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT", "Erreur billetterie:"+e.getMessage());
                Toast.makeText(BilletterieActivity.this, "Erreur Billetterie", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showListBillets(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new ListBilletsFragment(billetterie));
        ft.addToBackStack(null);
        ft.commit();
    }
}
