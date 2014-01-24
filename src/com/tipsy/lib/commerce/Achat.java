package com.tipsy.lib.commerce;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

import com.tipsy.lib.Participant;

/**
 * Created by Valentin on 21/01/14.
 */
@ParseClassName("achat")
public class Achat extends ParseObject implements Transaction, Parcelable {

    public Achat(){}

    public Date getDate() {
        return getDate("date");
    }

    public void setDate(Date date) {
        put("date",date);
    }

    public int getDevise(){
        return ((Produit) getParseObject("produit")).getDevise();
    }

    public Participant getParticipant() {
        return (Participant) getParseObject("participant");
    }

    public void setParticipant(Participant participant) {
        put("participant",participant);
    }

    public String getPayeur() {
        return getString("payeur");
    }

    public void setPayeur(String payeur) {
        put("payeur",payeur);
    }

    public Produit getProduit() {
        return (Produit) getParseObject("produit");
    }

    public void setProduit(Produit produit) {
        put("produit",produit);
    }

    public int getMontant(){
        return ((Produit) getParseObject("produit")).getPrix();
    }

    public String getDescription(){
        return getParticipant().getEvent().getNom() + " - " + getParticipant().getEvent().getLieu();
    }

    public String getTitre(){
        return ((Produit) getParseObject("produit")).getNom();
    }

    public boolean isDepot(){
        return false;
    }

    public boolean isParticipantDefined(){
        return getParticipant().isDefined();
    }



    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeSerializable(getDate());
        dest.writeParcelable(getParticipant(), flags);
        dest.writeString(getPayeur());
        dest.writeParcelable(getProduit(), flags);
    }

    public Achat(Parcel in) {
        setObjectId(in.readString());
        setDate((Date) in.readSerializable());
        setParticipant((Participant) in.readParcelable(Participant.class.getClassLoader()));
        setPayeur(in.readString());
        setProduit((Produit) in.readParcelable(Produit.class.getClassLoader()));
    }

    public static final Parcelable.Creator<Achat> CREATOR = new Parcelable.Creator<Achat>() {
        @Override
        public Achat createFromParcel(Parcel source) {
            return new Achat(source);
        }

        @Override
        public Achat[] newArray(int size) {
            return new Achat[size];
        }
    };

}
