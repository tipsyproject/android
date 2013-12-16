package tipsy.app.orga;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.List;

import tipsy.commun.Organisateur;
import tipsy.commun.User;

/**
 * Created by Valoo on 05/12/13.
 */
abstract class OrgaActivity extends FragmentActivity {

    protected Organisateur orga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* mise à disposition de l'organisateur connecté */
        User.getLoggedInUser(User.class, new StackMobQueryCallback<User>() {
            @Override
            public void success(List<User> list) {
                User user = list.get(0);
            }

            @Override
            public void failure(StackMobException e) {

            }
        });
    }

}
