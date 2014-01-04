package tipsy.commun;

import com.stackmob.sdk.model.StackMobModel;

import java.util.ArrayList;
import java.util.Iterator;

import tipsy.commun.commerce.Item;
import tipsy.commun.commerce.Panier;

/**
 * Created by valoo on 27/12/13.
 */
public class Billetterie extends StackMobModel {

    private ArrayList<Billet> billets = new ArrayList<Billet>();

    public Billetterie() {
        super(Billetterie.class);
    }

    public ArrayList<Billet> getBillets() {
        return billets;
    }

    protected void setBillets(ArrayList<Billet> billets) {
        this.billets = billets;
    }


    /*
    Retourne la liste de billets convertis en items.
    La quantité de chaque item est définie à zéro, sauf si l'item est présent dans le panier de l'utilisateur
    auquel cas, on définie la quantité par celle définie dans le panier
    */
    public ArrayList<Item> getItems(Panier panier){
        ArrayList<Item> items = new ArrayList<Item>();
        Item item;
        Iterator b = billets.iterator();
        while(b.hasNext()){
            // Quantité définie à zéro
            item = new Item((Billet) b.next(), 0);
            // Si des billets sont déjà dans le panier de l'utilisateur
            if(panier.contains(item)){
                // On récupère l'item du panier pour avoir la quantité correspondante
                Iterator i = panier.iterator();
                Item itemCourant;
                while(i.hasNext()){
                    itemCourant = (Item) i.next();
                    if(itemCourant.equals(item)){
                        item.setQuantite(itemCourant.getQuantite());
                        break;
                    }
                }
            }

            items.add(item);
        }
        return items;
    }
}
