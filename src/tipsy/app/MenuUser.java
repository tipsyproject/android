package tipsy.app;

import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexandre on 15/12/13.
 */
public abstract class MenuUser {
    protected static int ACCUEIL = 0;
    protected DrawerLayout drawerLayout;
    protected ListView drawerList;
    protected ActionBarDrawerToggle drawerToggle;
    protected Activity activity;
    protected CharSequence drawerTitle;
    protected CharSequence titre;
    final private String ICON = "icon";
    final private String TITLE = "title";
    protected List<HashMap<String,String>> mList ;
    protected SimpleAdapter mAdapter;
    // Array of integers points to images stored in /res/drawable-ldpi/
    protected int[] mIcons;
    // Array of strings to initial counts
    protected String[] mTitles;

    public MenuUser(Activity a) {
        activity = a;
        activity.overridePendingTransition(R.animator.right_to_left, R.animator.activity_close_scale);

        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_Layout);
        drawerList = (ListView) activity.findViewById(R.id.left_drawer);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        activity.getActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = new ActionBarDrawerToggle(
                activity,                  // host Activity
                drawerLayout,         // DrawerLayout object
                R.drawable.ic_drawer,  // nav drawer image to replace 'Up' caret
                R.string.drawer_open,  // "open drawer" description for accessibility
                R.string.drawer_close  // "close drawer" description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                activity.getActionBar().setTitle(titre);
                activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                activity.getActionBar().setTitle(titre);
                activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    public void initAdapter(UserActivity.DrawerItemClickListener listener) {
        // set up the drawer's list view with items and click listener
        //getDrawerList().setAdapter(new ArrayAdapter<String>(activity, R.layout.frag_drawer_list_item, titres_menu));

        // Keys used in Hashmap
        String[] from = { ICON,TITLE };
        // Ids of views in listview_layout
        int[] to = { R.id.item_icon , R.id.item_title};
        mList = new ArrayList<HashMap<String,String>>();
        for(int i=0;i<5;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put(ICON, Integer.toString(mIcons[i]));
            hm.put(TITLE, mTitles[i]);
            mList.add(hm);
        }
        mAdapter = new SimpleAdapter(activity, mList, R.layout.row_menu_user, from, to);
        getDrawerList().setAdapter(mAdapter);
        getDrawerList().setOnItemClickListener(listener);
    }

    public boolean isDrawerOpen() {
        return getDrawerLayout().isDrawerOpen(getDrawerList());
    }

    public String[] getmTitles() {
        return mTitles;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public ListView getDrawerList() {
        return drawerList;
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    public CharSequence getTitre() {
        return titre;
    }

    public void setDrawerTitle(CharSequence drawerTitle) {
        this.drawerTitle = drawerTitle;
    }

    public CharSequence getDrawerTitle() {
        return drawerTitle;
    }

    public void setTitre(CharSequence titre) {
        this.titre = titre;
    }
}
