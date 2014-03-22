package com.tipsy.app.orga.entree;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.abhi.barcode.frag.libv2.BarcodeFragment;
import com.abhi.barcode.frag.libv2.IScanResultHandler;
import com.abhi.barcode.frag.libv2.ScanResult;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Bracelet;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by vquefele on 20/01/14.
 */
public class EntreeActivity extends FragmentActivity implements EntreeListener, IScanResultHandler {

    private String eventId;

    /* Fragments */
    private EntreeModelFragment model;
    private EntreeMenuFragment fragMenu;
    private EntreeStatsFragment fragStats;
    private BarcodeFragment fragQRCode;
    private EntreeListeFragment fragListe;
    private EntreeVenteFragment fragVente;
    private EntreeNFCFragment fragScanNFC;
    private EntreeOKFragment fragOK;
    private EntreeKOFragment fragKO;
    private EntreeKOMessageFragment fragKOMessage;

    /* Scan NFC */
    private NfcAdapter adapter;
    private PendingIntent pendingIntent;
    public static final int MODE_ACTIVATION = 0;
    public static final int MODE_CONTROLE = 1;
    private int mode = MODE_CONTROLE;

    /*private EntreeTarifsFragment fragTarifs;
    private EntreeParticipantFragment fragParticipant;*/

