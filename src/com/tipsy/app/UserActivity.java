package com.tipsy.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Alexandre on 18/12/13.
 */
public abstract class UserActivity extends FragmentActivity implements MenuListener {
    protected static MenuUser menu;
    protected TipsyApp app;
    public final static int ACCUEIL = 0;
    public final static int MON_COMPTE = 1;
    public final static int EVENEMENTS = 2;
    public final static int AIDE = 3;
    public final static int DECONNEXION = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                app.hideKeyboard(UserActivity.this);
                return false;
            }
        });
    }

    protected void onStart() {
        super.onStart();
        setTitle(this.menu.getTitre());
    }

    // Called whenever we call invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (this.menu.getDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        } else {
            return false;
        }
    }

    protected void selectItem(int position) {

    }

    // The click listener for ListView in the navigation drawer
    public class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    @Override
    public void setTitle(CharSequence title) {
        menu.setTitre(title);
        getActionBar().setTitle(title);
    }

    public void setMenuTitle(int titleId) {
        setTitle(this.menu.getmTitles()[titleId]);
        this.menu.getDrawerList().setItemChecked(titleId, true);
    }

    public void setMenuTitle(String title) {
        setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        this.menu.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        this.menu.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    public static MenuUser getMenu() {
        return menu;
    }
}
