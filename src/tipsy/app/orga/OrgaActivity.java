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
import tipsy.app.billetterie.BilletterieActivity;
import tipsy.app.membre.EventMembreFragment;
import tipsy.commun.Billetterie;
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
        goToTableauDeBord();

    }


    protected void selectItem(int position) {
        // update selected item and title, then close the drawer
        this.menu.getDrawerList().setItemChecked(position, true);
        this.menu.getDrawerList().setSelection(position);
        this.menu.getDrawerLayout().closeDrawer(this.menu.getDrawerList());
        switch(position){
            case MenuOrga.ACCUEIL:
                goToTableauDeBord();
                break;
            case MenuOrga.MON_COMPTE:
                goToAccount();
                break;
            case MenuOrga.EVENEMENTS:
                goToEvents();
                break;
            case MenuOrga.AIDE:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case MenuOrga.DECONNEXION:
                app.logout(this);
                break;
        }

    }


    // IMPLEMENTATIONS DES LISTENERS DU MODULE ORGANISATEUR

    // clique sur le bouton de la Billetterie
    public void onBilletterieEdit(Event e){
        Intent intent = new Intent(this, BilletterieActivity.class);
        intent.putExtra("BILLETTERIE_ID",e.getBilletterie().getID());
        startActivity(intent);
    }

    // Clique sur le bouton "Créer un événement"
    public void onClickResumeEvent(Event e){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new EventHomeFragment(e))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    // Clique sur le bouton "Créer un événement"
    public void onEventEdit(Event e){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new EditEventFragment(e))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    // Création/Modification d'un événement terminée
    public void onEventEdited(){
        orga.save(StackMobOptions.depthOf(1),new StackMobModelCallback() {
            @Override
            public void success() {
                goToTableauDeBord();
            }

            @Override
            public void failure(StackMobException e) {
                Log.e("TOUTAFAIT", "erreur sauvegarde Event:"+e.getMessage());
            }
        });
    }

    public void goToTableauDeBord(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new HomeOrgaFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void goToAccount(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new AccountOrgaFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }



    public void goToEvents(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new EventsOrgaFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


}
