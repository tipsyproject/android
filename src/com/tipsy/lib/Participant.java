package com.tipsy.lib;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by valoo on 27/01/14.
 */
@ParseClassName("Participant")
public class Participant extends ParseObject implements Parcelable {

    public Participant() {
    }

    public String getBracelet() {
        return getString("bracelet");
    }

    public void setBracelet(String bracelet) throws Exception {
        if (getBracelet() != null)
            throw new Exception("Un bracelet est déjà attribué.");
        else put("bracelet", bracelet);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public String getNom() {
        return getString("nom");
    }

    public void setNom(String nom) {
        put("nom", nom);
    }

    public String getPrenom() {
        return getString("prenom");
    }

    public void setPrenom(String prenom) {
        put("prenom", prenom);
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeString(getEmail());
        dest.writeString(getNom());
        dest.writeString(getPrenom());
        dest.writeString(getBracelet());
    }

    public Participant(Parcel in) {
        setObjectId(in.readString());
        setEmail(in.readString());
        setNom(in.readString());
        setPrenom(in.readString());
        try {
            setBracelet(in.readString());
        } catch (Exception e) {
        }
    }

    public static final Parcelable.Creator<Participant> CREATOR = new Parcelable.Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel source) {
            return new Participant(source);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };
}
