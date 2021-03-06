package com.tipsy.app.orga.bar;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tipsy.app.R;

/**
 * Created by Alextoss on 19/03/2014.
 */

public class BarNFCFragment extends Fragment {

    private NfcAdapter adapter;
    private PendingIntent pendingIntent;


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
        Button buttonCancel = (Button) view.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        return view;
    }

    public void show(){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.show(this);
        ft.commit();
    }

    public void hide(){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.hide(this);
        ft.commit();
    }

}
