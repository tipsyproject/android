package tipsy.app.orga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import tipsy.app.HelpActivity;
import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.UserActivity;

/**
 * Created by Valoo on 05/12/13.
 */
public class OrgaActivity extends UserActivity {

    private TipsyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_user_menu);
        super.onCreate(savedInstanceState);
        this.menu = new MenuOrga(this);
        menu.initAdapter(new UserActivity.DrawerItemClickListener());

        app = (TipsyApp) getApplication();

        Log.d("TOUTAFAIT", app.getOrga().getEmail());
    }


    protected void selectItem(int position) {
        Fragment fragment = null;

        if (position == MenuOrga.ACCUEIL)
            fragment = new HomeOrgaFragment();
        else if (position == MenuOrga.MON_COMPTE)
            fragment = new AccountOrgaFragment();
        else if (position == MenuOrga.EVENEMENTS)
            fragment = new EventOrgaFragment();
        else if (position == MenuOrga.AIDE)
            startActivity(new Intent(this, HelpActivity.class));
        else if (position == MenuOrga.DECONNEXION) {
            app.logout(this);
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

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

}
