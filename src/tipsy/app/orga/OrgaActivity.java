package tipsy.app.orga;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.List;

import tipsy.app.HelpActivity;
import tipsy.app.LoginActivity;
import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.UserActivity;
import tipsy.commun.Prefs;
import tipsy.commun.User;

/**
 * Created by Valoo on 05/12/13.
 */
public class OrgaActivity extends UserActivity {

    private TipsyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_user_menu);
        super.onCreate(savedInstanceState);
        this.menu = new MenuOrga(this);
        menu.initAdapter(new UserActivity.DrawerItemClickListener());

        app = (TipsyApp) getApplication();

        Log.d("TOUTAFAIT", app.getOrga().getEmail());

        /*buttonCreerEvent = (ImageButton) findViewById(R.id.button_creer_event);

        buttonCreerEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(HomeOrgaActivity.this, EditEventActivity.class));*/
    }


    protected void selectItem(int position) {

        if (position == MenuOrga.AIDE)
            startActivity(new Intent(this, HelpActivity.class));
        else if (position == MenuOrga.DECONNEXION) {
            app.logout(this);

        } else {
            this.menu.getDrawerList().setItemChecked(position, true);
            setTitle(this.menu.getTitres_menu()[position]);
            this.menu.getDrawerLayout().closeDrawer(this.menu.getDrawerList());
        }

    }

}
