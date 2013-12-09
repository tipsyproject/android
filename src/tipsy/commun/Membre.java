package tipsy.commun;

/**
 * Created by valoo on 07/12/13.
 */

import java.util.Date;

public class Membre extends Participant {

    private String  password;
    private Date    naissance;

    public TypeUtilisateur getTypeUtilisateur(){
        return TypeUtilisateur.MEMBRE;
    }
}
