package com.tipsy.app.orga;

import android.app.Activity;

import com.tipsy.app.MenuUser;
import com.tipsy.app.R;

/**
 * Created by Alexandre on 18/12/13.
 */
public class MenuOrga extends MenuUser {
    public final static int ACCUEIL = 0;
    public final static int MON_COMPTE = 1;
    public final static int STATS = 2;
    public final static int AIDE = 3;
    public final static int DECONNEXION = 4;

    public MenuOrga(Activity a) {
        super(a);
        mIcons = new int[]{
                R.drawable.ic_action_event,
                R.drawable.ic_action_person,
                R.drawable.ic_action_network_cell,
                R.drawable.ic_action_help,
                R.drawable.ic_action_undo
        };
        mTitles = new String[]{"Événements", "Mon Compte", "Statistiques", "Aide", "Déconnexion"};
        setTitre(mTitles[ACCUEIL]);
    }
}
