package tipsy.commun;


import java.util.List;
import java.util.ArrayList;

/**
 * Created by valoo on 07/12/13.
 */
public class Organisateur extends User {

    public Organisateur(String username, String password){
        super(username,password);
        this.type = TypeUser.ORGANISATEUR;
    }

    public Organisateur(String username, String password, String nom){
        super(username,password,nom);
    }
}
