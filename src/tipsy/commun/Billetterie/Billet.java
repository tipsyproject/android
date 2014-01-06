package tipsy.commun.Billetterie;

import tipsy.commun.commerce.Produit;

/**
 * Created by valoo on 27/12/13.
 */
public class Billet extends Produit implements ParametresBillet.ParamBillet {

    public Billet() {
        super();
        this.typeProduit = Produit.BILLET;
        this.parametresBillet = new ParametresBillet();
    }


    /* Raccourci vers les propriétés du billet */

    public int getNbMax() {
        return parametresBillet.getNbMax();
    }

    public void setNbMax(int nbMax) {
        this.parametresBillet.setNbMax(nbMax);
    }

    // Nom du schema dans Stackmob
    public static String overrideSchemaName() {
        return "produit";
    }
}
