package tipsy.app.orga;

import android.content.Intent;
import android.os.Bundle;
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
        Log.d("TOUTAFAIT", "type 4:"+Integer.toString(orga.getUser().getType()));

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
        Event e = orga.creerEvent("Mon événement");
        ft.replace(R.id.content_frame, new EditEventFragment(e));
        ft.commit();
    }

    public void onEventEdited(){
        Log.d("TOUTAFAIT", "type 5:"+Integer.toString(orga.getUser().getType()));
        orga.save(StackMobOptions.depthOf(1), new StackMobModelCallback() {
            @Override
            public void success() {
                Log.d("TOUTAFAIT", "Event saved");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new HomeOrgaFragment());
                ft.commit();
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
