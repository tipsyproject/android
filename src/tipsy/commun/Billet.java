package tipsy.commun;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by valoo on 27/12/13.
 */
public class Billet extends StackMobModel {

    private String nom = "Mon Tarif";
    private double prix = 1.00;

    public  Billet(){
        super(Billet.class);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
