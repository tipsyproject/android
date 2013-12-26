package tipsy.commun;

import com.stackmob.sdk.model.StackMobModel;

import java.util.Date;

/**
 * Created by Valentin on 07/12/13.
 */
public class Event extends StackMobModel {
    private String nom = null;
    private Date debut;

    public Event() {
        super(Event.class);
        debut = new Date();
    }

    public Event(String nom) {
        super(Event.class);
        this.nom = nom;
        debut = new Date();
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
