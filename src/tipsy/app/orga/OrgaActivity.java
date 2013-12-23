package tipsy.app.orga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

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
        ft.replace(R.id.content_frame, new HomeOrgaFragment());
        ft.commit();
    }

    protected void selectItem(int position) {

        // Mise à jour du menu
        this.menu.getDrawerList().setItemChecked(position, true);
        setTitle(this.menu.getTitres_menu()[position]);
        this.menu.getDrawerLayout().closeDrawer(this.menu.getDrawerList());

        // Action à réaliser en fonction de l'onglet
        switch(position){
            case MenuOrga.ACCUEIL:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new HomeOrgaFragment());
                ft.commit();
                break;
            case MenuOrga.AIDE:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case MenuOrga.DECONNEXION:
                app.logout(this);
            break;
            default:
                break;

        }

    }


    // IMPLEMENTATIONS DES LISTENERS DE LA PARTIE ORGANISATEUR

    public void onEventNew(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Event e = orga.creerEvent("Mon événément");
        ft.replace(R.id.content_frame, new EditEventFragment(e));
        ft.commit();
    }

    public void onEventSaved(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new HomeOrgaFragment());
        ft.commit();
    }

}
