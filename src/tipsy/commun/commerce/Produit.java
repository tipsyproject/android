package tipsy.commun.commerce;

import com.stackmob.sdk.model.StackMobModel;

import tipsy.commun.Billetterie.ParametresBillet;

/**
 * Created by valoo on 04/01/14.
 */


public abstract class Produit extends StackMobModel{

    protected String nom = "Mon Tarif";
    protected int prix = 0;
    protected int devise = Commerce.Devise.EURO;
    protected ParametresBillet parametresBillet = null;
    protected int typeProduit;

    public static int BILLET = 0;
    public static int TICKET = 1;
    public static int CONSO  = 2;

    protected Produit(){
        super(Produit.class);
    }

    public int getDevise() {
        return devise;
    }

    public void setDevise(int devise) {
        this.devise = devise;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    @Override
    public boolean equals(Object o){
        return (this.getID() == ((Produit) o).getID());
    }

}
