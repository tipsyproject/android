package com.tipsy.lib;


import android.os.Parcel;
import android.os.Parcelable;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by valoo on 04/01/14.
 */


@ParseClassName("Vestiaire")
public class Vestiaire extends ParseObject implements Parcelable {

    public Vestiaire() {
    }

    public Vestiaire(int number){
        setNumber(number);
    }

    public Event getEvent() {
        return (Event) getParseObject("event");
    }

    public void setEvent(Event event) {
        put("event", event);
    }

    public int getNumber() {
        return getInt("number");
    }

    public void setNumber(int number) {
        put("number", number);
    }

    public Participant getParticipant() {
        return (Participant) getParseObject("participant");
    }

    public void setParticipant(Participant participant) {
        put("participant", participant);
    }


    /* Tri par ordre alphabetique des prénoms */
    public static Comparator<Vestiaire> SORT_BY_FULLNAME = new Comparator<Vestiaire>() {
        public int compare(Vestiaire one, Vestiaire other) {
            // les participants sans nom ni prenom doivent être mis à la fin
            if (one.getParticipant().isAnonymous())
                return 1;
            else return one.getParticipant().getFullName().compareTo(other.getParticipant().getFullName());
        }
    };

    /* Tri par ordre numéro de ticket */
    public static Comparator<Vestiaire> SORT_BY_TICKET = new Comparator<Vestiaire>() {
        public int compare(Vestiaire one, Vestiaire other) {
            return one.getNumber() > other.getNumber() ? 1 : -1;
        }
    };


    public boolean equals(Object o) {
        return (this.getNumber() == ((Vestiaire) o).getNumber());
    }



    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeParcelable(getEvent(), flags);
        dest.writeInt(getNumber());
        dest.writeParcelable(getParticipant(), flags);
    }

    public Vestiaire(Parcel in) {
        setObjectId(in.readString());
        setEvent((Event) in.readParcelable(Event.class.getClassLoader()));
        setNumber(in.readInt());
        setParticipant((Participant) in.readParcelable(Participant.class.getClassLoader()));
    }

    public static final Creator<Vestiaire> CREATOR = new Creator<Vestiaire>() {
        @Override
        public Vestiaire createFromParcel(Parcel source) {
            return new Vestiaire(source);
        }

        @Override
        public Vestiaire[] newArray(int size) {
            return new Vestiaire[size];
        }
    };
}
