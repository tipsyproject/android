package tipsy.commun;

import com.stackmob.sdk.model.StackMobModel;

import java.util.ArrayList;

/**
 * Created by valoo on 27/12/13.
 */
public class Billetterie extends StackMobModel {

    private ArrayList<Billet> billets = new ArrayList<Billet>();

    public Billetterie(){
        super(Billetterie.class);
    }

    public Billet creerBillet(String nom, double prix){
        Billet b = new Billet();
        b.setNom(nom);
        b.setPrix(prix);
        billets.add(b);
        return b;
    }

    public ArrayList<Billet> getBillets() {
        return billets;
    }
}
