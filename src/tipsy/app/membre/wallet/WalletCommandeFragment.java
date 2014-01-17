package tipsy.app.membre.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.commun.Event;
import tipsy.commun.commerce.Commande;
import tipsy.commun.commerce.Commerce;
import tipsy.commun.commerce.ItemArrayAdapter;
import tipsy.commun.commerce.Wallet;

/**
 * Created by Valentin on 30/12/13.
 */
public class WalletCommandeFragment extends Fragment {

    private Commande commande;
    private Event event;
    private Wallet wallet;
    private WalletListener callback;


    public static WalletCommandeFragment init(Event e) {
        WalletCommandeFragment frag = new WalletCommandeFragment();
        Bundle args = new Bundle();
        args.putParcelable("Event", e);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        event = (Event) getArguments().getParcelable("Event");
        View view = inflater.inflate(R.layout.frag_commande, container, false);

        /* On récupère le contenu de la commande */
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        commande = new Commande(app.getPanier());
        commande.setDescriptionFromEvent(event);
        commande.setTitreFromItems();
        wallet = app.getWallet();

        ItemArrayAdapter adapter = new ItemArrayAdapter(getActivity(), commande.getItems());
        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);

        TextView viewPrixTotal = (TextView) view.findViewById(R.id.prix_total);
        viewPrixTotal.setText(Commerce.prixToString(commande.getPrixTotal(), commande.getDevise()));

        Button buttonPay = (Button) view.findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    Log.d("TOUTAFAIT", "commande orga:" + event.getOrganisateur());
                    wallet.pay(commande,event.getOrganisateur());
                } catch (Wallet.WalletInsufficientFundsException e) {
                    Log.d("TOUTAFAIT", "Echec paiement:" + e.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    // Redéfinition de l'actionBar: Bouton de validation
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_wallet, menu);
        LinearLayout walletLayout = (LinearLayout) menu.findItem(R.id.action_wallet).getActionView();
        TextView montant = (TextView) walletLayout.findViewById(R.id.montant);
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        montant.setText(
                Commerce.prixToString(app.getWallet().getSolde(), app.getWallet().getDevise())
        );
    }

    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_wallet:
                Toast.makeText(getActivity(), "Hello World !", Toast.LENGTH_SHORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
