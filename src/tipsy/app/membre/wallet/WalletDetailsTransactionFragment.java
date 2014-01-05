package tipsy.app.membre.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tipsy.app.R;
import tipsy.commun.commerce.Transaction;

/**
 * Created by Alexandre on 23/12/13.
 */
public class WalletDetailsTransactionFragment extends Fragment {

    private WalletListener callback;
    private Transaction transaction;

    public WalletDetailsTransactionFragment(Transaction transaction){
        this.transaction = transaction;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (WalletListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wallet_details_transaction, container, false);
        return view;
    }
}
