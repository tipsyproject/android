package com.tipsy.app.orga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.app.R;
import com.tipsy.app.UserActivity;
import com.tipsy.app.help.HelpActivity;
import com.tipsy.app.login.LoginActivity;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.app.orga.event.edit.EditEventActivity;
import com.tipsy.lib.Event;
import com.tipsy.lib.TipsyUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valoo on 05/12/13.
 */
public class OrgaActivity extends UserActivity implements OrgaListener {

    private ArrayList<Event> events = new ArrayList<Event>();
    protected boolean bundle = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_user);
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // Get the content view
            View contentView = findViewById(android.R.id.content);

            // Make sure it's a valid instance of a FrameLayout
            if (contentView instanceof FrameLayout) {
                TypedValue tv = new TypedValue();

                // Get the windowContentOverlay value of the current theme
                if (getTheme().resolveAttribute(
                        android.R.attr.windowContentOverlay, tv, true)) {

                    // If it's a valid resource, set it as the foreground drawable
                    // for the content view
                    if (tv.resourceId != 0) {
                        ((FrameLayout) contentView).setForeground(
                                getResources().getDrawable(tv.resourceId));
                    }
                }
            }
        }

        menu = new MenuOrga(this);
        menu.initAdapter(new UserActivity.DrawerItemClickListener());
        menu.getDrawerList().setItemChecked(MenuOrga.ACCUEIL, true);

        if (savedInstanceState == null)
            bundle = true;
        else
            events = savedInstanceState.getParcelableArrayList("Events");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bundle) {
            final ProgressDialog wait = ProgressDialog.show(this, null, "Chargement...", true);

            ParseQuery<Event> eventsQuery = ParseQuery.getQuery(Event.class);
            eventsQuery.whereEqualTo("organisateur", TipsyUser.getCurrentUser().getObjectId());
            eventsQuery.findInBackground(new FindCallback<Event>() {
                public void done(List<Event> res, ParseException e) {
                    if (e == null) {
                        events.clear();
                        events.addAll(res);
                        wait.dismiss();
                        tableauDeBord(false);
                    } else {
                        Log.d("evens", "Error: " + e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelableArrayList("Events", events);
        super.onSaveInstanceState(outState);
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

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void goToEvent(int index) {
        Intent intent = new Intent(this, EventOrgaActivity.class);
        intent.putExtra("EVENT_ID", events.get(index).getObjectId());
        startActivity(intent);
        finish();
    }

    // Clique sur le bouton "Créer un événement" ou Modifications de l'event
    public void goToNewEvent() {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("NEW_EVENT", true);
        startActivity(intent);
        finish();
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
