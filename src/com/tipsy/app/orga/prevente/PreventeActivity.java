package com.tipsy.app.orga.prevente;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeActivity;
import com.tipsy.app.orga.entree.EntreeArrayAdapter;
import com.tipsy.app.orga.entree.EntreeListener;
import com.tipsy.app.orga.entree.InitEntreeFragment;
import com.tipsy.app.orga.entree.ModeManuelFragment;
import com.tipsy.app.orga.entree.ModeNFCFragment;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.Achat;
import com.tipsy.lib.Participant;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.Bracelet;
import com.tipsy.lib.util.EventActivity;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vquefele on 20/01/14.
 */
public class PreventeActivity extends FragmentActivity implements PreventeListener {
    private String EVENT_ID;
    Achat prevente;
    ArrayList<Ticket> billetterie;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_prevente);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Mode Prévente");


        if(savedInstanceState == null){
            EVENT_ID = getIntent().getStringExtra("EVENT_ID");
            prevente = new Achat();
            billetterie = getIntent().getParcelableArrayListExtra("Billetterie");

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content, new ScanFragment());
            ft.commit();
        }else{
            prevente = savedInstanceState.getParcelable("Achat");
            EVENT_ID = savedInstanceState.getString("EVENT_ID");
            billetterie = savedInstanceState.getParcelableArrayList("Billetterie");
        }
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
        outState.putParcelable("Achat", prevente);
        outState.putString("EVENT_ID", EVENT_ID);
        outState.putParcelableArrayList("Billetterie", billetterie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Confirmation avant de quitter le mode Entrées
            case android.R.id.home:
                backToEntrees(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onNewIntent(Intent intent) {
        final ProgressDialog wait = ProgressDialog.show(this, "", "Scan en cours...", true, true);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        final String tagID = Bracelet.bytesToHex(tag.getId());
        Participant p = new Participant();
        try{
            Log.d("TOUTAFAIT","set bracelet:"+tagID);
            p.setBracelet(tagID);
            Log.d("TOUTAFAIT","get bracelet:"+p.getBracelet());
        }catch (Exception e){}
        prevente.setParticipant(p);
        wait.dismiss();
        goToTarifs();
    }

    public void goToScan(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new ScanFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void goToTarifs(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new TarifsFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void goToParticipant(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new ParticipantFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void backToEntrees(boolean includePrevente){
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
        Intent intent = new Intent(this, EntreeActivity.class);
        intent.putExtra("EVENT_ID",EVENT_ID);
        if(includePrevente)
            intent.putExtra("PREVENTE",prevente);
        startActivity(intent);
    }

    public Achat getPrevente() {
        return prevente;
    }
    public ArrayList<Ticket> getBilletterie(){
        return billetterie;
    }
}
