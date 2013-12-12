package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.List;

import tipsy.commun.Organisateur;

/**
 * Created by Alexandre on 04/12/13.
 */

public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initialisation de STACKMOB avec la clé publique
        StackMobAndroid.init(getApplicationContext(), 0, "eeedff37-f59d-408a-9279-27cd8fe7062e");
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        setContentView(R.layout.activity_loading);

        super.onCreate(savedInstanceState);

        if (StackMob.getStackMob().isLoggedIn()) {
            Organisateur.getLoggedInUser(Organisateur.class, new StackMobQueryCallback<Organisateur>() {
                @Override
                public void success(List<Organisateur> list) {
                    Organisateur loggedInUser = list.get(0);
                    startActivity(new Intent(MainActivity.this, tipsy.app.orga.HomeOrgaActivity.class));
                }

                @Override
                public void failure(StackMobException e) {
                    Log.d("Connexion Orga", e.getMessage());
                    startActivity(new Intent(MainActivity.this, HelpMainActivity.class));
                }
            });
        } else {
            startActivity(new Intent(MainActivity.this, HelpMainActivity.class));
        }
    }


}
