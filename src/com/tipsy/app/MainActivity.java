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
import com.tipsy.app.help.HelpActivity;
import com.tipsy.app.login.LoginActivity;
import com.tipsy.app.orga.OrgaActivity;
import com.tipsy.lib.TipsyUser;
import com.tipsy.lib.util.Prefs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vquefele on 23/01/14.
 */
public class MainActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Parse.initialize(this, "pcMVnVGR9jVH5yjuGbtfZYsdUfrQadWMiaHvjkYH", "GDauifCqWNi5F3ocuvgn9wYqvn63OeXNHJDw1f7S");

        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_loading);

        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean(Prefs.CONNECTED, false);


        TipsyApp app = (TipsyApp) getApplication();
        /*
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
        }*/

        // Affichage de l'aide si elle n'a encore jamais été passée.
        // Sinon, on essaye de reconnecter automatiquement l'utilisateur
        if (!app.skipHelp(this)) startActivity(new Intent(this, HelpActivity.class));
        else {
            TipsyUser user = TipsyUser.getCurrentUser();
            if (user == null)
                startActivity(new Intent(this, LoginActivity.class));
            else if (user.getType() == TipsyUser.ORGA)// Orga
                startActivity(new Intent(this, OrgaActivity.class));
            else
                startActivity(new Intent(this, LoginActivity.class));
        }

    }

}
