package tipsy.app.membre;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.SearchView;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.api.StackMobQueryField;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import tipsy.app.HelpActivity;
import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.UserActivity;
import tipsy.app.membre.wallet.WalletActivity;
import tipsy.app.membre.wallet.WalletResumeFragment;
import tipsy.commun.Billetterie;
import tipsy.commun.Event;
import tipsy.commun.Membre;

/**
 * Created by tech on 05/12/13.
 */
public class MembreActivity extends UserActivity implements MembreListener {

    private TipsyApp app;
    private SearchView SearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_user);
        super.onCreate(savedInstanceState);
        this.menu = new MenuMembre(this);
        menu.initAdapter(new UserActivity.DrawerItemClickListener());

        app = (TipsyApp) getApplication();
        goToTableauDeBord(false);
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
        SearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }


    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.search_date:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
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
            case MenuMembre.SOLDE:
                goToWallet();
                break;
            case MenuMembre.EVENEMENTS:
                goToEvents();
                break;
            case MenuMembre.AIDE:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case MenuMembre.DECONNEXION:
                app.logout(this);
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
        final Event event = e;
        event.fetch(StackMobOptions.depthOf(3), new StackMobModelCallback() {
            @Override
            public void success() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content, new EventFragment(event))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }

            @Override
            public void failure(StackMobException e) {
            }
        });

    }

    public void goToEventBillets(Billetterie b) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new EventBilletsFragment(b))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    public Membre getMembre() {
        return app.getMembre();
    }

    public void searchEventByDate(Date d) {
        // On va definir l'intervalle de recherche de date
        //  00h00  <= date < 00h00 à J+1
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        GregorianCalendar calmin = new GregorianCalendar(year, month, day);
        GregorianCalendar calmax = new GregorianCalendar(year, month, day);
        calmax.add(Calendar.DAY_OF_MONTH, +1);
        Event.query(Event.class,
                new StackMobQuery().field(new StackMobQueryField("debut")
                        .isGreaterThanOrEqualTo(calmin.getTimeInMillis())
                        .isLessThan(calmax.getTimeInMillis())
                ),
                new StackMobQueryCallback<Event>() {
                    @Override
                    public void success(List<Event> result) {
                        ArrayList<Event> events = new ArrayList<Event>();
                        for (int i = 0; i < result.size(); ++i) {
                            events.add(result.get(i));
                        }
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.content, new SearchEventFragment(events))
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                    }

                    @Override
                    public void failure(StackMobException e) {
                        Log.d("TOUTAFAIT", "Requete event KO:" + e.getMessage());
                    }
                });
    }


    public void searchEventByKeyword(String query) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content, new SearchEventFragment(new ArrayList<Event>()))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // Dialog de recherche d'event par date
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            GregorianCalendar cal = new GregorianCalendar(year, month, day);
            searchEventByDate(new Date(cal.getTimeInMillis()));
        }
    }
}
