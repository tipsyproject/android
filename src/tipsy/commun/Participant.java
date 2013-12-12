package tipsy.commun;


import com.stackmob.sdk.model.StackMobUser;

/**
 * Created by Valentin on 07/12/13.
 */
public class Participant extends StackMobUser implements UtilisateurTipsy {

    private Bracelet bracelet;

    //TEST
    private String nom = null;

    public Participant(String username, String password) {
        super(Participant.class, username, password);

    }

    public Participant(String username, String password, String nom) {
        super(Participant.class, username, password);
        this.nom = nom;
    }
//TEST

    public Bracelet getBracelet() {
        return bracelet;
    }

    private void setBracelet(Bracelet bracelet) {
        this.bracelet = bracelet;
    }

    public TypeUtilisateur getTypeUtilisateur() {
        return TypeUtilisateur.ANONYME;
    }
}
