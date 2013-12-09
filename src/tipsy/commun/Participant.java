package tipsy.commun;


/**
 * Created by Valentin on 07/12/13.
 */
public class Participant extends Contact implements UtilisateurTipsy {

    private Bracelet                bracelet;

    public Participant(){}

    public Bracelet getBracelet() { return bracelet; }
    private void setBracelet(Bracelet bracelet) { this.bracelet = bracelet; }

    public TypeUtilisateur getTypeUtilisateur(){
        return TypeUtilisateur.ANONYME;
    }
}
