package tipsy.commun;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by Valentin on 07/12/13.
 */
public class Event extends StackMobModel {
    private String nom = null;

    public Event() {
        super(Event.class);
    }

    public Event(String nom) {
        super(Event.class);
        this.nom = nom;
        //this.debut = debut;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
