package tipsy.app.orga;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.app.HomeMembreActivity;
import tipsy.app.R;
import tipsy.commun.Event;

/**
 * Created by Valoo on 05/12/13.
 */

public class EditEventActivity extends FragmentActivity implements ActionBar.TabListener, Validator.ValidationListener {
    // Nombre de colonnes
    private static final int NUM_ITEMS = 4;
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
    private EditText nom;
    private ImageButton enregistrer;
    private Validator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orga_editer_event);

        mAdapter = new EditEventAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.event_pager);
        mPager.setAdapter(mAdapter);

        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        // Permet aux tabs d'être afficher dans la barre de navigation.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (int icon : icones) {
            actionBar.addTab(actionBar.newTab().setIcon(icon).setTabListener(this));
        }

        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });


        nom = (EditText) findViewById(R.id.nom);
        enregistrer = (ImageButton) findViewById(R.id.button_enregistrer);
        validator = new Validator(this);
        validator.setValidationListener(this);

        enregistrer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Validation du formulaire d'inscription
                validator.validate();
            }

            ;
        });
    }


    public void onValidationSucceeded() {
        // Création d'un nouvel organisateur

        nom = (EditText) findViewById(R.id.nom);
        final Event e = new Event(nom.getText().toString());
        // Sauvegarde dans la BDD
        e.save(new StackMobModelCallback() {
            // Connexion automatique en cas de réussite
            @Override
            public void success() {
                startActivity(new Intent(EditEventActivity.this, HomeMembreActivity.class));
            }

            // En cas d'échec
            @Override
            public void failure(StackMobException e) {
            }
        });
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    // Listener de
    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
    }

    // Gestionnaire de Fragments
    public static class EditEventAdapter extends FragmentPagerAdapter {
        public EditEventAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        // Affiche le fragment voulu en fonction de la position
        public Fragment getItem(int position) {
            // Dans l'ordre de gauche à droite
            switch (position) {
                case 0:
                    return EditEventDescFragment.init(position);
                case 1:
                    return EditEventLocFragment.init(position);
                case 2:
                    return EditEventDateFragment.init(position);
                default:
                    return EditEventSettingsFragment.init(position);
            }
        }
    }
}
