package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import tipsy.app.orga.InscriptionActivity;

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
        setContentView(R.layout.activity_choice);

        final ImageButton next_orga = (ImageButton) findViewById(R.id.next_orga);
        next_orga.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent connect = new Intent(ChoiceActivity.this, InscriptionActivity.class);
                startActivity(connect);
            }
        });

        final ImageButton next_user = (ImageButton) findViewById(R.id.next_user);
        next_user.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent connect = new Intent(ChoiceActivity.this, InscriptionUserActivity.class);
                startActivity(connect);
            }
        });
    }
}