package com.tipsy.app.orga.entree;

import com.tipsy.lib.util.Bracelet;

/**
 * Created by valoo on 22/03/14.
 */
public abstract class NFCCallback {
    abstract void onScan(Bracelet b);
}
