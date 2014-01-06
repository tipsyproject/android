package tipsy.commun;

import com.stackmob.sdk.model.StackMobModel;

import java.util.ArrayList;
import java.util.Date;

import tipsy.commun.Billetterie.Billet;
import tipsy.commun.Billetterie.Billetterie;

/**
 * Created by Valentin on 07/12/13.
 */
public class Event extends StackMobModel {

    private Billetterie<Billet> billetterie = new Billetterie<Billet>();
    private Date debut = new Date();
    private String lieu = null;
    private String nom = null;

    public Event() {
        super(Event.class);
    }

    public Event(String nom) {
        super(Event.class);
        this.nom = nom;
    }

    public Billetterie<Billet> getBilletterie() {
        return billetterie;
    }

    public void setBilletterie(ArrayList<Billet> billets) {
        this.billetterie.setBillets(billetterie);
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
