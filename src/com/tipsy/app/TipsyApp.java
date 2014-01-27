package com.tipsy.app;

import android.app.Activity;
import android.app.Application;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Bracelet;
import com.tipsy.lib.Depot;
import com.tipsy.lib.Event;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Prefs;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.TipsyUser;

/**
 * Created by valoo on 18/12/13.
 */
public class TipsyApp extends Application {

    static final String TAG = "Cestpasfaux";

    @Override
    public void onCreate() {
        ParseUser.registerSubclass(Achat.class);
        ParseUser.registerSubclass(Bracelet.class);
        ParseUser.registerSubclass(Depot.class);
        ParseUser.registerSubclass(Event.class);
        ParseUser.registerSubclass(Participant.class);
        ParseUser.registerSubclass(Ticket.class);
        ParseUser.registerSubclass(TipsyUser.class);
        Parse.initialize(this, "pcMVnVGR9jVH5yjuGbtfZYsdUfrQadWMiaHvjkYH", "GDauifCqWNi5F3ocuvgn9wYqvn63OeXNHJDw1f7S");
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
        ParseTwitterUtils.initialize("3RUMZQ8xHisk5fHMUAcaIg", "eSYhVIAAwxirYCeJgu2Xu7nuw2oTZLJEIHNBAHjwE");

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
