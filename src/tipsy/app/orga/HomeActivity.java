package tipsy.app.orga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tipsy.app.R;

/**
 * Created by tech on 05/12/13.
 */
public class HomeActivity extends Activity {

    private Button buttonCreerEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orga_home);

        buttonCreerEvent = (Button) findViewById(R.id.button_creer_event);

        buttonCreerEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, EditEventActivity.class));
            }
        });
    }
}
