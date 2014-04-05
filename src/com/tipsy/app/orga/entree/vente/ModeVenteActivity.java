package com.tipsy.app.orga.entree.vente;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeActivity;
import com.tipsy.app.orga.entree.EntreeMenuFragment;
import com.tipsy.app.orga.entree.NFCCallback;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Bracelet;

/**
 * Created by valoo on 24/03/14.
 */
public class ModeVenteActivity extends EntreeActivity implements ModeVenteListener{

    private Achat prevente;

    private WaitFragment fragWait;
    private TarifsFragment fragTarifs;
    private ParticipantFragment fragParticipant;

    private boolean stepBack = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(ModeVenteActivity.class, savedInstanceState);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        fragWait = new WaitFragment();
        fragTarifs = new TarifsFragment();
        fragParticipant = new ParticipantFragment();
        resetPrevente();
        stepWait();
    }

    public boolean modePrevente(){
        return fragWait.modePrevente();
    }

    private void resetPrevente(){
        prevente = new Achat();
        prevente.setParticipant(new Participant());
    }

    public Achat getPrevente(){
        return prevente;
    }

    public void finishPrevente(){
        final ProgressDialog wait = ProgressDialog.show(this, null, "Enregistrement...", true, true);
        prevente.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    wait.dismiss();
                    OK(prevente.getTicket().getNom(),prevente.getParticipant().getFullName());
                    entrees.add(prevente);
                    resetPrevente();
                    stepWait();
                }
            }
        });
    }

    public void stepWait(){
        resetPrevente();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mode_container, fragWait);
        ft.commit();
        setNFCCallback(new NFCCallback() {
            @Override
            public void onScan(Bracelet bracelet) {
                Log.d("TOUTAFAIT", bracelet.getTag());
                /* Le bracelet ne doit pas avoir été associé à un participant de l'event */
                if (findBracelet(bracelet) != -1) {
                    KO("Bracelet déjà utilisé", "Veuillez en utiliser un autre");
                } else {
                    Toast.makeText(ModeVenteActivity.this, "Bracelet activé", Toast.LENGTH_SHORT).show();
                    prevente.getParticipant().setBracelet(bracelet);
                    prevente.setUsed(!modePrevente());
                    setNFCCallback(null);
                    stepTarifs();
                }
            }
        });
        stepBack = false;
    }

    public void stepTarifs(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.mode_container, fragTarifs);
        ft.commit();
        stepBack = true;
    }

    public void stepParticipant(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mode_container, fragParticipant);
        ft.commit();
        stepBack = true;
    }



    /* Le bouton Home sert de retour à l'étape initiale du process de vente */
    @Override
    public void onBackPressed() {
        if(stepBack) {
            stepWait();
            Toast.makeText(ModeVenteActivity.this, "Vente annulée", Toast.LENGTH_SHORT).show();
        }else
            super.onBackPressed();
    }

    /* Permet au fragment EntreeMenuFragment d'afficher le bon onglet */
    public int getCurrentMode(){
        return EntreeMenuFragment.MODE_VENTE;
    }

    /* Rien à actualiser */
    public void modelUpdated(){}
}
