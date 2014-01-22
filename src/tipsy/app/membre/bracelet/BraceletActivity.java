package tipsy.app.membre.bracelet;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.List;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.membre.MembreActivity;
import tipsy.commun.Bracelet;

/**
 * Created by valoo on 22/01/14.
 */
public class BraceletActivity extends Activity {

    NfcAdapter adapter;
    PendingIntent pendingIntent;
    ProgressDialog scanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    protected void onNewIntent(Intent intent){
        scanning.dismiss();

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagID = Bracelet.bytesToHex(tag.getId());

        Bracelet.query(Bracelet.class, new StackMobQuery().fieldIsEqualTo("tagid",tagID),
                new StackMobQueryCallback<Bracelet>() {
                    @Override
                    public void success(List<Bracelet> bracelets) {
                        Bracelet bracelet = bracelets.get(0);

                        if(bracelet.isFree()){
                            Log.d("TOUTAFAIT","bracelet dispo");
                            TipsyApp app = (TipsyApp) getApplication();
                            bracelet.setMembre(app.getMembre());
                            bracelet.save(new StackMobModelCallback() {
                                @Override
                                public void success() {
                                    Log.d("TOUTAFAIT","save bracelet");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(BraceletActivity.this, "Bracelet enregistré !", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void failure(StackMobException e) {
                                    Log.d("TOUTAFAIT","erreur save bracelet:"+e.getMessage());
                                }
                            });
                        }else{
                            Log.d("TOUTAFAIT","bracelet utilisé");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BraceletActivity.this, "Bracelet déjà utilisé !", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }

                    @Override
                    public void failure(StackMobException e) {
                        Log.d("TOUTAFAIT","erreur query bracelet:"+e.getMessage());

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
