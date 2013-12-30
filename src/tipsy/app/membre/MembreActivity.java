package tipsy.app.membre;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;

import tipsy.app.HelpActivity;
import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.UserActivity;
import tipsy.commun.Membre;

/**
 * Created by tech on 05/12/13.
 */
public class MembreActivity extends UserActivity implements MembreListener{

    private TipsyApp app;
    private SearchView SearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_user);
        super.onCreate(savedInstanceState);
        this.menu = new MenuMembre(this);
        menu.initAdapter(new UserActivity.DrawerItemClickListener());

        app = (TipsyApp) getApplication();

        selectItem(MenuMembre.ACCUEIL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        menu.findItem(R.id.search).setVisible(!this.menu.isDrawerOpen());
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView = (SearchView) searchItem.getActionView();
        SearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    protected void selectItem(int position) {
        // update selected item and title, then close the drawer
        this.menu.getDrawerList().setItemChecked(position, true);
        this.menu.getDrawerList().setSelection(position);
        this.menu.getDrawerLayout().closeDrawer(this.menu.getDrawerList());
        switch(position){
            case MenuMembre.ACCUEIL:
                goToTableauDeBord();
                break;
            case MenuMembre.MON_COMPTE:
                goToAccount();
                break;
            case MenuMembre.SOLDE:
                goToSolde();
                break;
            case MenuMembre.EVENEMENTS:
                goToEvents();
                break;
            case MenuMembre.AIDE:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case MenuMembre.DECONNEXION:
                app.logout(this);
                break;
        }

    }

    // IMPLEMENTATION DU LISTENER MEMBRE
    public void goToAccount(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new AccountMembreFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void goToTableauDeBord(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new HomeMembreFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void goToSolde(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new SoldeMembreFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void goToEvents(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new EventMembreFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public Membre getMembre(){
        return app.getMembre();
    }
}
