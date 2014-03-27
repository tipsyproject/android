package com.tipsy.app.orga.entree.qrcode;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.abhi.barcode.frag.libv2.BarcodeFragment;
import com.abhi.barcode.frag.libv2.IScanResultHandler;
import com.abhi.barcode.frag.libv2.ScanResult;
import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeActivity;
import com.tipsy.app.orga.entree.EntreeMenuFragment;
import com.tipsy.app.orga.entree.old.NFCCallback;
import com.tipsy.lib.Achat;
import com.tipsy.lib.util.Bracelet;

import java.util.Iterator;

/**
 * Created by valoo on 24/03/14.
 */
public class ModeQRCodeActivity extends EntreeActivity implements IScanResultHandler {

    private BarcodeFragment fragBarcode;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(ModeQRCodeActivity.class, savedInstanceState);
        fragBarcode = new BarcodeFragment();
        /* Association du fragment QRCode avec l'activité */
        fragBarcode.setScanResultHandler(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mode_container, fragBarcode);
        ft.commit();
    }

    /* RESULTAT SCAN QRCODE */
    public void scanResult(ScanResult result) {
        fragBarcode.onDestroy();
        String entreeID = result.getRawResult().getText();

        Iterator it = entrees.iterator();
        Achat entree = null;
        boolean found = false;
        while (it.hasNext() && !found) {
            entree = (Achat) it.next();
            if (entree.getObjectId().equals(entreeID)) {
                found = true;
            }
        }
        if (!found) {
            //callback.KO("Entrée non reconnue", "Pensez à actualiser la liste");
            fragBarcode.restart();
        } else if (entree.isUsed()){
            //callback.KO("Entrée déjà utilisée", entree.getPrenom() + " " + entree.getNom());
            fragBarcode.restart();
        }
        else {
            //callback.OK(entree.getTicket().getNom(), entree.getPrenom() + " " + entree.getNom());
            //modeNFC();
            entree.setUsed(true);
            final Achat e = entree;
            /*callback.setNFCCallback(new NFCCallback() {
                @Override
                public void onScan(Bracelet bracelet) {
                    /* Bracelet déjà associé à une entrée */
                    /*if(callback.findBracelet(bracelet) > -1)
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
            });*/
        }
    }

    /* Permet au fragment EntreeMenuFragment d'afficher le bon onglet */
    public int getCurrentMode(){
        return EntreeMenuFragment.MODE_QRCODE;
    }

    /* Rien à actualiser */
    public void modelUpdated(){}
}
