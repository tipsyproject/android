package tipsy.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.commun.Membre;
import tipsy.commun.Organisateur;
import tipsy.commun.Prefs;
import tipsy.commun.User;

/**
 * Created by valoo on 18/12/13.
 */
public class TipsyApp extends Application {
    private Membre membre;
    private Organisateur orga;

    public TipsyApp() {
        membre = null;
        orga = null;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public Organisateur getOrga() {
        return orga;
    }

    public void setOrga(Organisateur orga) {
        this.orga = orga;
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

    public void logout(Activity act) {
        final Activity a = act;
        User user;
        if (membre != null)
            user = membre.getUser();
        else if (orga != null)
            user = orga.getUser();
        else {
            Log.d("REMEMBER", "Logout failed: user inconnu");
            return;
        }
        user.logout(new StackMobModelCallback() {
            @Override
            public void success() {
                User.forgetMe(a);
                membre = null;
                orga = null;
                startActivity(new Intent(a, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("REMEMBER", "Logout failed");
            }
        });
    }
}
