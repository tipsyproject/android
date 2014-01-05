package tipsy.commun.commerce;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by valoo on 04/01/14.
 */


public abstract class Article extends StackMobModel{

    protected String nom = "Mon Tarif";
    protected int prix = 100;
    protected int devise = Commerce.Devise.EURO;

    protected Article(java.lang.Class c){
        super(c);
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
        return (this.getID() == ((Article) o).getID());
    }

}
