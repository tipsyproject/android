package tipsy.app;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.List;

import tipsy.commun.Prefs;
import tipsy.commun.User;

/**
 * Created by Alexandre on 15/12/13.
 */
public class HomeActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private SharedPreferences prefs;

    private CharSequence mDrawerTitle;
    private CharSequence titre;
    private String[] titres_menu;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.right_to_left, R.animator.activity_close_scale);
        setContentView(R.layout.act_home);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        titre = mDrawerTitle = getTitle();
        //StackMobQuery query = new StackMobQuery("user")
        //        .fieldIsEqualTo("username", Prefs.USERNAME).and().field(new StackMobQueryField("type"));

        if (LoginActivity.type == 2) {
            titres_menu = getResources().getStringArray(R.array.menu_organisateur);
        } else if (LoginActivity.type == 1) {
            titres_menu = getResources().getStringArray(R.array.menu_participant);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_Layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, titres_menu));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(titre);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(titre);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(HomeActivity.this);
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.animator.activity_open_close, R.animator.left_to_right);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        if (LoginActivity.type == 1) {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
            menu.findItem(R.id.search).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (LoginActivity.type == 1) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_user, menu);
            MenuItem searchItem = menu.findItem(R.id.search);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            mSearchView = (SearchView) searchItem.getActionView();
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return false;
        }
    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        /*Fragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt(EventFragment.ARG_EVENT_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();*/

        // update selected item and title, then close the drawer

        if (position == 2 && LoginActivity.type == 2)
            startActivity(new Intent(HomeActivity.this, HelpActivity.class));
        else if (LoginActivity.type == 1 && position == 3 || LoginActivity.type == 2 && position == 2)
            startActivity(new Intent(HomeActivity.this, HelpActivity.class));
        else if (LoginActivity.type == 2 && position == 3 || LoginActivity.type == 1 && position == 4) {
            User.getLoggedInUser(User.class, new StackMobQueryCallback<User>() {
                @Override
                public void success(List<User> list) {
                    User user = list.get(0);
                    user.logout(new StackMobModelCallback() {
                        @Override
                        public void success() {
                            Log.d("REMEMBER", "Logout");
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            prefs.edit()
                                    .putString(Prefs.USERNAME, "")
                                    .putString(Prefs.PASSWORD, "")
                                    .commit();
                        }

                        @Override
                        public void failure(StackMobException e) {
                            Log.d("REMEMBER", "Logout failed");
                        }
                    });
                }

                @Override
                public void failure(StackMobException e) {
                }
            });

        } else {
            mDrawerList.setItemChecked(position, true);
            setTitle(titres_menu[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        titre = title;
        getActionBar().setTitle(titre);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
