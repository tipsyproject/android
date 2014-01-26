package com.tipsy.app.membre.bracelet;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.List;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.app.membre.MembreActivity;
import com.tipsy.lib.Bracelet;
import com.tipsy.lib.TipsyUser;

/**
 * Created by valoo on 22/01/14.
 */
public class BraceletActivity extends Activity {

    NfcAdapter adapter;
    PendingIntent pendingIntent;
    ProgressDialog scanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_bracelet);
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Mon Bracelet");

        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        Button buttonNFC = (Button) findViewById(R.id.button_nfc);
        buttonNFC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scanning = ProgressDialog.show(BraceletActivity.this,"","Approchez le bracelet de l'arrière du téléphone",true,true);
                scanning.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        adapter.disableForegroundDispatch(BraceletActivity.this);
                    }
                });
                scanning.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        adapter.disableForegroundDispatch(BraceletActivity.this);
                    }
                });
                adapter.enableForegroundDispatch(BraceletActivity.this, pendingIntent, null, null);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
    }

    @Override
    protected void onNewIntent(Intent intent){
        scanning.dismiss();

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagID = Bracelet.bytesToHex(tag.getId());

        ParseQuery<Bracelet> query = ParseQuery.getQuery(Bracelet.class);
        query.include("user");
        query.include("participant");
        final ProgressDialog wait = ProgressDialog.show(this,"","Association bracelet",true,false);
        query.getInBackground(tagID,new GetCallback<Bracelet>() {
            @Override
            public void done(Bracelet bracelet, ParseException e) {
                /* BRACELET REPERTORIE */
                if(bracelet != null){

                    if(bracelet.isFree()){
                        bracelet.setUser(TipsyUser.getCurrentUser());
                        bracelet.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                wait.dismiss();
                                if(e == null)
                                    Toast.makeText(BraceletActivity.this, "Bracelet enregistré !", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(BraceletActivity.this, "Erreur enregistrement bracelet", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else{
                        wait.dismiss();
                        Toast.makeText(BraceletActivity.this, "Bracelet déjà utilisé !", Toast.LENGTH_SHORT).show();
                    }
                }else{ /* BRACELET INCONNU */
                    wait.dismiss();
                    Log.d("TOUTAFAIT", "bracelet inconnu");
                    Toast.makeText(BraceletActivity.this, "Bracelet inconnu !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                backToMembre();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void backToMembre(){
        Intent intent = new Intent(this, MembreActivity.class);
        startActivity(intent);
    }
}
