package tipsy.app.orga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.app.HelpActivity;
import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.UserActivity;
import tipsy.app.orga.event.edit.EditEventActivity;
import tipsy.app.orga.event.EventOrgaActivity;
import tipsy.commun.Event;
import tipsy.commun.Organisateur;

/**
 * Created by Valoo on 05/12/13.
 */
public class OrgaActivity extends UserActivity implements OrgaListener {

    private TipsyApp app;
    private Organisateur orga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_user);
        super.onCreate(savedInstanceState);
        this.menu = new MenuOrga(this);
        menu.initAdapter(new UserActivity.DrawerItemClickListener());
        menu.getDrawerList().setItemChecked(MenuOrga.ACCUEIL, true);

        app = (TipsyApp) getApplication();
        orga = app.getOrga();

        if(getIntent().hasExtra("EVENT_HOME")){
            Event e = getIntent().getParcelableExtra("EVENT_HOME");
            goToEvent(e);
        }else if (savedInstanceState == null) {
            tableauDeBord(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_orga, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    protected void selectItem(int position) {
        // update selected item and title, then close the drawer
        this.menu.getDrawerList().setItemChecked(position, true);
        this.menu.getDrawerList().setSelection(position);
        this.menu.getDrawerLayout().closeDrawer(this.menu.getDrawerList());
        switch (position) {
            case MenuOrga.ACCUEIL:
                tableauDeBord(true);
                break;
            case MenuOrga.MON_COMPTE:
                account();
                break;
            case MenuOrga.EVENEMENTS:
                events();
                break;
            case MenuOrga.AIDE:
                Intent intent = new Intent(this, HelpActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("Connected", true);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case MenuOrga.DECONNEXION:
                app.logout(this);
                break;
        }

    }


    // IMPLEMENTATIONS DES LISTENERS DU MODULE ORGANISATEUR

    // Clique sur le bouton "Créer un événement"
    public void goToEvent(Event e) {
        Intent intent = new Intent(this, EventOrgaActivity.class);
        intent.putExtra("Event", e);
        startActivity(intent);
    }

    // Clique sur le bouton "Créer un événement" ou Modifications de l'event
    public void goToNewEvent() {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("NEW_EVENT",true);
        startActivity(intent);
    }

    public void tableauDeBord(boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new HomeOrgaFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void account() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.content, new AccountOrgaFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }


    public void events() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.content, new EventsOrgaFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }


}
