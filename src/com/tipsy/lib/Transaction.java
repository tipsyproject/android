package com.tipsy.lib;

import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

import java.util.Date;

/**
 * Created by valoo on 05/01/14.
 */
public interface Transaction extends Parcelable{
    public Date getDate();
    public String getDescription();
    public int getDevise();
    public int getMontant();
    public String getTitre();
    public boolean isDepot();
}
