package com.tipsy.app.membre.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.lib.Commande;
import com.tipsy.lib.Commerce;
import com.tipsy.lib.ItemArrayAdapter;
import com.tipsy.lib.Panier;
import com.tipsy.lib.Wallet;
import com.tipsy.lib.WalletCallback;

/**
 * Created by Valentin on 30/12/13.
 */
public class WalletCommandeFragment extends Fragment {

    private Commande commande;
    private WalletListener callback;


    public static WalletCommandeFragment init(Panier p, Commande c) {
        WalletCommandeFragment frag = new WalletCommandeFragment();
        Bundle args = new Bundle();
        args.putParcelable("Panier", p);
        args.putParcelable("Commande", c);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        commande = getArguments().getParcelable("Commande");
        Panier panier = getArguments().getParcelable("Panier");
        View view = inflater.inflate(R.layout.frag_commande, container, false);

        TextView viewPrixTotal = (TextView) view.findViewById(R.id.prix_total);
        viewPrixTotal.setText(Commerce.prixToString(commande.getPrixTotal(), commande.getDevise()));

        ItemArrayAdapter adapter = new ItemArrayAdapter(getActivity(), panier, viewPrixTotal);

        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);

        Button buttonPay = (Button) view.findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ProgressDialog wait = Wallet.getProgressDialog(getActivity());

                callback.getWallet().pay(commande, new WalletCallback() {
                    @Override
                    public void onWait() {
                        wait.show();
                    }

                    @Override
                    public void onSuccess() {
                        wait.dismiss();
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
                    }

                    @Override
                    public void onFailure(final Exception e) {
                        wait.dismiss();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
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
        /*montant.setText(
                Commerce.prixToString(app.getWallet().getSolde(), app.getWallet().getDevise())
        );*/
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
