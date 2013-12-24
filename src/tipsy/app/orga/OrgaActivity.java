package tipsy.app.orga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.app.HelpActivity;
import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.UserActivity;
import tipsy.commun.Event;
import tipsy.commun.Organisateur;

/**
 * Created by Valoo on 05/12/13.
 */
public class OrgaActivity extends UserActivity implements OrgaListener{

    private TipsyApp app;
    private Organisateur orga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_user);
        super.onCreate(savedInstanceState);
        this.menu = new MenuOrga(this);
        menu.initAdapter(new UserActivity.DrawerItemClickListener());

        app = (TipsyApp) getApplication();
        orga = app.getOrga();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new HomeOrgaFragment());
        ft.addToBackStack(null);
        ft.commit();
    }


    protected void selectItem(int position) {
        Fragment fragment = null;

        switch(position){
            case MenuOrga.ACCUEIL:
            fragment = new HomeOrgaFragment();
                break;
            case MenuOrga.MON_COMPTE:
                fragment = new AccountOrgaFragment();
                break;
            case MenuOrga.EVENEMENTS:
                fragment = new EventsOrgaFragment();
                break;
            case MenuOrga.AIDE:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case MenuOrga.DECONNEXION:
                app.logout(this);
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();

            // update selected item and title, then close the drawer
            this.menu.getDrawerList().setItemChecked(position, true);
            this.menu.getDrawerList().setSelection(position);
            setTitle(this.menu.getTitres_menu()[position]);
            this.menu.getDrawerLayout().closeDrawer(this.menu.getDrawerList());
        } else {
            // error in creating fragment
            Log.e("OrgaActivity", "Error in creating fragment");
        }

    }


    // IMPLEMENTATIONS DES LISTENERS DU MODULE ORGANISATEUR

    // Clique sur le bouton "Créer un événement"
    public void onEventNew(){
        Event e = orga.creerEvent("");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new EditEventFragment(e))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    // Création/Modification d'un événement terminée
    public void onEventEdited(){
        orga.save(StackMobOptions.depthOf(1), new StackMobModelCallback() {
            @Override
            public void success() {
                Log.d("TOUTAFAIT", "Event saved");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, new HomeOrgaFragment())
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                Toast.makeText(OrgaActivity.this, "Evénement enregistré", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT", "Erreur sauvegarde event:" + e.getMessage());
                Toast.makeText(OrgaActivity.this, "Erreur sauvegarde event:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
