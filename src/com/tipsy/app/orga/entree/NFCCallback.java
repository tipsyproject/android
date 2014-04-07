package com.tipsy.app.orga.entree;

import android.util.Log;

import com.tipsy.lib.util.Bracelet;

/**
 * Created by valoo on 22/03/14.
 */
public abstract class NFCCallback {
    public final static NFCCallback EMPTY = new NFCCallback() {
        @Override
        public void onScan(Bracelet bracelet) {

        }
    };
    public abstract void onScan(Bracelet bracelet);
}
