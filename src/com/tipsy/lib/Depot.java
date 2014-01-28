package com.tipsy.lib;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.tipsy.lib.util.Transaction;

/**
 * Created by Valentin on 21/01/14.
 */
@ParseClassName("Depot")
public class Depot extends ParseObject implements Transaction {

    public Depot(){}

    public Depot(int montant, TipsyUser user, int devise){
        setDevise(devise);
        setMontant(montant);
        setUser(user);
    }

    public Depot(int montant, Participant participant, int devise){
        setDevise(devise);
        setMontant(montant);
        setParticipant(participant);
    }

    public int getDevise() {
        return getInt("devise");
    }

    public void setDevise(int devise) {
        put("devise",devise);
    }

    public int getMontant() {
        return getInt("montant");
    }

    public void setMontant(int montant) {
        put("montant",montant);
    }

    public Participant getParticipant() {
        return (Participant) getParseObject("participant");
    }

    public void setParticipant(Participant participant) {
        if(participant == null)
            remove("participant");
        else{
            remove("user");
            put("participant", participant);
        }
    }

    public TipsyUser getUser() {
        return (TipsyUser) getParseObject("user");
    }

    public void setUser(TipsyUser user) {
        if(user == null)
            remove("user");
        else{
            remove("participant");
            put("user",user);
        }
    }

    public String getDescription(){
        return "";
    }

    public String getTitre(){
        return "Rechargement";
    }

    public boolean isDepot(){
        return true;
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
        dest.writeInt(getMontant());
        dest.writeParcelable(getUser(),flags);
    }

    public Depot(Parcel in) {
        setObjectId(in.readString());
        setDevise(in.readInt());
        setMontant(in.readInt());
        setUser((TipsyUser) in.readParcelable(TipsyUser.class.getClassLoader()));
    }

    public static final Parcelable.Creator<Depot> CREATOR = new Parcelable.Creator<Depot>() {
        @Override
        public Depot createFromParcel(Parcel source) {
            return new Depot(source);
        }

        @Override
        public Depot[] newArray(int size) {
            return new Depot[size];
        }
    };
}
