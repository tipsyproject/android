package tipsy.app.membre;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.commun.Membre;

/**
 * Created by Alexandre on 23/12/13.
 */
public class AccountMembreFragment extends Fragment implements TextWatcher {

    private MembreListener callback;
    protected EditText Nom;
    protected EditText Prenom;
    protected EditText Email;
    protected ImageButton Save;
    protected boolean change = false;

    public AccountMembreFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MembreListener) activity;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.frag_membre_account, container, false);
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        final Membre membre = app.getMembre();


        Nom = (EditText) fragmentView.findViewById(R.id.input_nom);
        Prenom = (EditText) fragmentView.findViewById(R.id.input_prenom);
        Email = (EditText) fragmentView.findViewById(R.id.input_mail);
        Save = (ImageButton) fragmentView.findViewById(R.id.save);

        Nom.setText(membre.getNom());
        Prenom.setText(membre.getPrenom());
        Email.setHint(membre.getEmail());

        Nom.addTextChangedListener(this);
        Prenom.addTextChangedListener(this);
        Email.setFocusable(false);
        Email.setEnabled(false);
        Save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (change) {
                    membre.setNom(Nom.getText().toString());
                    membre.setPrenom(Prenom.getText().toString());
                    membre.save(new StackMobModelCallback() {
                        @Override
                        public void success() {
                            startActivity(new Intent(getActivity(), MembreActivity.class));
                        }

                        // En cas d'échec
                        @Override
                        public void failure(StackMobException e) {
                            Toast.makeText(getActivity(), "Sauvegarde échouée", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    startActivity(new Intent(getActivity(), MembreActivity.class));
            }
        });
        return fragmentView;
    }

    @Override
    public void onStart(){
        super.onStart();
        callback.setMenuTitle(MenuMembre.MON_COMPTE);
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public void afterTextChanged(Editable s) {
        change = true;
    }
}
