package tipsy.commun.billetterie;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

import tipsy.commun.Event;
import tipsy.commun.Membre;
import tipsy.commun.commerce.Produit;

/**
 * Created by vquefele on 20/01/14.
 */
public class Participation extends StackMobModel  implements Parcelable {

    private Participant participant = null;
    private Membre proprietaire = null;
    private Produit billet;
    private Event event;

    public Participation(){
        super(Participation.class);
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Produit getBillet() {
        return billet;
    }

    public void setBillet(Produit billet) {
        this.billet = billet;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Membre getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Membre proprietaire) {
        this.proprietaire = proprietaire;
    }

    public void setProprietaireParticipant(){
        this.participant = new Participant(proprietaire.getNom(),proprietaire.getPrenom(),proprietaire.getEmail());
    }

    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getID());
        dest.writeParcelable(participant,flags);
        dest.writeParcelable(proprietaire,flags);
        dest.writeParcelable(billet,flags);
        dest.writeParcelable(event,flags);
    }

    public Participation(Parcel in) {
        super(Participation.class);
        setID(in.readString());
        participant = in.readParcelable(Participant.class.getClassLoader());
        proprietaire = in.readParcelable(Membre.class.getClassLoader());
        billet = in.readParcelable(Billet.class.getClassLoader());
        event = in.readParcelable(Event.class.getClassLoader());
    }

    public static final Parcelable.Creator<Participation> CREATOR = new Parcelable.Creator<Participation>() {
        @Override
        public Participation createFromParcel(Parcel source) {
            return new Participation(source);
        }

        @Override
        public Participation[] newArray(int size) {
            return new Participation[size];
        }
    };
}
