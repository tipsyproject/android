package tipsy.app.membre;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.List;

import tipsy.app.HelpActivity;
import tipsy.app.LoginActivity;
import tipsy.app.R;
import tipsy.app.UserActivity;
import tipsy.commun.Prefs;
import tipsy.commun.User;

/**
 * Created by tech on 05/12/13.
 */
public class MembreActivity extends UserActivity {

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_user_menu);
        super.onCreate(savedInstanceState);
        this.menu = new MenuMembre(this);
        menu.initAdapter(new UserActivity.DrawerItemClickListener());
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
            User.getLoggedInUser(User.class, new StackMobQueryCallback<User>() {
                @Override
                public void success(List<User> list) {
                    User user = list.get(0);
                    user.logout(new StackMobModelCallback() {
                        @Override
                        public void success() {
                            Log.d("REMEMBER", "Logout");
                            startActivity(new Intent(MembreActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            PreferenceManager.getDefaultSharedPreferences(MembreActivity.this).edit()
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
            this.menu.getDrawerList().setItemChecked(position, true);
            setTitle(this.menu.getTitres_menu()[position]);
            this.menu.getDrawerLayout().closeDrawer(this.menu.getDrawerList());
        }

    }
}
