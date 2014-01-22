package tipsy.app.orga;

import android.app.Activity;

import tipsy.app.MenuUser;
import tipsy.app.R;

/**
 * Created by Alexandre on 18/12/13.
 */
public class MenuOrga extends MenuUser {
    public final static int ACCUEIL = 0;
    public final static int MON_COMPTE = 1;
    public final static int EVENEMENTS = 2;
    public final static int AIDE = 3;
    public final static int DECONNEXION = 4;

    public MenuOrga(Activity a) {
        super(a);
        mIcons = new int[]{
                R.drawable.ic_action_view_as_grid_dark,
                R.drawable.ic_action_person_dark,
                R.drawable.ic_action_event_dark,
                R.drawable.ic_action_help_dark,
                R.drawable.ic_action_undo_dark
        };
        mTitles = new String[]{"Tableau de Bord", "Mon Compte", "Événements", "Aide", "Déconnexion"};
        titres_menu = a.getResources().getStringArray(R.array.menu_organisateur);
        setTitre(titres_menu[ACCUEIL]);
    }
}
