package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by tech on 05/12/13.
 */
public class OrgaHomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orga_home);
    }

    // Redirection vers l'activité permettant de créer un Event
    public void creerEventActivity(View view){
        startActivity(new Intent(this, OrgaCreerEventActivity.class));
    }
}
