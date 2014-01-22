package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.stackmob.android.sdk.common.StackMobAndroid;

import tipsy.commun.Prefs;
import tipsy.commun.User;


/**
 * Created by Alexandre on 04/12/13.
 */

public class MainActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initialisation de STACKMOB avec la clé publique
        StackMobAndroid.init(getApplicationContext(), 0, "eeedff37-f59d-408a-9279-27cd8fe7062e");
        setContentView(R.layout.act_loading);


        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean(Prefs.CONNECTED, false);

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
        request.executeAsync();

        TipsyApp app = (TipsyApp) getApplication();
        // Affichage de l'aide si elle n'a encore jamais été passée.
        // Sinon, on essaye de reconnecter automatiquement l'utilisateur
        if (!app.skipHelp(this)) startActivity(new Intent(this, HelpActivity.class));
        else User.tryLogin(this);
    }


}
