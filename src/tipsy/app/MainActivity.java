package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.stackmob.android.sdk.common.StackMobAndroid;

import tipsy.commun.User;


/**
 * Created by Alexandre on 04/12/13.
 */

public class MainActivity extends Activity {

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initialisation de STACKMOB avec la clé publique
        StackMobAndroid.init(getApplicationContext(), 0, "eeedff37-f59d-408a-9279-27cd8fe7062e");
        setContentView(R.layout.act_loading);


        super.onCreate(savedInstanceState);

        TipsyApp app = (TipsyApp) getApplication();
        // Affichage de l'aide si elle n'a encore jamais été passée.
        // Sinon, on essaye de reconnecter automatiquement l'utilisateur
        if (!app.skipHelp(this)) startActivity(new Intent(this, HelpActivity.class));
        else User.tryLogin(this);
    }


}
