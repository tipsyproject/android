package tipsy.commun;

/**
 * Created by valoo on 07/12/13.
 */

import com.stackmob.sdk.model.StackMobModel;


public class Membre extends StackMobModel implements User.TipsyUser {

    private String nom;
    private String prenom;
    private User user;

    public Membre(){
        super(Membre.class);
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

    public int getType() {
        return TypeUser.MEMBRE;
    }

    public User getUser() {
        return user;
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
}
