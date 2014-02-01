package com.tipsy.app.orga.acces;

import android.nfc.NfcAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tipsy.app.orga.billetterie.EntreeArrayAdapter;
import com.tipsy.lib.Achat;
import com.tipsy.lib.util.EventModule;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;

/**
 * Created by vquefele on 20/01/14.
 */
public interface AccesListener extends EventModule {
    public ArrayList<Achat> getEntrees();
    public EntreeArrayAdapter getEntreesAdapter();
    public ProgressBar getProgressBar();
    public void setProgressBar(ProgressBar pb);
    public TextView getProgressText();
    public void setProgressText(TextView tv);
    public void updateProgress();
    public void goToNFC(boolean addTobackStack);
    public void goToManualAccess();
    public void loadVentes(QueryCallback cb);
}
