package com.tipsy.app.orga.entree;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tipsy.app.R;

/**
 * Created by valoo on 21/03/14.
 */
public class EntreeMenuFragment extends Fragment {

    private EntreeListener callback;

    private ImageButton buttonStats;
    private ImageButton buttonQRCode;
    private ImageButton buttonListe;
    private ImageButton buttonVente;


    /* Mode Courant */
    public static final int MODE_STATS = 0;
    public static final int MODE_QRCODE = 1;
    public static final int MODE_LISTE = 2;
    public static final int MODE_VENTE = 3;
    private int currentMode = MODE_STATS;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_menu, container, false);

        buttonStats = (ImageButton) view.findViewById(R.id.button_stats);
        buttonQRCode = (ImageButton) view.findViewById(R.id.button_qrcode);
        buttonListe = (ImageButton) view.findViewById(R.id.button_liste);
        buttonVente = (ImageButton) view.findViewById(R.id.button_vente);

        buttonStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMode(MODE_STATS);
                callback.modeStats();

            }
        });

        buttonQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMode(MODE_QRCODE);
                callback.modeQRCode();
            }
        });


        buttonListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMode(MODE_LISTE);
                callback.modeListe();
            }
        });


        buttonVente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMode(MODE_VENTE);
                callback.modeVente();
            }
        });

        if(savedInstanceState != null){
            setMode(savedInstanceState.getInt("MODE"));
        }

        return view;
    }

    private void setMode(int mode){
        buttonStats.setBackgroundResource(mode == MODE_STATS ? R.color.primary : R.color.secondary);
        buttonQRCode.setBackgroundResource(mode == MODE_QRCODE ? R.color.primary : R.color.secondary);
        buttonListe.setBackgroundResource(mode == MODE_LISTE ? R.color.primary : R.color.secondary);
        currentMode = mode;
    }

    public int getCurrentMode() {
        return currentMode;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putInt("MODE", currentMode);
        super.onSaveInstanceState(outState);
    }
}
