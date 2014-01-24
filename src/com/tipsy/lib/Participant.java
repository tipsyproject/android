package com.tipsy.lib;


import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by vquefele on 20/01/14.
 */
@ParseClassName("participant")
public class Participant extends ParseObject implements Parcelable{

    public Participant(){}

    public String getEmail() {
        return getUser() == null ? getString("email") : getUser().getEmail();
    }

    public void setEmail(String email) {
        put("email",email);
    }

    public Event getEvent() {
        return (Event) getParseObject("event");
    }

    public void setEvent(Event event) {
        put("event",event);
    }

    public TipsyUser getUser() {
        return (TipsyUser) getParseObject("user");
    }

    public void setUser(TipsyUser user) {
        put("nom",null);
        put("prenom",null);
        put("email",null);
        put("user",user);
    }

    public String getNom() {
        return getUser() == null ? getString("nom") : getUser().getNom();
    }

    public void setNom(String nom) {
        put("nom",nom);
    }

    public String getPrenom() {
        return getUser() == null ? getString("prenom") : getUser().getPrenom();
    }

    public void setPrenom(String prenom) {
        put("prenom",prenom);
    }


    public boolean isDefined(){
        return getUser() != null || (getNom() != null && getPrenom() != null && getEmail() != null);
    }

    public boolean isMembre(){
        return getUser() != null;
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
        dest.writeParcelable(getEvent(), flags);
        dest.writeParcelable(getUser(),flags);
        dest.writeString(getNom());
        dest.writeString(getPrenom());
    }

    public Participant(Parcel in) {
        setObjectId(in.readString());
        setEmail(in.readString());
        setEvent((Event) in.readParcelable(Event.class.getClassLoader()));
        setUser((TipsyUser) in.readParcelable(TipsyUser.class.getClassLoader()));
        setNom(in.readString());
        setPrenom(in.readString());
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
