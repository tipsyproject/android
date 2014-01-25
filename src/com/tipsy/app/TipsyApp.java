package com.tipsy.app;

import android.app.Activity;
import android.app.Application;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.tipsy.lib.Bracelet;
import com.tipsy.lib.Event;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Prefs;
import com.tipsy.lib.TipsyUser;
import com.tipsy.lib.billetterie.Billet;
import com.tipsy.lib.commerce.Achat;
import com.tipsy.lib.commerce.Depot;
import com.tipsy.lib.commerce.Produit;

/**
 * Created by valoo on 18/12/13.
 */
public class TipsyApp extends Application {

    static final String TAG = "Cestpasfaux";

    @Override
    public void onCreate() {
        ParseUser.registerSubclass(Achat.class);
        ParseUser.registerSubclass(Billet.class);
        ParseUser.registerSubclass(Bracelet.class);
        ParseUser.registerSubclass(Depot.class);
        ParseUser.registerSubclass(Event.class);
        ParseUser.registerSubclass(Participant.class);
        ParseUser.registerSubclass(Produit.class);
        ParseUser.registerSubclass(TipsyUser.class);
        Parse.initialize(this, "pcMVnVGR9jVH5yjuGbtfZYsdUfrQadWMiaHvjkYH", "GDauifCqWNi5F3ocuvgn9wYqvn63OeXNHJDw1f7S");
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

    }

    public boolean skipHelp(Activity a) {
        // retourne 'false' par défaut
        return PreferenceManager.getDefaultSharedPreferences(a)
                .getBoolean(Prefs.SKIP_HELP, false);
    }

    public static void setSkipHelp(Activity a, boolean skip) {
        PreferenceManager.getDefaultSharedPreferences(a)
                .edit().putBoolean(Prefs.SKIP_HELP, skip).commit();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
