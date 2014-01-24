package com.tipsy.lib.commerce;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

import java.util.Date;

import com.tipsy.lib.Participant;

/**
 * Created by Valentin on 21/01/14.
 */
public class Achat extends StackMobModel implements Transaction, Parcelable {

    private Date date;
    private Participant participant;
    private String payeur;
    private Produit produit;

    public Achat(){
        super(Achat.class);
    }

    public Achat(Produit p){
        super(Achat.class);
        date = new Date();
        produit = p;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDevise(){
        return produit.getDevise();
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public String getPayeur() {
        return payeur;
    }

    public void setPayeur(String payeur) {
        this.payeur = payeur;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getMontant(){
        return produit.getPrix();
    }

    public String getDescription(){
        return participant.getEventOld().getNom() + " - " + participant.getEventOld().getLieu();
    }

    public String getTitre(){
        return produit.getNom();
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
        dest.writeString(getID());
        dest.writeSerializable(date);
        dest.writeParcelable(participant, flags);
        dest.writeString(payeur);
        dest.writeParcelable(produit, flags);
    }

    public Achat(Parcel in) {
        super(Achat.class);
        setID(in.readString());
        date = (Date) in.readSerializable();
        participant = in.readParcelable(Participant.class.getClassLoader());
        payeur = in.readString();
        produit = in.readParcelable(Produit.class.getClassLoader());
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