    private Achat currentEntree;




    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_entree);
        /* Home Button */
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Entrée");

        /* Ecran constamment allumé */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /* Initialisation écoute NFC */
        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, EntreeActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);


        eventId = getIntent().getStringExtra("EVENT_ID");

        /* Chargement des fragments */
        FragmentManager fm = getSupportFragmentManager();
        fragMenu = (EntreeMenuFragment) fm.findFragmentById(R.id.frag_entree_menu);
        fragOK = (EntreeOKFragment) fm.findFragmentById(R.id.frag_entree_ok);
        fragKO = (EntreeKOFragment) fm.findFragmentById(R.id.frag_entree_ko);
        fragKOMessage = (EntreeKOMessageFragment) fm.findFragmentById(R.id.frag_entree_bracelet_used);
        fragStats = new EntreeStatsFragment();
        fragQRCode = new BarcodeFragment();
        fragListe = new EntreeListeFragment();
        fragVente = new EntreeVenteFragment();
        fragScanNFC = new EntreeNFCFragment();

        /* Association du fragment QRCode avec l'activité */
        fragQRCode.setScanResultHandler(this);

        /* Mise en arrière plan des fragments */
        fragOK.hide();
        fragKO.hide();
        fragKOMessage.hide();

        /* Chargement des données si premier lancement */
        model = (EntreeModelFragment) fm.findFragmentByTag("init");
        if (model == null) {
            loadModel();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.mode_container, fragStats);
            ft.commit();
        }
        /* Ou affichage du dernier mode affiché */
        else {
            eventId = savedInstanceState.getString("EventID");
            if (fragMenu.getCurrentMode() == EntreeMenuFragment.MODE_QRCODE)
                modeQRCode();
            else if (fragMenu.getCurrentMode() == EntreeMenuFragment.MODE_LISTE)
                modeListe();
            else
                modeStats();
        }
    }

    public void loadModel(){
        /* Lancement du fragment d'init */
        model = new EntreeModelFragment();
        Bundle args = new Bundle();
        args.putString("EVENT_ID", eventId);
        model.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        /* Suppression de l'ancien model */
        EntreeModelFragment oldModel = (EntreeModelFragment) getSupportFragmentManager().findFragmentByTag("init");
        if(oldModel != null)
            ft.remove(oldModel);

        ft.add(model, "init");
        ft.commit();
    }

    /* Mise à jour de la barre de progression des stats */
    public void updateProgress(){
        fragStats.updateProgress();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Bracelet bracelet = new Bracelet(tag);
        Iterator it = getEntrees().iterator();
        Achat entree = null;
        boolean found = false;
        while (it.hasNext() && !found) {
            entree = (Achat) it.next();
            if (entree.getParticipant() != null && entree.getParticipant().getBracelet() != null)
                found = entree.getParticipant().getBracelet().equals(bracelet.getTag());
        }

        /* Contrôle d'accès */
        if (mode == MODE_CONTROLE) {
            validateEntree((found ? entree : null));
        }
        /* Vérification que le bracelet n'est pas déjà associé durant cet event */
        else if (mode == MODE_ACTIVATION) {
            /* Bracelet déjà utilisé... */
            if (found) {
                fragKOMessage.show("Bracelet déjà attribué","Veuillez en utiliser un autre");
                return;
            } else {
                try {
                    currentEntree.getParticipant().setBracelet(bracelet.getTag());
                    currentEntree.saveInBackground();
                    setCurrentEntree(null);
                    /* Retour au mode précédent */
                    mode = MODE_CONTROLE; // NFC en écoute globale
                    backToMode();

                }catch(Exception e){
                    Log.d("TOUTAFAIT",e.getMessage());
                    fragKOMessage.show("Cette entrée correspond déjà à un bracelet","");
                }
            }
        }
    }

    /* RESULTAT SCAN QRCODE */
    public void scanResult(ScanResult result) {
        fragQRCode.onDestroy();
        String entreeID = result.getRawResult().getText();

        Iterator it = getEntrees().iterator();
        Achat entree = null;
        boolean found = false;
        while (it.hasNext() && !found) {
            entree = (Achat) it.next();
            if (entree.getObjectId().equals(entreeID)) {
                found = true;
            }
        }
        boolean validate = validateEntree(found ? entree : null);
        /* Activation d'un bracelet si entrée valide */
        if(validate) {
            modeNFC();
            setCurrentEntree(entree);
        }
    }


    public boolean validateEntree(Achat entree){
        boolean validate = false;
        // Bracelet non reconnu
        if (entree == null) {
            fragKO.show(null);
            validate = false;
        }else{
            // Bracelet autorisé
            if (!entree.isUsed()) {
                entree.setUsed(true);
                entree.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        updateProgress();
                    }
                });
                fragOK.show(entree);
                validate = true;
            }
            // Bracelet déjà passé
            else {
                fragKO.show(entree);
                validate = false;
            }
        }
        return validate;
    }

    public Event getEvent(){
        return model.getEvent();
    }
    public ArrayList<Achat> getEntrees(){
        return model.getEntrees();
    }
    public ArrayList<Ticket> getBilletterie(){
        return model.getBilletterie();
    }
    public void setCurrentEntree(Achat entree){
        currentEntree = entree;
    }

    /* AFFICHAGE DU MODE STATS */
    public void modeStats(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.mode_container, fragStats);
        ft.commit();
    }

    /* AFFICHAGE DU MODE QRCODE */
    public void modeQRCode(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.mode_container, fragQRCode);
        ft.commit();
    }

    /* AFFICHAGE DU MODE LISTE */
    public void modeListe(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.mode_container, fragListe);
        ft.commit();
    }

    /* AFFICHAGE DU MODE LISTE */
    public void modeVente(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.mode_container, fragVente);
        ft.commit();
    }

    /* AFFICHAGE DE L'ECOUTE NFC */
    public void modeNFC(){
        mode = MODE_ACTIVATION;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.mode_container, fragScanNFC);
        ft.commit();
    }

    public void setNFCMode(int mode){
        this.mode = mode;
    }

    public void backToMode(){
        if(fragMenu.getCurrentMode() == EntreeMenuFragment.MODE_STATS){
            modeStats();
        }else if(fragMenu.getCurrentMode() == EntreeMenuFragment.MODE_QRCODE){
            modeQRCode();
        }else if(fragMenu.getCurrentMode() == EntreeMenuFragment.MODE_LISTE){
            modeListe();
        }
    }

    /* Retour à l'activity Event */
    public void backToEvent() {
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
        Intent intent = new Intent(this, EventOrgaActivity.class);
        intent.putExtra("EVENT_ID", model.getEvent().getObjectId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putString("EventID", eventId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_access, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Confirmation avant de quitter le mode Entrées
            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        backToEvent();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.setMessage("Vous allez quitter le mode Entrée.");
                builder.show();
                return true;
            case R.id.action_refresh:
                loadModel();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /* Activation de l'écoute NFC */
    @Override
    public void onResume() {
        super.onResume();
        adapter.enableForegroundDispatch(this, pendingIntent, null, null);
        Log.d("TOUTAFAIT", "NFC Actif");
    }

    /* Désactivation de l'écoute NFC */
    @Override
    public void onPause() {
        adapter.disableForegroundDispatch(this);
        Log.d("TOUTAFAIT", "NFC Inactif");
        super.onPause();
    }


}
