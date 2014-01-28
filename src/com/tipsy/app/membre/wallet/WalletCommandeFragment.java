package com.tipsy.app.membre.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.lib.Achat;
import com.tipsy.lib.TipsyUser;
import com.tipsy.lib.util.Commerce;
import com.tipsy.lib.util.QueryCallback;
import com.tipsy.lib.util.Wallet;

/**
 * Created by Valentin on 30/12/13.
 */
public class WalletCommandeFragment extends Fragment {

    private WalletListener callback;

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
        View view = inflater.inflate(R.layout.frag_commande, container, false);

        TextView viewPrixTotal = (TextView) view.findViewById(R.id.prix_total);
        viewPrixTotal.setText(Commerce.prixToString(callback.getCommande().getPrixTotal(), callback.getCommande().getDevise()));

        //ItemArrayAdapter adapter = new ItemArrayAdapter(getActivity(), panier, viewPrixTotal);

        //ListView listView = (ListView) view.findViewById(R.id.list);
        //listView.setAdapter(adapter);

        Button buttonPay = (Button) view.findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ProgressDialog wait = Wallet.getProgressDialog(getActivity());
                TipsyApp app = (TipsyApp) getActivity().getApplication();
                app.getWallet().pay(callback.getCommande(), new QueryCallback() {
                    @Override
                    public void done(Exception e) {
                        wait.dismiss();
                        if(e==null){
                            getActivity().finish();
                            getActivity().overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
                        }else{
                            Log.d("TOUTAFAIT", "erreur pay: " + e.getMessage());
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }


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
