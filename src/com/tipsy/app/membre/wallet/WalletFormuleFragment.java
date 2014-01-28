package com.tipsy.app.membre.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.lib.util.QueryCallback;
import com.tipsy.lib.util.Wallet;

/**
 * Created by Alexandre on 20/01/14.
 */
public class WalletFormuleFragment extends Fragment {

    private Button button20;
    private Button button50;
    private Button button100;
    private Button buttonAutres;
    private WalletListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (WalletListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wallet_formule, container, false);

        button20 = (Button) view.findViewById(R.id.rech20);
        button50 = (Button) view.findViewById(R.id.rech50);
        button100 = (Button) view.findViewById(R.id.rech100);
        buttonAutres = (Button) view.findViewById(R.id.autres);

        button20.setOnClickListener(clickListener);
        button50.setOnClickListener(clickListener);
        button100.setOnClickListener(clickListener);
        buttonAutres.setOnClickListener(clickListener);

        return view;
    }

    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int montant = 0;
            switch (v.getId()) {
                case R.id.rech20:
                    montant = 2000;
                    break;
                case R.id.rech50:
                    montant = 5000;
                    break;
                case R.id.rech100:
                    montant = 10000;
                    break;
                case R.id.autres:
                    callback.goToCredit();
                    break;
            }
            if (v.getId() != R.id.autres) {
                final ProgressDialog wait = Wallet.getProgressDialog(getActivity());
                wait.show();

                TipsyApp app = (TipsyApp) getActivity().getApplication();
                app.getWallet().credit(montant, new QueryCallback() {
                    @Override
                    public void done(Exception e) {
                        wait.dismiss();
                        if(e==null){
                            Toast.makeText(getActivity(), "Rechargement effectué", Toast.LENGTH_LONG).show();
                            getActivity().finish();
                        }else Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };
}
