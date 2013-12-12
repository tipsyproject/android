package tipsy.commun;

/**
 * Created by valoo on 07/12/13.
 */

import java.util.Date;


public class Membre extends User {

    private String prenom;

    public Membre(String username, String password){
        super(username,password);
        this.type = TypeUser.MEMBRE;
    }

    public Membre(String username, String password, String nom, String prenom){
        super(username,password,nom);
        this.prenom = prenom;
    }
}
