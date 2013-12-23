package tipsy.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.app.membre.MembreActivity;
import tipsy.commun.Membre;

/**
 * Created by tech on 23/12/13.
 */

public class MyProfile extends FragmentActivity implements TextWatcher {

    protected EditText Nom;
    protected EditText Prenom;
    protected EditText Email;
    protected ImageButton Save;
    protected boolean change = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initialisation de STACKMOB avec la clé publique
        StackMobAndroid.init(getApplicationContext(), 0, "eeedff37-f59d-408a-9279-27cd8fe7062e");
        setContentView(R.layout.act_myprofile);
        super.onCreate(savedInstanceState);

        TipsyApp app = (TipsyApp) getApplication();
        final Membre membre = app.getMembre();


        Nom = (EditText) findViewById(R.id.input_nom);
        Prenom = (EditText) findViewById(R.id.input_prenom);
        Email = (EditText) findViewById(R.id.input_mail);
        Save = (ImageButton) findViewById(R.id.save);

        Nom.setText(membre.getNom());
        Prenom.setText(membre.getPrenom());
        Email.setText(membre.getEmail());

        Nom.addTextChangedListener(this);
        Prenom.addTextChangedListener(this);
        //Email.addTextChangedListener(this);
        Save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (change) {
                    membre.setNom(Nom.getText().toString());
                    membre.setPrenom(Prenom.getText().toString());
                    membre.save(new StackMobModelCallback() {
                        @Override
                        public void success() {
                            startActivity(new Intent(MyProfile.this, MembreActivity.class));
                        }

                        // En cas d'échec
                        @Override
                        public void failure(StackMobException e) {
                            Toast.makeText(MyProfile.this, "Sauvegarde échouée", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    startActivity(new Intent(MyProfile.this, MembreActivity.class));
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        change = true;
    }
}
