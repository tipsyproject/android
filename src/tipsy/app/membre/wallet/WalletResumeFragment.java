package tipsy.app.membre.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import tipsy.app.R;
import tipsy.commun.commerce.Commerce;
import tipsy.commun.commerce.TransactionArrayAdapter;
import tipsy.commun.commerce.Wallet;

/**
 * Created by Alexandre on 23/12/13.
 */
public class WalletResumeFragment extends Fragment {

    private WalletListener callback;
    private Wallet wallet;

    public WalletResumeFragment(Wallet wallet){
        this.wallet = wallet;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (WalletListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wallet_resume, container, false);

        /* Affichage du solde du Wallet */
        TextView viewSolde = (TextView) view.findViewById(R.id.solde);
        viewSolde.setText(Commerce.prixToString(wallet.getSolde(), wallet.getDevise()));

        /* Clique sur Créditer le Wallet */
        ImageButton buttonCredit = (ImageButton) view.findViewById(R.id.button_credit);
        buttonCredit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToCredit();
            }
        });

        ListView listView = (ListView) view.findViewById(R.id.list);
        TransactionArrayAdapter adapter = new TransactionArrayAdapter(getActivity(), wallet.getTransactions());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callback.goToDetailsTransaction(wallet.getTransactions().get(position));
            }
        });

        return view;
    }
}
