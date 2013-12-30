package tipsy.app.membre;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import tipsy.app.R;
import tipsy.app.orga.OrgaListener;

/**
 * Created by Alexandre on 23/12/13.
 */
public class HomeMembreFragment extends Fragment {

    private MembreListener callback;
    private LinearLayout buttonAccount;

    public HomeMembreFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MembreListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_membre_home, container, false);
        buttonAccount = (LinearLayout) view.findViewById(R.id.button_account);
        buttonAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToAccount();
            }
        });
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        callback.setMenuTitle(MenuMembre.ACCUEIL);
    }
}