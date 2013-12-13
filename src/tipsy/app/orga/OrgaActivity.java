package tipsy.app.orga;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
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
import tipsy.app.HomeAnonymousActivity;
import tipsy.app.R;
import tipsy.app.membre.HomeMembreActivity;
import tipsy.commun.Organisateur;
import tipsy.commun.TypeUser;
import tipsy.commun.User;

/**
 * Created by Valoo on 05/12/13.
 */
abstract class OrgaActivity extends FragmentActivity {

    protected Organisateur orga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* mise à disposition de l'organisateur connecté */
        User.getLoggedInUser(User.class, new StackMobQueryCallback<User>() {
            @Override
            public void success(List<User> list) {
                User user = list.get(0);
            }
            @Override
            public void failure(StackMobException e) {

            }
        });
    }

}
