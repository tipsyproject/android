package tipsy.commun.billetterie;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by vquefele on 20/01/14.
 */
public class Participant extends StackMobModel implements Parcelable {
    private String email;
    private String nom;
    private String prenom;

    public Participant(){
        super(Participant.class);
    }

    public Participant(String nom, String prenom, String email){
        super(Participant.class);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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
        dest.writeString(nom);
        dest.writeString(prenom);
    }

    public Participant(Parcel in) {
        super(Participant.class);
        setID(in.readString());
        email = in.readString();
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
