package tipsy.commun;

/**
 * Created by valoo on 07/12/13.
 */

import java.util.Date;

public class Membre extends User {

    private String password;
    private Date naissance;

    public Membre(String username, String password){
        super(username,password);
    }

    public TypeUtilisateur getTypeUtilisateur() {
        return TypeUtilisateur.MEMBRE;
    }
}
