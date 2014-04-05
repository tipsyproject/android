package com.tipsy.app.orga.entree.qrcode;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.abhi.barcode.frag.libv2.BarcodeFragment;
import com.abhi.barcode.frag.libv2.IScanResultHandler;
import com.abhi.barcode.frag.libv2.ScanResult;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeActivity;
import com.tipsy.app.orga.entree.EntreeMenuFragment;
import com.tipsy.app.orga.entree.NFCCallback;
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
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
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
            KO("Entrée non reconnue", "Pensez à actualiser la liste");
            fragBarcode.restart();
        } else if (entree.isUsed()){
            KO("Entrée déjà utilisée", entree.getParticipant().getFullName());
            fragBarcode.restart();
        }
        else {
            OK(entree.getTicket().getNom(), entree.getParticipant().getFullName());
            stepBracelet(entree);
        }
    }

    public void stepBracelet(final Achat entree){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Activation du bracelet");
        builder.setMessage("Scannez un bracelet à activer");
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                fragBarcode.restart();
                Toast.makeText(ModeQRCodeActivity.this, "Entrée annulée", Toast.LENGTH_SHORT).show();
            }
        });
                    /* Annuler */
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fragBarcode.restart();
                Toast.makeText(ModeQRCodeActivity.this, "Entrée annulée", Toast.LENGTH_SHORT).show();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        setNFCCallback(new NFCCallback() {
            @Override
            public void onScan(Bracelet bracelet) {
                    /* Bracelet déjà associé à une entrée */
                if (findBracelet(bracelet) > -1)
                    KO("Bracelet déjà utilisé", "Veuillez en utiliser un autre");
                else {
                    entree.getParticipant().setBracelet(bracelet);
                    entree.setUsed(true);
                    dialog.dismiss();
                    final ProgressDialog loading = ProgressDialog.show(ModeQRCodeActivity.this, null, "Enregistrement...", true, true);
                    entree.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            loading.dismiss();
                            if(e==null){
                                setNFCCallback(null);
                                OK("Bracelet Activé", entree.getParticipant().getFullName());
                            }else {
                                Toast.makeText(ModeQRCodeActivity.this, "Erreur enregistrement", Toast.LENGTH_SHORT).show();
                            }
                            fragBarcode.restart();
                        }
                    });

                }
            }
        });
    }

    /* Permet au fragment EntreeMenuFragment d'afficher le bon onglet */
    public int getCurrentMode(){
        return EntreeMenuFragment.MODE_QRCODE;
    }

    /* Rien à actualiser */
    public void modelUpdated(){}
}
