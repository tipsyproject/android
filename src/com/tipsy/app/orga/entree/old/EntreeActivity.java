package com.tipsy.app.orga.entree.old;


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

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.entree.old.liste.ModeListeFragment;
import com.tipsy.app.orga.entree.old.qrcode.ModeQRCodeFragment;
import com.tipsy.app.orga.entree.old.stats.ModeStatsFragment;
import com.tipsy.app.orga.entree.old.vente.ModeVenteFragment;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Event;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Bracelet;

import java.util.ArrayList;

/**
 * Created by vquefele on 20/01/14.
 */
public class EntreeActivity extends FragmentActivity implements EntreeListener {

    private String eventId;

    /* Fragments */
    private EntreeModelFragment model;
    private EntreeMenuFragment fragMenu;
    private ModeStatsFragment fragStats;
    private ModeQRCodeFragment fragQRCode;
    private ModeListeFragment fragListe;
    private ModeVenteFragment fragVente;
    private EntreeNFCFragment fragScanNFC;
    private KOFragment fragKO;
    private OKFragment fragOK;

    /* Scan NFC */
    private NfcAdapter adapter;
    private PendingIntent pendingIntent;
    private NFCCallback nfcCallback;

    /*
    public static final int MODE_ACTIVATION = 0;
    public static final int MODE_CONTROLE = 1;
    private int mode = MODE_CONTROLE;
    */

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
        fragOK = (OKFragment) fm.findFragmentById(R.id.frag_ok);
        fragKO = (KOFragment) fm.findFragmentById(R.id.frag_ko);
        fragStats = new ModeStatsFragment();
        fragQRCode = new ModeQRCodeFragment();
        fragListe = new ModeListeFragment();
        fragVente = new ModeVenteFragment();
        fragScanNFC = new EntreeNFCFragment();


        /* Mise en arrière plan des fragments */
        fragOK.hide();
        fragKO.hide();

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
            else if (fragMenu.getCurrentMode() == EntreeMenuFragment.MODE_VENTE)
                modeVente();
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

        /* Bracelet utilisé pour une autre action que le controle d'accès */
        if(nfcCallback != null){
            nfcCallback.onScan(bracelet);
        }
        /* CONTROLE D'ACCES */
        else{
            int found = findBracelet(bracelet);
            /* Bracelet inconnu */
            if(found == -1){
                fragKO.show("Entrée non autorisée", "Bracelet inconnu");
            }else{
                Achat entree = getEntrees().get(found);
                // Bracelet autorisé
                if (!entree.isUsed()) {
                    entree.setUsed(true);
                    entree.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            updateProgress();
                        }
                    });
                    fragOK.show(entree.getTicket().getNom(),entree.getPrenom() + " " + entree.getNom());
                }
                // Bracelet déjà passé
                else {
                    fragKO.show("Entrée déjà validée",entree.getPrenom() + " " + entree.getNom());
                }
            }
        }
    }


    /* Retourne l'index de l'entree correspondante au bracelet dans la liste des entrees
    *  Retourne -1 si le bracelet n'est associé à aucune entree  */
    public int findBracelet(Bracelet bracelet){
        Achat entree = null;
        for(int i=0; i<getEntrees().size(); ++i){
            entree = getEntrees().get(i);
            if (entree.getParticipant() != null && entree.getParticipant().getBracelet() != null)
                if(entree.getParticipant().getBracelet().equals(bracelet.getTag()))
                    return i;
        }
        return -1;
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
    public EntreeNFCFragment getFragNFC(){
        return this.fragScanNFC;
    }


    public void activationManuelle(final Achat entree){
        fragListe.activationManuelle(entree);
    }

    public void setTarifVente(Ticket t){
        fragVente.setTarifVente(t);
    }

    public void setParticipantInfos(Participant p){
        fragVente.setParticipantInfos(p);
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

    public void KO(String m1, String m2){
        fragKO.show(m1,m2);
    }

    public void OK(String m1, String m2){
        fragOK.show(m1,m2);
    }

    public void setNFCCallback(NFCCallback cb){
        nfcCallback = cb;
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
