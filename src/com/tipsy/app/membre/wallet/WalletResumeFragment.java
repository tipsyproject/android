package com.tipsy.app.membre.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.lib.Commerce;
import com.tipsy.lib.Transaction;
import com.tipsy.lib.TransactionArrayAdapter;

/**
 * Created by Alexandre on 23/12/13.
 */
public class WalletResumeFragment extends Fragment {

    private WalletListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (WalletListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wallet_resume, container, false);
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        //wallet = app.getWallet();

        /* Affichage du solde du Wallet */
        TextView viewSolde = (TextView) view.findViewById(R.id.solde);
        viewSolde.setText(Commerce.prixToString(callback.getWallet().getSolde(), callback.getWallet().getDevise()));

        /* Clique sur Créditer le Wallet */
        ImageButton buttonCredit = (ImageButton) view.findViewById(R.id.button_credit);
        buttonCredit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToFormule();
            }
        });

        ListView listView = (ListView) view.findViewById(R.id.list);
        TransactionArrayAdapter adapter = new TransactionArrayAdapter(getActivity(), callback.getWallet());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callback.goToDetailsTransaction((Transaction) callback.getWallet().get(position));
            }
        });

        return view;
    }
}
