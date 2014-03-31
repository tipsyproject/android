package com.tipsy.app;

import android.app.Activity;
import android.app.Application;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Depot;
import com.tipsy.lib.Event;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.TipsyUser;
import com.tipsy.lib.Vestiaire;
import com.tipsy.lib.util.Prefs;
import com.tipsy.lib.util.QueryCallback;
import com.tipsy.lib.util.Wallet;

/**
 * Created by valoo on 18/12/13.
 */
public class TipsyApp extends Application {

    public static final String TAG = "Cestpasfaux";
    private Wallet wallet;

    @Override
    public void onCreate() {
        ParseObject.registerSubclass(Achat.class);
        ParseObject.registerSubclass(Depot.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Participant.class);
        ParseObject.registerSubclass(Ticket.class);
        ParseUser.registerSubclass(TipsyUser.class);
        ParseObject.registerSubclass(Vestiaire.class);
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


    public Wallet getWallet() {
        return wallet;
    }

    public void loadWallet(QueryCallback cb) {
        wallet = new Wallet(TipsyUser.getCurrentUser());
        wallet.load(cb);
    }
}
