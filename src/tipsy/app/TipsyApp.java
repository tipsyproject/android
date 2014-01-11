package tipsy.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.plus.PlusClient;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.commun.Membre;
import tipsy.commun.Organisateur;
import tipsy.commun.Prefs;
import tipsy.commun.User;
import tipsy.commun.commerce.Panier;
import tipsy.commun.commerce.Transaction;
import tipsy.commun.commerce.Wallet;

/**
 * Created by valoo on 18/12/13.
 */
public class TipsyApp extends Application {
    private Membre membre;
    private Panier panier = new Panier();
    private Organisateur orga;
    private Wallet<Transaction> wallet;

    public TipsyApp() {
        membre = null;
        orga = null;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
        this.wallet = new Wallet<Transaction>(membre.getUser());
    }

    public Organisateur getOrga() {
        return orga;
    }

    public void setOrga(Organisateur orga) {
        this.orga = orga;
    }

    public Panier getPanier() {
        return this.panier;
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

    public Wallet getWallet() {
        return wallet;
    }

    public void logout(Activity act) {
        final Activity a = act;
        final User user;
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
                wallet = null;
                startActivity(new Intent(a, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("REMEMBER", "Logout failed");
            }
        });
    }
}
