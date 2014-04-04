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
                R.drawable.ic_action_calendar_day,
                R.drawable.ic_action_user,
                R.drawable.ic_action_bargraph,
                R.drawable.ic_action_help,
                R.drawable.ic_action_io
        };
        mTitles = new String[]{"Événements", "Mon Compte", "Statistiques", "Aide", "Déconnexion"};
        setTitre(mTitles[ACCUEIL]);
    }
}
