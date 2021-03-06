package com.tipsy.lib;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseUser;

/**
 * Created by vquefele on 23/01/14.
 */
public class TipsyUser extends ParseUser implements Parcelable {
    public static int MEMBRE = 1;
    public static int ORGA = 2;

    public TipsyUser() {
    }

    public String getBracelet() {
        return getString("bracelet");
    }

    public void setBracelet(String bracelet) throws Exception {
        if (getBracelet() != null)
            throw new Exception("Un bracelet est déjà attribué.");
        else put("bracelet", bracelet);
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
        super.setEmail(username);
    }

    @Override
    public String getEmail() {
        return getUsername();
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

    public int getType() {
        return getInt("type");
    }

    public void setType(int type) {
        put("type", type);
    }

    public static TipsyUser getCurrentUser() {
        return (TipsyUser) ParseUser.getCurrentUser();
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeString(getUsername());
        dest.writeString(getNom());
        dest.writeString(getPrenom());
        dest.writeInt(getType());
    }

    public TipsyUser(Parcel in) {
        setObjectId(in.readString());
        setUsername(in.readString());
        setNom(in.readString());
        setPrenom(in.readString());
        setType(in.readInt());
    }

    public static final Parcelable.Creator<TipsyUser> CREATOR = new Parcelable.Creator<TipsyUser>() {
        @Override
        public TipsyUser createFromParcel(Parcel source) {
            return new TipsyUser(source);
        }

        @Override
        public TipsyUser[] newArray(int size) {
            return new TipsyUser[size];
        }
    };
}
