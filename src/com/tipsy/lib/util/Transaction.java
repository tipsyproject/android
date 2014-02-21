package com.tipsy.lib.util;

import android.os.Parcelable;

import java.util.Date;

/**
 * Created by valoo on 05/01/14.
 */
public interface Transaction extends Parcelable {
    public Date getCreatedAt();

    public String getDescription();

    public int getDevise();

    public int getMontant();

    public String getTitre();

    public boolean isDepot();
}
