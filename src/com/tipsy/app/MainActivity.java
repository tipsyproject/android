package com.tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.parse.Parse;
import com.tipsy.app.membre.MembreActivity;
import com.tipsy.app.orga.OrgaActivity;
import com.tipsy.lib.Prefs;
import com.tipsy.lib.TipsyUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vquefele on 23/01/14.
 */
public class MainActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Parse.initialize(this, "pcMVnVGR9jVH5yjuGbtfZYsdUfrQadWMiaHvjkYH", "GDauifCqWNi5F3ocuvgn9wYqvn63OeXNHJDw1f7S");

        setContentView(R.layout.act_loading);

        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean(Prefs.CONNECTED, false);

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.tipsy.app", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }

        TipsyApp app = (TipsyApp) getApplication();
        // Affichage de l'aide si elle n'a encore jamais été passée.
        // Sinon, on essaye de reconnecter automatiquement l'utilisateur
        if (!app.skipHelp(this)) startActivity(new Intent(this, HelpActivity.class));
        else {
            TipsyUser user = TipsyUser.getCurrentUser();
            if (user == null)
                startActivity(new Intent(this, LoginActivity.class));
            else if (user.getType() == TipsyUser.MEMBRE)
                startActivity(new Intent(this, MembreActivity.class));
            else // Orga
                startActivity(new Intent(this, OrgaActivity.class));
        }

    }

}
