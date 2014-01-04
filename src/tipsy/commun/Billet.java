package tipsy.commun;

import tipsy.commun.commerce.Article;

/**
 * Created by valoo on 27/12/13.
 */
public class Billet extends Article {

    public Billet() {
        super(Billet.class);
    }

    // Nom du schema dans Stackmob
    public static String overrideSchemaName() {
        return "billet";
    }
}
