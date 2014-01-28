package com.tipsy.lib;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by valoo on 24/01/14.
 */

@ParseClassName("Event")
public class Event extends ParseObject implements Parcelable {

    public Event() {
    }

    public Date getDebut() {
        return getDate("debut");
    }

    public void setDebut(Date debut) {
        put("debut", debut);
    }

    public String getLieu() {
        return getString("lieu");
    }

    public void setLieu(String lieu) {
        put("lieu", lieu);
    }

    public String getNom() {
        return getString("nom");
    }

    public void setNom(String nom) {
        put("nom", nom);
    }

    public String getOrganisateur() {
        return getString("organisateur");
    }

    public void setOrganisateur(String organisateur) {
        put("organisateur", organisateur);
    }

    public void findBilletterie(FindCallback cb) {
        ParseQuery<Ticket> query = ParseQuery.getQuery(Ticket.class);
        query.include("event");
        query.whereEqualTo("event", this);
        query.whereEqualTo("type", Ticket.BILLET);
        query.findInBackground(cb);
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeSerializable(getDebut());
        dest.writeString(getLieu());
        dest.writeString(getNom());
        dest.writeString(getOrganisateur());
    }

    public Event(Parcel in) {
        setObjectId(in.readString());
        setDebut((Date) in.readSerializable());
        setLieu(in.readString());
        setNom(in.readString());
        setOrganisateur(in.readString());
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
    };
}
