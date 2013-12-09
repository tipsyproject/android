package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.stackmob.android.sdk.common.StackMobAndroid;

/**
 * Created by Alexandre on 04/12/13.
 */

public class MainActivity extends Activity{
    FrameLayout main;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initialisation de STACKMOB avec la clé publique
        StackMobAndroid.init(getApplicationContext(), 0, "60e9809f-6268-4fa3-9d4b-a4375ac15884");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (FrameLayout) findViewById(R.id.main);
        final Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent connection = new Intent(MainActivity.this, Connection.class);
                startActivity(connection);
            }
        });
    }
}
