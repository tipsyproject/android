package tipsy.app.orga;

import android.app.Activity;

import tipsy.app.MenuUser;
import tipsy.app.R;

/**
 * Created by Alexandre on 18/12/13.
 */
public class MenuOrga extends MenuUser {
    public static int ACCUEIL = 0;
    public static int MON_COMPTE = 1;
    public static int EVENEMENTS = 2;
    public static int AIDE = 3;
    public static int DECONNEXION = 4;

    public MenuOrga(Activity a) {
        super(a);
        titres_menu = a.getResources().getStringArray(R.array.menu_organisateur);
        setTitre(titres_menu[ACCUEIL]);
    }
}
