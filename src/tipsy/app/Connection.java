package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.commun.Organisateur;

public class Connection extends Activity {

    private EditText    username;
    private EditText    password;
    private Button      connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        connect  = (Button) findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // D'ABORD TENTATIVE DE CONNEXION EN TANT QU'ORGANISATEUR
                Organisateur user = new Organisateur(username.toString(), password.toString());
                user.login(new StackMobModelCallback() {
                    @Override
                    public void success() {
                        Intent home = new Intent(Connection.this, Home.class);
                        startActivity(home);
                    }

                    // SINON TENTATIVE DE CONNEXION EN TANT QUE MEMBRE
                    @Override
                    public void failure(StackMobException e) {
                        return;
                    }
                });


            }
        });
    }
}
