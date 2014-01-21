package tipsy.commun.commerce;

import com.stackmob.sdk.model.StackMobModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import tipsy.commun.Event;
import tipsy.commun.billetterie.Participation;

/**
 * Created by Valentin on 07/01/14.
 */
public class Commande extends StackMobModel {

    private ArrayList<Item> items = new ArrayList<Item>();
    private String description;
    private String titre;


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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setDescriptionFromEvent(Event event) {
        this.description = event.getNom() + " - " + event.getLieu();
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    // Le titre de la commande correspond aux noms des items qu'elle contient
    public void setTitreFromItems() {
        String titre = "Commande vide";
        if(!items.isEmpty()){
            Iterator it = items.iterator();
            Item item = (Item) it.next();
            titre = item.getProduit().getNom()+" x"+Integer.toString(item.getQuantite());
            while(it.hasNext()){
                item = (Item) it.next();
                titre += ", " + item.getProduit().getNom()+" x"+Integer.toString(item.getQuantite());
            }
        }
        this.titre = titre;
    }

    public int getPrixTotal() {
        return Panier.getPrixTotal(new HashSet<Item>(items));
    }

    public int getDevise() {
        return (items.size() > 0) ? items.get(0).getProduit().getDevise() : Commerce.Devise.EURO;
    }
}
