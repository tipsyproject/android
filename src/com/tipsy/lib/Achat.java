package com.tipsy.lib;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Valentin on 21/01/14.
 */
@ParseClassName("Achat")
public class Achat extends ParseObject implements Transaction {

    public Achat() {
    }

    public Achat(Ticket p) {
        setDate(new Date());
        setProduit(p);
    }

    public Date getDate() {
        return getDate("date");
    }

    public void setDate(Date date) {
        put("date", date);
    }

    public int getDevise() {
        return ((Ticket) getParseObject("produit")).getDevise();
    }

    public Participant getParticipant() {
        return (Participant) getParseObject("participant");
    }

    public void setParticipant(Participant participant) {
        put("participant", participant);
    }

    public TipsyUser getPayeur() {
        return (TipsyUser) getParseObject("payeur");
    }

    public void setPayeur(TipsyUser payeur) {
        put("payeur", payeur);
    }

    public Ticket getProduit() {
        return (Ticket) getParseObject("produit");
    }

    public void setProduit(Ticket ticket) {
        put("ticket", ticket);
    }

    public boolean isUsed() {
        return getBoolean("used");
    }

    public void setUsed(boolean used) {
        put("used", used);
    }

    public int getMontant() {
        return ((Ticket) getParseObject("produit")).getPrix();
    }

    public String getDescription() {
        return getParticipant().getEvent().getNom() + " - " + getParticipant().getEvent().getLieu();
    }

    public String getTitre() {
        return ((Ticket) getParseObject("produit")).getNom();
    }

    public boolean isDepot() {
        return false;
    }

    public boolean isParticipantDefined() {
        return getParticipant().isDefined();
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeSerializable(getDate());
        dest.writeParcelable(getParticipant(), flags);
        dest.writeParcelable(getPayeur(), flags);
        dest.writeParcelable(getProduit(), flags);
        dest.writeValue(isUsed());
    }

    public Achat(Parcel in) {
        setObjectId(in.readString());
        setDate((Date) in.readSerializable());
        setParticipant((Participant) in.readParcelable(Participant.class.getClassLoader()));
        setPayeur((TipsyUser) in.readParcelable(TipsyUser.class.getClassLoader()));
        setProduit((Ticket) in.readParcelable(Ticket.class.getClassLoader()));
        setUsed((Boolean) in.readValue(Boolean.class.getClassLoader()));
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
