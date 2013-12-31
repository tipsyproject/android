package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tipsy.app.membre.SignUpMembreActivity;
import tipsy.app.orga.SignUpOrgaActivity;

/**
 * Created by fulgor on 23/12/13.
 */
public class TypeSignUpActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_typesignup);
    }

    public void OnClickInscription(View view) {

        startActivity(new Intent(this, SignUpMembreActivity.class));

    }

    public void OnClickOrga(View view) {

        startActivity(new Intent(this, SignUpOrgaActivity.class));

    }

}
