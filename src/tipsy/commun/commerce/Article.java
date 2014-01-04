package tipsy.commun.commerce;

import android.widget.TextView;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by valoo on 04/01/14.
 */


public abstract class Article extends StackMobModel{

    protected String nom = "Mon Tarif";
    /* On ne manipule que des centimes pour simplifier
        prix = 100 --> 1,00 €
     */
    protected int prix = 100;

    protected Article(java.lang.Class c){
        super(c);
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

    // laisser en protected
    // et utiliser la méthode parsePrix
    protected void setPrix(int prix) {
        this.prix = prix;
    }

    // Recupère le prix en euros flottant affiché dans un TextView
    // et le convertie en centimes entier
    public void parsePrix(TextView view){
        this.prix = (int) (Float.parseFloat(view.getText().toString()) * 100);
    }

    // Convertie le prix en euros puis en String
    public String getPrixToString(){
        return prixToString(prix);
    }

    public String getDevise(){
        return "€";
    }


    @Override
    public boolean equals(Object o){
        return (this.getID() == ((Article) o).getID());
    }

    // Convertie le prix de centimes vers euros puis en String
    public static String prixToString(int prix){
        // Pas de chiffre après la virgule
        if(prix%100==0){
            return Integer.toString(prix / 100);
        }else
            return Float.toString((float) prix / 100);
    }
}
