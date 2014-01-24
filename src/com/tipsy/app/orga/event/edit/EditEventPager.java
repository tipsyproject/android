package com.tipsy.app.orga.event.edit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by valoo on 22/01/14.
 */
// Gestionnaire des Fragments
public class EditEventPager extends FragmentPagerAdapter {

    public EditEventPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return EditEventActivity.NUM_ITEMS;
    }

    @Override
    // Affiche le fragment voulu en fonction de la position
    public Fragment getItem(int position) {
        // Dans l'ordre de gauche à droite
        switch (position) {
            case EditEventActivity.DESC:
                return new EditEventDescFragment();
            case EditEventActivity.LIEU:
                return new EditEventLocFragment();
            case EditEventActivity.DATE:
                return new EditEventDateFragment();
            default:
                return new EditEventSettingsFragment();
        }
    }
}
