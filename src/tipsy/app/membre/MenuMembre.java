package tipsy.app.membre;

import android.app.Activity;

import tipsy.app.MenuUser;
import tipsy.app.R;

/**
 * Created by Alexandre on 18/12/13.
 */
public class MenuMembre extends MenuUser {
    public static int ACCUEIL = 0;
    public static int MON_COMPTE = 1;
    public static int SOLDE = 2;
    public static int EVENEMENTS = 3;
    public static int AIDE = 4;
    public static int DECONNEXION = 5;

    public MenuMembre(Activity a) {
        super(a);
        this.titres_menu = a.getResources().getStringArray(R.array.menu_membre);
        this.titre=titres_menu[0];
    }
}
