package com.tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.parse.Parse;
import com.tipsy.app.membre.MembreActivity;
import com.tipsy.app.orga.OrgaActivity;
import com.tipsy.lib.Prefs;
import com.tipsy.lib.TipsyUser;

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

        /*
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Settings.addLoggingBehavior(LoggingBehavior.REQUESTS);

        Request request = Request.newGraphPathRequest(null, "/4", new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                if(response.getError() != null) {
                    Log.i("MainActivity", String.format("Error making request: %s", response.getError()));
                } else {
                    GraphUser user = response.getGraphObjectAs(GraphUser.class);
                    Log.i("MainActivity", String.format("Name: %s", user.getName()));
                }
            }
        });
        request.executeAsync();*/

        TipsyApp app = (TipsyApp) getApplication();
        // Affichage de l'aide si elle n'a encore jamais été passée.
        // Sinon, on essaye de reconnecter automatiquement l'utilisateur
        if (!app.skipHelp(this)) startActivity(new Intent(this, HelpActivity.class));
        else{
            TipsyUser user = (TipsyUser) TipsyUser.getCurrentUser();
            if(user.getType() == TipsyUser.MEMBRE)
                //startActivity(new Intent(this, MembreActivity.class));
                Toast.makeText(this,"connexion auto membre", Toast.LENGTH_SHORT).show();
            else if(user.getType() == TipsyUser.ORGA)
                //startActivity(new Intent(this, OrgaActivity.class));
                Toast.makeText(this,"connexion auto orga", Toast.LENGTH_SHORT).show();
            else
                startActivity(new Intent(this, LoginActivity.class));
        }

    }

}
