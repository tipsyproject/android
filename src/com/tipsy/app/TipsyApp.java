package com.tipsy.app;

import android.app.Activity;
import android.app.Application;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import com.parse.Parse;
import com.parse.ParseUser;
import com.tipsy.lib.Prefs;
import com.tipsy.lib.TipsyUser;

/**
 * Created by valoo on 18/12/13.
 */
public class TipsyApp extends Application {

    @Override
    public void onCreate() {
        ParseUser.registerSubclass(TipsyUser.class);
        Parse.initialize(this, "pcMVnVGR9jVH5yjuGbtfZYsdUfrQadWMiaHvjkYH", "GDauifCqWNi5F3ocuvgn9wYqvn63OeXNHJDw1f7S");
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
