

/**
 * Created by Valentin on 07/12/13.
 */


package com.tipsy.lib;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

import java.util.ArrayList;
import java.util.Date;

import com.tipsy.lib.billetterie.Billet;

/**
 * Created by Valentin on 07/12/13.
 */
public class Event_old extends StackMobModel implements Parcelable {
    private ArrayList<Billet> billetterie = new ArrayList<Billet>();
    private Date debut = new Date();
    private String lieu = null;
    private String nom = null;
    private String organisateur = null;

    public Event_old() {
        super(Event_old.class);
    }

    public Event_old(String nom) {
        super(Event_old.class);
        this.nom = nom;
    }

    public ArrayList<Billet> getBilletterie() {
        return billetterie;
    }

    public void setBilletterie(ArrayList<Billet> billets) {
        this.billetterie.clear();
        this.billetterie.addAll(billets);
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getOrganisateur() {
        return organisateur;
    }

    public void setOrganisateur(String organisateur) {
        this.organisateur = organisateur;
    }

    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getID());
        dest.writeList(billetterie);
        dest.writeSerializable(debut);
        dest.writeString(lieu);
        dest.writeString(nom);
        dest.writeString(organisateur);
    }

    public Event_old(Parcel in) {
        super(Event_old.class);
        setID(in.readString());
        in.readList(billetterie, Billet.class.getClassLoader());
        debut = (Date) in.readSerializable();
        lieu = in.readString();
        nom = in.readString();
        organisateur = in.readString();
    }

    public static final Parcelable.Creator<Event_old> CREATOR = new Parcelable.Creator<Event_old>() {
        @Override
        public Event_old createFromParcel(Parcel source) {
            return new Event_old(source);
        }

        @Override
        public Event_old[] newArray(int size) {
            return new Event_old[size];
        }
    };

}


