package tipsy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.commun.Organisateur;

public class LoginActivity extends Activity {

    private EditText    email;
    private EditText    password;
    private Button      connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connect  = (Button) findViewById(R.id.connect);

        // TENTATIVE DE CONNEXION
        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                email = (EditText) findViewById(R.id.email);
                password = (EditText) findViewById(R.id.password);
                // D'ABORD TENTATIVE DE CONNEXION EN TANT QU'ORGANISATEUR
                Organisateur user = new Organisateur(email.getText().toString(), password.getText().toString());

                user.login(new StackMobModelCallback() {
                    @Override
                    public void success() {
                        Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(home);
                    }

                    // SINON TENTATIVE DE CONNEXION EN TANT QUE MEMBRE
                    @Override
                    public void failure(StackMobException e) {
                        Intent ma = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(ma);
                    }
                });


            }
        });
    }

}
