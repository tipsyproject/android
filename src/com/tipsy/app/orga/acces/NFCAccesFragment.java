package com.tipsy.app.orga.acces;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Achat;

/**
 * Created by vquefele on 20/01/14.
 */
public class NFCAccesFragment extends Fragment {

    private AccesListener callback;
    NfcAdapter adapter;
    PendingIntent pendingIntent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (AccesListener) activity;
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        adapter = NfcAdapter.getDefaultAdapter(getActivity());
        pendingIntent = PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(), AccesActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.enableForegroundDispatch(getActivity(), pendingIntent, null, null);
    }

    @Override
    public void onPause() {
        adapter.disableForegroundDispatch(getActivity());
        super.onPause();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_access_nfc, container, false);
        callback.setProgressText( (TextView) view.findViewById(R.id.progressText) );
        callback.setProgressBar( (ProgressBar) view.findViewById(R.id.progressBar) );
        Button buttonQrcode = (Button) view.findViewById(R.id.button_qrcode);
        buttonQrcode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        Button buttonSearch = (Button) view.findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        callback.updateProgress();
        return view;
    }

}
