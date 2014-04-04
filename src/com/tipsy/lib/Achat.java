package com.tipsy.lib;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.tipsy.lib.util.Transaction;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Valentin on 21/01/14.
 */
@ParseClassName("Achat")
public class Achat extends ParseObject implements Transaction {

    public Achat() {
    }

    public Achat(Ticket t) {
        setTicket(t);
        setUsed(false);
    }

    public int getDevise() {
        return getTicket().getDevise();
    }

    public Participant getParticipant() {
        return (Participant) getParseObject("participant");
    }

    public void setParticipant(Participant participant) {
        if (participant == null)
            remove("participant");
        else {
            remove("user");
            put("participant", participant);
        }
    }

    public TipsyUser getPaiementUser() {
        return (TipsyUser) getParseObject("paiementuser");
    }

    public void setPaiementUser(TipsyUser payeur) {
        if (payeur != null) put("paiementuser", payeur);
    }

    public Participant getPaiementParticipant() {
        return (Participant) getParseObject("paiementparticipant");
    }

    public void setPaiementParticipant(Participant payeur) {
        if (payeur != null) put("paiementparticipant", payeur);
    }

    public Ticket getTicket() {
        return (Ticket) getParseObject("ticket");
    }

    public void setTicket(Ticket ticket) {
        put("ticket", ticket);
    }

    public TipsyUser getUser() {
        return (TipsyUser) getParseObject("user");
    }

    public void setUser(TipsyUser user) {
        if (user == null)
            remove("user");
        else {
            remove("participant");
            put("user", user);
        }
    }

    public boolean isUsed() {
        return getBoolean("used");
    }

    public void setUsed(boolean used) {
        put("used", used);
    }

    public boolean isWeb() {
        return getBoolean("web");
    }

    public void setWeb(boolean web) {
        put("web", web);
    }

    public int getMontant() {
        return getTicket().getPrix();
    }

    public String getDescription() {
        return getTicket().getEvent().getNom() + " - " + getTicket().getEvent().getLieu();
    }

    public String getTitre() {
        return getTicket().getNom();
    }

    public boolean isDepot() {
        return false;
    }

    public boolean isTipsyUser() {
        return getUser() != null;
    }



    public static int nombreVenteEnLigne(ArrayList<Achat> achats){
        int enligne = 0;
        for(Achat a: achats)
            if(a.isWeb())
                enligne++;
        return enligne;
    }

    /* Tri par ordre alphabetique des prénoms */
    public static Comparator<Achat> SORT_BY_FULLNAME = new Comparator<Achat>() {
        public int compare(Achat one, Achat other) {
            // les participants sans nom ni prenom doivent être mis à la fin
            if (one.getParticipant().isAnonymous())
                if(other.getParticipant().isAnonymous())
                    return 0;
                else
                    return 1;
            else return one.getParticipant().getFullName().compareTo(other.getParticipant().getFullName());
        }
    };


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeParcelable(getUser(), flags);
        dest.writeParcelable(getParticipant(), flags);
        dest.writeParcelable(getPaiementUser(), flags);
        dest.writeParcelable(getPaiementParticipant(), flags);
        dest.writeParcelable(getTicket(), flags);
        dest.writeValue(isUsed());
        dest.writeValue(isWeb());
    }

    public Achat(Parcel in) {
        setObjectId(in.readString());
        setUser((TipsyUser) in.readParcelable(TipsyUser.class.getClassLoader()));
        setParticipant((Participant) in.readParcelable(Participant.class.getClassLoader()));
        setPaiementUser((TipsyUser) in.readParcelable(TipsyUser.class.getClassLoader()));
        setPaiementParticipant((Participant) in.readParcelable(Participant.class.getClassLoader()));
        setTicket((Ticket) in.readParcelable(Ticket.class.getClassLoader()));
        setUsed((Boolean) in.readValue(Boolean.class.getClassLoader()));
        setWeb((Boolean) in.readValue(Boolean.class.getClassLoader()));
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
