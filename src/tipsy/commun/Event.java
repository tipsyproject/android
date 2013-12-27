package tipsy.commun;

import com.stackmob.sdk.model.StackMobModel;

import java.util.Date;

/**
 * Created by Valentin on 07/12/13.
 */
public class Event extends StackMobModel {
    private Billetterie billetterie = new Billetterie();
    private Date debut = new Date();
    private String nom = null;

    public Event() {
        super(Event.class);
    }

    public Event(String nom) {
        super(Event.class);
        this.nom = nom;
    }

    public Billetterie getBilletterie() {
        return billetterie;
    }

    public void setBilletterie(Billetterie billetterie) {
        this.billetterie = billetterie;
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
