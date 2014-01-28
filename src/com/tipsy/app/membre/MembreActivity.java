package com.tipsy.app.membre;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.app.TipsyApp;
import com.tipsy.app.help.HelpActivity;
import com.tipsy.app.login.LoginActivity;
import com.tipsy.app.R;
import com.tipsy.app.UserActivity;
import com.tipsy.app.membre.bracelet.BraceletActivity;
import com.tipsy.app.membre.event.EventMembreActivity;
import com.tipsy.app.membre.wallet.WalletActivity;
import com.tipsy.lib.Event;
import com.tipsy.lib.TipsyUser;
import com.tipsy.lib.util.QueryCallback;
import com.tipsy.lib.util.Wallet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by tech on 05/12/13.
 */
public class MembreActivity extends UserActivity implements MembreListener {

    private SearchView SearchView;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private ArrayList<Event> eventResults = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_user);
        super.onCreate(savedInstanceState);
        this.menu = new MenuMembre(this);
        menu.initAdapter(new UserActivity.DrawerItemClickListener());
        menu.getDrawerList().setItemChecked(MenuMembre.ACCUEIL, true);

        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                GregorianCalendar cal = new GregorianCalendar(year, month, day);
                searchEventByDate(cal);
            }

        };

        if (savedInstanceState == null){
            /* Chargement du TipsyWallet */
            TipsyApp app = (TipsyApp) getApplication();
            final ProgressDialog wait = ProgressDialog.show(this, null, "Chargement du Tipsy Wallet...", true, false);
            app.loadWallet(new QueryCallback() {
                @Override
                public void done(Exception e) {
                    wait.dismiss();
                    goToTableauDeBord(false);
                    if(e!=null)
                        Toast.makeText(MembreActivity.this,"Erreur initialisation du wallet",Toast.LENGTH_SHORT);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        menu.findItem(R.id.search).setVisible(!this.menu.isDrawerOpen());
        menu.findItem(R.id.search_date).setVisible(!this.menu.isDrawerOpen());

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView = (SearchView) searchItem.getActionView();
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) SearchView.findViewById(SearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        //searchTextView.setTextColor(getResources().getColor(R.color.text));
        //searchTextView.setTextColor(getResources().getColor(R.color.text));
        searchTextView.setHintTextColor(getResources().getColor(R.color.search_text_hint));
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor);
        } catch (Exception e) {
        }
        SearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }


    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.search_date:
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(this, datePickerListener, year, month, day).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void selectItem(int position) {
        // update selected item and title, then close the drawer
        this.menu.getDrawerList().setItemChecked(position, true);
        this.menu.getDrawerList().setSelection(position);
        this.menu.getDrawerLayout().closeDrawer(this.menu.getDrawerList());
        switch (position) {
            case MenuMembre.ACCUEIL:
                goToTableauDeBord(true);
                break;
            case MenuMembre.MON_COMPTE:
                goToAccount();
                break;
            case MenuMembre.EVENEMENTS:
                goToEvents();
                break;
            case MenuMembre.AIDE:
                Intent intent = new Intent(this, HelpActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("Connected", true);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case MenuMembre.DECONNEXION:
                TipsyUser.logOut();
                startActivity(new Intent(this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
        }

    }

    // IMPLEMENTATION DU LISTENER MEMBRE
    public void goToAccount() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new AccountMembreFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void goToTableauDeBord(boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new HomeMembreFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void goToBracelet() {
        Intent intent = new Intent(this, BraceletActivity.class);
        startActivity(intent);
    }

    public void goToWallet() {
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
    }

    public void goToEvents() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new EventsMembreFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void goToEvent(Event e) {
        Intent intent = new Intent(this, EventMembreActivity.class);
        intent.putExtra("EVENT_ID", e.getObjectId());
        startActivity(intent);
    }

    public void searchEventByDate(Calendar cal) {
        final ProgressDialog wait = ProgressDialog.show(this, null, "Recherche...", true);

        // On va definir l'intervalle de recherche de date
        //  00h00  <= date < 00h00 à J+1
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        GregorianCalendar calmin = new GregorianCalendar(year, month, day);
        GregorianCalendar calmax = new GregorianCalendar(year, month, day);
        calmax.add(Calendar.DAY_OF_MONTH, +1);

        ParseQuery<Event> eventsQuery = ParseQuery.getQuery(Event.class);
        eventsQuery.whereGreaterThanOrEqualTo("debut", calmin.getTime());
        eventsQuery.whereLessThan("debut", calmax.getTime());

        eventsQuery.findInBackground(new FindCallback<Event>() {
            public void done(List<Event> res, ParseException e) {
                if (e == null) {
                    eventResults.clear();
                    eventResults.addAll(res);
                    wait.dismiss();
                    goToSearchResults();
                } else {
                    Log.d("TOUTAFAIT", "result ko:" + e.getMessage());
                    wait.dismiss();
                }
            }
        });
    }

    public void searchEventByKeyword(String query) {
    }


    public void goToSearchResults() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new SearchEventFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public ArrayList<Event> getSearchResults() {
        return eventResults;
    }
}
