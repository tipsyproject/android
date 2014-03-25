package com.tipsy.app.orga.bar;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tipsy.app.R;

/**
 * Created by Alextoss on 19/03/2014.
 */

public class BarNFCFragment extends Fragment {

    NfcAdapter adapter;
    PendingIntent pendingIntent;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        adapter = NfcAdapter.getDefaultAdapter(getActivity());
        pendingIntent = PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(), BarActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
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
        View view = inflater.inflate(R.layout.frag_bar_nfc, container, false);

        return view;
    }

}
