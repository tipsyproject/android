package tipsy.app.membre.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.membre.MembreListener;
import tipsy.commun.commerce.Commande;
import tipsy.commun.commerce.Commerce;
import tipsy.commun.commerce.Item;
import tipsy.commun.commerce.ItemArrayAdapter;
import tipsy.commun.commerce.Wallet;

/**
 * Created by Valentin on 30/12/13.
 */
public class WalletCommandeFragment extends Fragment {

    private Commande commande;
    private WalletListener callback;

    public WalletCommandeFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (WalletListener) activity;
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        commande = new Commande(app.getPanier());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_commande, container, false);

        /* On récupère le contenu de la commande */
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        commande = new Commande(app.getPanier());

        ItemArrayAdapter adapter = new ItemArrayAdapter(getActivity(), commande.getItems());
        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);

        TextView viewPrixTotal = (TextView) view.findViewById(R.id.prix_total);
        viewPrixTotal.setText(Commerce.prixToString(commande.getPrixTotal(), commande.getDevise()));

        Button buttonPay = (Button) view.findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        return view;
    }
}
