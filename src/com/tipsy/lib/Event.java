package com.tipsy.lib;

import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by valoo on 24/01/14.
 */

@ParseClassName("Event")
public class Event extends ParseObject {

    public Event() {}

    public Date getDebut() {
        return getDate("debut");
    }

    public void setDebut(Date debut) {
        put("debut",debut);
    }

    public String getLieu() {
        return getString("lieu");
    }

    public void setLieu(String lieu) {
        put("lieu",lieu);
    }

    public String getNom() {
        return getString("nom");
    }

    public void setNom(String nom) {
        put("nom",nom);
    }

    public String getOrganisateur() {
        return getString("organisateur");
    }

    public void setOrganisateur(String organisateur) {
        put("organisateur",organisateur);
    }

    /*
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

    public Event(Parcel in) {
        super(Event.class);
        setID(in.readString());
        in.readList(billetterie, Billet.class.getClassLoader());
        debut = (Date) in.readSerializable();
        lieu = in.readString();
        nom = in.readString();
        organisateur = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };*/

}
