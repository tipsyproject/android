package tipsy.commun.billetterie;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by vquefele on 20/01/14.
 */
public class Participant extends StackMobModel{
    private String email = "";
    private String nom = "";
    private String prenom = "";

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

}
