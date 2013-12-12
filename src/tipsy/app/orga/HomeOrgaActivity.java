package tipsy.app.orga;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.stackmob.android.sdk.common.StackMobAndroid;

import tipsy.app.HelpMainActivity;
import tipsy.app.R;

/**
 * Created by Valoo on 05/12/13.
 */
public class HomeOrgaActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence titre;
    private String[] titres_menu;

    private ImageButton buttonCreerEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StackMobAndroid.init(getApplicationContext(), 0, "eeedff37-f59d-408a-9279-27cd8fe7062e");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orga_home);

        buttonCreerEvent = (ImageButton) findViewById(R.id.button_creer_event);

        buttonCreerEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(HomeOrgaActivity.this, EditEventActivity.class));
            }
        });
        titre = mDrawerTitle = getTitle();
        titres_menu = getResources().getStringArray(R.array.menu_organisateur);
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
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(titre);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
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
        if (position == 2) {
            startActivity(new Intent(HomeOrgaActivity.this, HelpMainActivity.class));
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
    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    /*public static class EventFragment extends Fragment {

        public EventFragment() {
            // Empty constructor required for fragment subclasses
        }
    }*/
}
