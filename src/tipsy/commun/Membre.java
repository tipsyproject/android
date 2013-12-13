package tipsy.commun;

/**
 * Created by valoo on 07/12/13.
 */

import com.stackmob.sdk.model.StackMobModel;

public class Membre extends StackMobModel {

    private String nom;
    private String prenom;
    private User user;


    /*
    public Membre(String username, String password){
        super(Membre.class);
        user = new User(username,password,TypeUser.MEMBRE);
    }*/

    public Membre(String username, String password, String nom, String prenom){
        super(Membre.class);
        user = new User(username,password,TypeUser.MEMBRE);
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getEmail() { return user.getEmail(); }

    public User getUser() { return user; }
}
