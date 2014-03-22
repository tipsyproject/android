package com.tipsy.lib;


import android.os.Parcel;
import android.os.Parcelable;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by valoo on 04/01/14.
 */


@ParseClassName("Ticket")
public class Ticket extends ParseObject implements Parcelable {

    public static int BILLET = 0;
    public static int VESTIAIRE = 1;
    public static int CONSO = 2;

    public Ticket() {
    }


    public int getDevise() {
        return getInt("devise");
    }

    public void setDevise(int devise) {
        put("devise", devise);
    }

    public Event getEvent() {
        return (Event) getParseObject("event");
    }

    public void setEvent(Event event) {
        put("event", event);
    }

    public String getNom() {
        return getString("nom");
    }

    public void setNom(String nom) {
        put("nom", nom);
    }


    public int getPrix() {
        return getInt("prix");
    }

    public void setPrix(int prix) {
        if (prix < 0) throw new ArithmeticException("Le prix d'un ticket ne peut être négatif.");
        else put("prix", prix);
    }

    public int getType() {
        return getInt("type");
    }

    public void setType(int type) {
        put("type", type);
    }


    @Override
    public boolean equals(Object o) {
        return (this.getObjectId() == ((Ticket) o).getObjectId());
    }


    public static void loadVentes(ArrayList<Ticket> tickets, FindCallback<Achat> callback) {
        // Seulement si des billets sont définis
        if (!tickets.isEmpty()) {
            ParseQuery<Achat> query = ParseQuery.getQuery(Achat.class);
            query.include("participant");
            query.include("paiementuser");
            query.include("user");
            query.include("ticket.event");
            query.whereContainedIn("ticket", tickets);
            query.findInBackground(callback);
        } else callback.done(new ArrayList<Achat>(), null);
    }

    public static void loadParticipants(final String tagID, FindCallback<Participant> callback) {
            ParseQuery<Participant> query = ParseQuery.getQuery(Participant.class);
            query.whereEqualTo("bracelet",tagID);
            query.findInBackground(callback);
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeInt(getDevise());
        dest.writeParcelable(getEvent(), flags);
        dest.writeString(getNom());
        dest.writeInt(getPrix());
        dest.writeInt(getType());
    }

    public Ticket(Parcel in) {
        setObjectId(in.readString());
        setDevise(in.readInt());
        setEvent((Event) in.readParcelable(Event.class.getClassLoader()));
        setNom(in.readString());
        setPrix(in.readInt());
        setType(in.readInt());
    }

    public static final Parcelable.Creator<Ticket> CREATOR = new Parcelable.Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel source) {
            return new Ticket(source);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };
}
