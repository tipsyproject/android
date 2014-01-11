package tipsy.commun.commerce;

import com.stackmob.sdk.model.StackMobModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Valentin on 07/01/14.
 */
public class Commande extends StackMobModel {

    private ArrayList<Item> items = new ArrayList<Item>();

    public Commande() {
        super(Commande.class);
    }

    public Commande(Panier p) {
        super(Commande.class);
        Iterator it = p.iterator();
        while (it.hasNext()) {
            items.add((Item) it.next());
        }
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getDestinataire() {
        return items.get(0).getProduit().getEvent().getOrganisateur().getUser().getUsername();
    }

    public int getPrixTotal() {
        return Panier.getPrixTotal(new HashSet<Item>(items));
    }

    public int getDevise() {
        return (items.size() > 0) ? items.get(0).getProduit().getDevise() : Commerce.Devise.EURO;
    }
}
