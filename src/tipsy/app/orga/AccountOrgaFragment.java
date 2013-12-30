package tipsy.app.orga;

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
import tipsy.app.membre.MenuMembre;
import tipsy.commun.Organisateur;

/**
 * Created by Alexandre on 23/12/13.
 */
public class AccountOrgaFragment extends Fragment implements TextWatcher {

    private OrgaListener callback;
    protected EditText Orga;
    protected EditText Email;
    protected ImageButton Save;
    protected boolean change = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OrgaListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.frag_orga_account, container, false);
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        final Organisateur orga = app.getOrga();


        Orga = (EditText) fragmentView.findViewById(R.id.input_orga);
        Email = (EditText) fragmentView.findViewById(R.id.input_mail);
        Save = (ImageButton) fragmentView.findViewById(R.id.save);

        Orga.setText(orga.getNom());
        Email.setHint(orga.getEmail());

        Orga.addTextChangedListener(this);
        Email.setFocusable(false);
        Email.setEnabled(false);
        Save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (change) {
                    orga.setNom(Orga.getText().toString());
                    orga.save(new StackMobModelCallback() {
                        @Override
                        public void success() {
                            startActivity(new Intent(getActivity(), OrgaActivity.class));
                        }

                        // En cas d'échec
                        @Override
                        public void failure(StackMobException e) {
                            Toast.makeText(getActivity(), "Sauvegarde échouée", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    startActivity(new Intent(getActivity(), OrgaActivity.class));
            }
        });
        return fragmentView;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public void afterTextChanged(Editable s) {
        change = true;
    }

    @Override
     public void onStart(){
        super.onStart();
        callback.setMenuTitle(MenuOrga.MON_COMPTE);
    }
}
