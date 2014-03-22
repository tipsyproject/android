package com.tipsy.app.orga.entree.qrcode;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abhi.barcode.frag.libv2.BarcodeFragment;
import com.abhi.barcode.frag.libv2.IScanResultHandler;
import com.abhi.barcode.frag.libv2.ScanResult;
import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeListener;
import com.tipsy.app.orga.entree.EntreeNFCFragment;
import com.tipsy.app.orga.entree.ModeFragment;
import com.tipsy.app.orga.entree.NFCCallback;
import com.tipsy.app.orga.entree.liste.ListeFragment;
import com.tipsy.lib.Achat;
import com.tipsy.lib.util.Bracelet;

import java.util.Iterator;


/**
 * Created by vquefele on 20/01/14.
 */
public class ModeQRCodeFragment extends ModeFragment implements IScanResultHandler {

    private BarcodeFragment fragBarcode;
    private EntreeNFCFragment fragNFC;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragNFC = callback.getFragNFC();
        fragBarcode = new BarcodeFragment();
        /* Association du fragment QRCode avec l'activité */
        fragBarcode.setScanResultHandler(this);
        modeQRCode();
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    /* RESULTAT SCAN QRCODE */
    public void scanResult(ScanResult result) {
        fragBarcode.onDestroy();
        String entreeID = result.getRawResult().getText();

        Iterator it = callback.getEntrees().iterator();
        Achat entree = null;
        boolean found = false;
        while (it.hasNext() && !found) {
            entree = (Achat) it.next();
            if (entree.getObjectId().equals(entreeID)) {
                found = true;
            }
        }
        if (!found) {
            callback.KO("Entrée non reconnue", "Pensez à actualiser la liste");
            fragBarcode.restart();
        } else if (entree.isUsed()){
            callback.KO("Entrée déjà utilisée", entree.getPrenom() + " " + entree.getNom());
            fragBarcode.restart();
        }
        else {
            callback.OK(entree.getTicket().getNom(), entree.getPrenom() + " " + entree.getNom());
            modeNFC();
            entree.setUsed(true);
            final Achat e = entree;
            callback.setNFCCallback(new NFCCallback() {
                @Override
                public void onScan(Bracelet bracelet) {
                    /* Bracelet déjà associé à une entrée */
                    if(callback.findBracelet(bracelet) > -1)
                        callback.KO("Bracelet déjà utilisé","Veuillez en utiliser un autre");
                    else {
                        e.getParticipant().setBracelet(bracelet.getTag());
                        e.setUsed(true);
                        e.saveInBackground();
                        callback.setNFCCallback(null);
                        Toast.makeText(getActivity(), "Bracelet activé", Toast.LENGTH_SHORT).show();
                        modeQRCode();
                    }
                }
            });
        }
    }

    /* AFFICHAGE DE L'ECOUTE NFC */
    public void modeQRCode(){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.frag_container, fragBarcode);
        ft.commit();
    }

    /* AFFICHAGE DE L'ECOUTE NFC */
    public void modeNFC(){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.frag_container, fragNFC);
        ft.commit();
    }

}
