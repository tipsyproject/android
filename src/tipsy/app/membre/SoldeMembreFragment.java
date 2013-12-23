package tipsy.app.membre;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tipsy.app.R;

/**
 * Created by Alexandre on 23/12/13.
 */
public class SoldeMembreFragment extends Fragment {

    public SoldeMembreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.frag_solde, container, false);

        return fragmentView;
    }
}
