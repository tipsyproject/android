package tipsy.app.orga;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.List;

import tipsy.app.HelpActivity;
import tipsy.app.LoginActivity;
import tipsy.app.R;
import tipsy.commun.User;

/**
 * Created by Valoo on 05/12/13.
 */
public class HomeOrgaActivity extends OrgaActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence titre;
    private String[] titres_menu;

    private ImageButton buttonCreerEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_orga_home);

        buttonCreerEvent = (ImageButton) findViewById(R.id.button_creer_event);

        buttonCreerEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(HomeOrgaActivity.this, EditEventActivity.class));
            }
        });
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
            startActivity(new Intent(HomeOrgaActivity.this, HelpActivity.class));
        } else if (position == 3) {
            User.getLoggedInUser(User.class, new StackMobQueryCallback<User>() {
                @Override
                public void success(List<User> list) {
                    User user = list.get(0);
                    user.logout(new StackMobModelCallback() {
                        @Override
                        public void success() {
                            Log.d("REMEMBER", "Logout");
                            startActivity(new Intent(HomeOrgaActivity.this, LoginActivity.class));
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

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    /*public static class EventFragment extends Fragment {

        public EventFragment() {
            // Empty constructor required for fragment subclasses
        }
    }*/
}
