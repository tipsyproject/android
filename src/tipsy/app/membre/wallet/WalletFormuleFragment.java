package tipsy.app.membre.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Validator;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.commun.commerce.Wallet;
import tipsy.commun.commerce.WalletCallback;

/**
 * Created by Alexandre on 20/01/14.
 */
public class WalletFormuleFragment extends Fragment {

    private Button button20;
    private Button button50;
    private Button button100;
    private Button buttonAutres;

    private Wallet wallet;
    private WalletListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (WalletListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wallet_formule, container, false);

        TipsyApp app = (TipsyApp) getActivity().getApplication();
        wallet = app.getWallet();

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
            switch(v.getId()) {
                case R.id.rech20 :
                    montant=2000;
                    break;
                case R.id.rech50 :
                    montant=5000;
                    break;
                case R.id.rech100 :
                    montant=10000;
                    break;
                case R.id.autres :
                    callback.goToCredit();
                    break;
            }
            if (v.getId() != R.id.autres){
                final ProgressDialog wait = Wallet.getProgressDialog(getActivity());
                wallet.credit(montant, new WalletCallback() {
                    @Override
                    public void onWait() {
                        wait.show();
                    }

                    @Override
                    public void onSuccess() {
                        wait.dismiss();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Rechargement effectué", Toast.LENGTH_LONG).show();
                            }
                        });
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        wait.dismiss();
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };
}
