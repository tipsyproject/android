package tipsy.commun.billetterie;

import java.util.ArrayList;
import java.util.Iterator;

import tipsy.commun.commerce.Item;
import tipsy.commun.commerce.Panier;
import tipsy.commun.commerce.Produit;

/**
 * Created by valoo on 27/12/13.
 */
public class Billetterie<Billet> extends ArrayList<Billet> {

    public void setBillets(ArrayList<Billet> billets) {
        for(Billet b : billets)
            add(b);
    }


}
