package com.tipsy.app.orga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.app.HelpActivity;
import com.tipsy.app.LoginActivity;
import com.tipsy.app.R;
import com.tipsy.app.UserActivity;
import com.tipsy.app.orga.event.edit.EditEventActivity;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.Event;
import com.tipsy.lib.TipsyUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valoo on 05/12/13.
 */
public class OrgaActivity extends UserActivity implements OrgaListener {

    private ArrayList<Event> events = new ArrayList<Event>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setContentView(R.layout.act_user);
        super.onCreate(savedInstanceState);

        this.menu = new MenuOrga(this);
        menu.initAdapter(new UserActivity.DrawerItemClickListener());
        menu.getDrawerList().setItemChecked(MenuOrga.ACCUEIL, true);


        final ProgressDialog wait = ProgressDialog.show(this,null,"Chargement...",true);

        ParseQuery<Event> eventsQuery = ParseQuery.getQuery(Event.class);
        eventsQuery.whereEqualTo("organisateur", TipsyUser.getCurrentUser().getObjectId());
        eventsQuery.findInBackground(new FindCallback<Event>() {
            public void done(List<Event> res, ParseException e) {
                if (e == null) {
                    events.clear();
                    events.addAll(res);
                    wait.dismiss();

                    if (savedInstanceState == null) {
                        tableauDeBord(false);
                    }

                } else {
                    Log.d("evens", "Error: " + e.getMessage());
                }
            }
        });


    }


    protected void selectItem(int position) {
        // update selected item and title, then close the drawer
        this.menu.getDrawerList().setItemChecked(position, true);
        this.menu.getDrawerList().setSelection(position);
        this.menu.getDrawerLayout().closeDrawer(this.menu.getDrawerList());
        switch (position) {
            case MenuOrga.ACCUEIL:
                tableauDeBord(true);
                break;
            case MenuOrga.MON_COMPTE:
                account();
                break;
            case MenuOrga.STATS:
                stats();
                break;
            case MenuOrga.AIDE:
                Intent intent = new Intent(this, HelpActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("Connected", true);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case MenuOrga.DECONNEXION:
                TipsyUser.logOut();
                startActivity(new Intent(this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
        }

    }


    // IMPLEMENTATIONS DES LISTENERS DU MODULE ORGANISATEUR

    public ArrayList<Event> getEvents(){
        return events;
    }

    public void goToEvent(int index) {
        Intent intent = new Intent(this, EventOrgaActivity.class);
        intent.putExtra("EVENT_ID", events.get(index).getObjectId());
        startActivity(intent);
    }

    // Clique sur le bouton "Créer un événement" ou Modifications de l'event
    public void goToNewEvent() {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("NEW_EVENT",true);
        startActivity(intent);
    }

    public void tableauDeBord(boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new HomeOrgaFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void account() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.content, new AccountOrgaFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }


    public void stats() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.content, new StatsOrgaFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }


}
