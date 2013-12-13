package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import tipsy.app.membre.SignUpMembreActivity;
import tipsy.app.orga.SignUpOrgaActivity;

/**
 * Created by Guillaume on 09/12/13.
 */
public class ChoiceActivity extends Activity {


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_choice);

        final ImageButton next_orga = (ImageButton) findViewById(R.id.next_orga);
        next_orga.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(ChoiceActivity.this, SignUpOrgaActivity.class));
            }
        });

        final ImageButton next_user = (ImageButton) findViewById(R.id.next_user);
        next_user.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(ChoiceActivity.this, SignUpMembreActivity.class));
            }
        });
    }
}