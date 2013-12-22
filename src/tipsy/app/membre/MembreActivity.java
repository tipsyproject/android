package tipsy.app.membre;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import tipsy.app.HelpActivity;
import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.UserActivity;

/**
 * Created by tech on 05/12/13.
 */
public class MembreActivity extends UserActivity {

    private TipsyApp app;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_user);
        super.onCreate(savedInstanceState);
        this.menu = new MenuMembre(this);
        menu.initAdapter(new UserActivity.DrawerItemClickListener());

        app = (TipsyApp) getApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        menu.findItem(R.id.search).setVisible(!this.menu.isDrawerOpen());
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    protected void selectItem(int position) {

        // update selected item and title, then close the drawer

        if (position == MenuMembre.AIDE)
            startActivity(new Intent(this, HelpActivity.class));
        else if (position == MenuMembre.DECONNEXION) {
            app.logout(this);
        } else {
            this.menu.getDrawerList().setItemChecked(position, true);
            setTitle(this.menu.getTitres_menu()[position]);
            this.menu.getDrawerLayout().closeDrawer(this.menu.getDrawerList());
        }

    }
}
