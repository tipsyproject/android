package com.tipsy.app.orga.entree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.liste.ModeListeActivity;
import com.tipsy.app.orga.entree.qrcode.ModeQRCodeActivity;
import com.tipsy.app.orga.entree.stats.ModeStatsActivity;
import com.tipsy.app.orga.entree.vente.ModeVenteActivity;

/**
 * Created by valoo on 21/03/14.
 */
public class EntreeMenuFragment extends EntreeFragment {

    private ImageButton buttonStats;
    private ImageButton buttonQRCode;
    private ImageButton buttonListe;
    private ImageButton buttonVente;

    /* Mode Courant */
    public static final int MODE_STATS = 0;
    public static final int MODE_QRCODE = 1;
    public static final int MODE_LISTE = 2;
    public static final int MODE_VENTE = 3;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_menu, container, false);

        buttonStats = (ImageButton) view.findViewById(R.id.button_stats);
        buttonQRCode = (ImageButton) view.findViewById(R.id.button_qrcode);
        buttonListe = (ImageButton) view.findViewById(R.id.button_liste);
        buttonVente = (ImageButton) view.findViewById(R.id.button_vente);
        buttonStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback.getCurrentMode() != MODE_STATS)
                    modeStats();
            }
        });
        buttonQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback.getCurrentMode() != MODE_QRCODE)
                    modeQRCode();
            }
        });
        buttonListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback.getCurrentMode() != MODE_LISTE)
                    modeListe();
            }
        });
        buttonVente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback.getCurrentMode() != MODE_VENTE)
                    modeVente();
            }
        });

        /* Seulement après l'initialisation des buttons */
        setMode(callback.getCurrentMode());
        return view;
    }

    private void setMode(int mode){
        buttonStats.setBackgroundResource(mode == MODE_STATS ? R.color.primary : R.color.secondary);
        buttonQRCode.setBackgroundResource(mode == MODE_QRCODE ? R.color.primary : R.color.secondary);
        buttonListe.setBackgroundResource(mode == MODE_LISTE ? R.color.primary : R.color.secondary);
        buttonVente.setBackgroundResource(mode == MODE_VENTE ? R.color.primary : R.color.secondary);
    }




    /* MODE STATS */
    public void modeStats() {
        goTo(ModeStatsActivity.class);
    }
    /* MODE QR CODE */
    public void modeQRCode() {
        goTo(ModeQRCodeActivity.class);
    }
    /* MODE LISTE */
    public void modeListe() {
        goTo(ModeListeActivity.class);
    }
    /* MODE VENTE */
    public void modeVente() {
        goTo(ModeVenteActivity.class);
    }

    private void goTo(Class<?> cls){
        getActivity().overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra("eventId", callback.getEventId());
        intent.putExtra("event", callback.getEvent());
        intent.putExtra("billetterie", callback.getBilletterie());
        intent.putExtra("entrees", callback.getEntrees());
        startActivity(intent);
        getActivity().finish();
    }
}
