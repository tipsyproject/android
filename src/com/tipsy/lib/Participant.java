package com.tipsy.lib;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by vquefele on 20/01/14.
 */
public class Participant extends StackMobModel implements Parcelable {
    private String email;
    private Event_old eventOld;
    private Membre membre = null;
    private String nom;
    private String prenom;

    public Participant(){
        super(Participant.class);
    }

    public Participant(Event_old e){
        super(Participant.class);
        eventOld = e;
    }

    public String getEmail() {
        return membre == null ? email : membre.getEmail();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Event_old getEventOld() {
        return eventOld;
    }

    public void setEventOld(Event_old eventOld) {
        this.eventOld = eventOld;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.nom = null;
        this.prenom = null;
        this.email = null;
        this.membre = membre;
    }

    public String getNom() {
        return membre == null ? nom : membre.getNom();
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return membre == null ? prenom : membre.getPrenom();
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    public boolean isDefined(){
        return membre != null || (nom != null && prenom != null && email != null);
    }

    public boolean isMembre(){
        return membre != null;
    }

    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getID());
        dest.writeString(email);
        dest.writeParcelable(eventOld, flags);
        dest.writeParcelable(membre,flags);
        dest.writeString(nom);
        dest.writeString(prenom);
    }

    public Participant(Parcel in) {
        super(Participant.class);
        setID(in.readString());
        email = in.readString();
        eventOld = in.readParcelable(Event_old.class.getClassLoader());
        membre = in.readParcelable(Membre.class.getClassLoader());
        nom = in.readString();
        prenom = in.readString();
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
