package tipsy.commun;

/**
 * Created by valoo on 07/12/13.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.api.StackMobFile;
import com.stackmob.sdk.model.StackMobModel;


public class Membre extends StackMobModel implements User.TipsyUser, Parcelable {

    private StackMobFile avatar = null;
    private String nom;
    private String prenom;
    private User user;

    public Membre() {
        super(Membre.class);
    }

    public Membre(String username) {
        super(Membre.class);
        user = new User(username, TypeUser.MEMBRE);
    }

    public Membre(String username, String nom, String prenom) {
        super(Membre.class);
        user = new User(username, TypeUser.MEMBRE);
        this.nom = nom;
        this.prenom = prenom;
    }

    public Membre(String username, String password, String nom, String prenom) {
        super(Membre.class);
        user = new User(username, password, TypeUser.MEMBRE);
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public StackMobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(StackMobFile avatar) {
        this.avatar = avatar;
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

    public int getType() {
        return TypeUser.MEMBRE;
    }

    public User getUser() {
        return user;
    }



    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getID());
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeParcelable(user,flags);
    }

    public Membre(Parcel in) {
        super(Membre.class);
        setID(in.readString());
        nom = in.readString();
        prenom = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<Membre> CREATOR = new Parcelable.Creator<Membre>() {
        @Override
        public Membre createFromParcel(Parcel source) {
            return new Membre(source);
        }

        @Override
        public Membre[] newArray(int size) {
            return new Membre[size];
        }
    };

}
