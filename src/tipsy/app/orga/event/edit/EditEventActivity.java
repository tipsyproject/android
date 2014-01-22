package tipsy.app.orga.event.edit;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.app.orga.OrgaActivity;
import tipsy.app.orga.event.EventOrgaActivity;
import tipsy.commun.Event;
import tipsy.commun.Organisateur;

/**
 * Created by valoo on 22/01/14.
 */
public class EditEventActivity extends FragmentActivity implements EditEventListener, ActionBar.TabListener, Validator.ValidationListener{

    private Event event;
    private boolean newEvent = false;
    boolean saving = false;

    public static final int NUM_ITEMS = 4;
    public final static int DESC = 0;
    public final static int LIEU = 1;
    public final static int DATE = 2;
    public final static int PARAMS = 3;
    private static final int[] icones = {
            R.drawable.ic_action_about,
            R.drawable.ic_action_place,
            R.drawable.ic_action_go_to_today,
            R.drawable.ic_action_settings
    };
    private ActionBar actionBar;
    private EditEventAdapter mAdapter;
    private ViewPager mPager;

    @Required(order = 1)
    private EditText inputNom;
    @Required(order = 2)
    private EditText inputLieu;

    private TextView inputDateDebut;
    private TextView inputTimeDebut;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_edit_event);
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            newEvent = savedInstanceState.getBoolean("NEW_EVENT");
            event = savedInstanceState.getParcelable("Event");
        }else{
            if(getIntent().hasExtra("NEW_EVENT")){
                newEvent = true;
                event = new Event();
            }else
                event = getIntent().getParcelableExtra("Event");
        }


        getActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new EditEventAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.event_pager);
        mPager.setAdapter(mAdapter);

        // DEFINITION DES TABS
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.removeAllTabs();
        for (int icon : icones)
            actionBar.addTab(actionBar.newTab().setIcon(icon).setTabListener(this));

        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the corresponding tab.
                        actionBar.setSelectedNavigationItem(position);
                    }
                }
        );



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(outState==null)
            outState = new Bundle();
        outState.putParcelable("Event", event);
        outState.putBoolean("NEW_EVENT", newEvent);
        // Add variable to outState here
        super.onSaveInstanceState(outState);
    }


    /* Définition de l'ACTIONBAR */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_event, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if(newEvent) backToOrga();
                else backToEventOrga();
                return true;
            case R.id.action_validate:
                if(!saving){
                    validator = new Validator(this);
                    validator.setValidationListener(this);
                    validator.validate();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    // Envoi de la demande de sauvegarde de l'événement à l'activité
    public void onValidationSucceeded() {
        saving = true;
        // Si c'est une création d'event, on initialise l'event
        final ProgressDialog wait = ProgressDialog.show(this,"","Enregistrement...",true,false);

        TipsyApp app = (TipsyApp) getApplication();
        Organisateur orga = app.getOrga();
        if (newEvent) {
            event = orga.creerEvent("");
        }
        if (inputNom != null) {
            event.setNom(inputNom.getText().toString());
        }
        if (inputLieu != null) {
            event.setLieu(inputLieu.getText().toString());
        }
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        String dateDebut = inputDateDebut.getText().toString() + " " + inputTimeDebut.getText().toString();
        try {
            event.setDebut(f.parse(dateDebut));
        } catch (ParseException e) {
        }

        orga.save(StackMobOptions.depthOf(1), new StackMobModelCallback() {
            @Override
            public void success() {
                saving = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wait.dismiss();
                    }
                });
                backToEventOrga();
            }

            @Override
            public void failure(StackMobException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wait.dismiss();
                    }
                });
                Log.e("TOUTAFAIT", "erreur sauvegarde Event:" + e.getMessage());
            }
        });
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        final String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(EditEventActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Listener des tabs
    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {}


    // EditEventListener

    public Event getEvent(){
        return event;
    }

    public void backToEventOrga(){
        Intent intent = new Intent(this, EventOrgaActivity.class);
        intent.putExtra("Event", event);
        startActivity(intent);
    }

    public void backToOrga(){
        Intent intent = new Intent(this, OrgaActivity.class);
        startActivity(intent);
    }


    // initialisation des inputs lors de leur affichage pour les rendre accessible au Validator

    // inputs partie description
    public void onDescFragCreated(View v) {
        inputNom = (EditText) v.findViewById(R.id.input_nom);
        inputNom.setText(event.getNom());
    }

    // inputs partie lieu
    public void onLocFragCreated(View v) {
        inputLieu = (EditText) v.findViewById(R.id.input_lieu);
        inputLieu.setText(event.getLieu());
    }

    // inputs partie date
    public void onDateFragCreated(View v) {
        inputDateDebut = (TextView) v.findViewById(R.id.input_date_debut);
        inputTimeDebut = (TextView) v.findViewById(R.id.input_time_debut);

        // Initialisation des dates de debut et de fin
        inputDateDebut.setText(EditEventDateFragment.dateFormatter.format(event.getDebut()));
        inputTimeDebut.setText(EditEventDateFragment.timeFormatter.format(event.getDebut()));
    }

}