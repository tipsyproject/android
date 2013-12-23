package tipsy.app.orga;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.commun.Event;
import tipsy.commun.Organisateur;

/**
 * Created by valoo on 22/12/13.
 */
public class EditEventFragment extends Fragment implements ActionBar.TabListener, Validator.ValidationListener {

    private OrgaListener callback;
    private Event event;

    private static final int NUM_ITEMS = 4;
    private final static int DESC = 0;
    private final static int LIEU = 1;
    private final static int DATE = 2;
    private final static int PARAMS = 3;
    private static final int[] icones = {
            R.drawable.ic_action_about,
            R.drawable.ic_action_place,
            R.drawable.ic_action_go_to_today,
            R.drawable.ic_action_settings
    };
    private Fragment[] frags;


    private ActionBar actionBar;
    private EditEventAdapter mAdapter;
    private ViewPager mPager;

    @Required(order = 1)
    private EditText inputNom;
    private ImageButton buttonSave;
    private Validator validator;

    public EditEventFragment(Event e){
        super();
        event = e;
        frags = new Fragment[]{
                new EditEventDescFragment(e),
                new EditEventLocFragment(e),
                new EditEventDateFragment(e),
                new EditEventSettingsFragment(e)
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OrgaListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_edit_event, container, false);



        mAdapter = new EditEventAdapter(getActivity().getSupportFragmentManager());
        mPager = (ViewPager) view.findViewById(R.id.event_pager);
        mPager.setAdapter(mAdapter);

        // DEFINITION DES TABS
        actionBar = getActivity().getActionBar();
        actionBar.setHomeButtonEnabled(false);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (int icon : icones) {
            actionBar.addTab(actionBar.newTab().setIcon(icon).setTabListener(this));
        }

        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the corresponding tab.
                        actionBar.setSelectedNavigationItem(position);
                    }
                }
        );

        Fragment fragInfos = mAdapter.getItem(DESC);
        inputNom = (EditText) fragInfos.getView().findViewById(R.id.input_nom);
        validator = new Validator(this);
        validator.setValidationListener(this);

        buttonSave = (ImageButton) view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Validation du formulaire d'inscription
                validator.validate();
            };
        });



        return view;
    }

    // Sauvegarde de l'événement
    public void onValidationSucceeded() {
        Log.d("TOUTAFAIT","Nom event:"+inputNom.getText().toString());

        event.setNom(inputNom.getText().toString());

        event.save(StackMobOptions.depthOf(1), new StackMobModelCallback() {
            @Override
            public void success() {
                Log.d("TOUTAFAIT","Event saved");
                callback.onEventSaved();
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT","Erreur sauvegarde event:"+e.getMessage());
                Toast.makeText(getActivity(), "Erreur sauvegarde event:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
    public class EditEventAdapter extends FragmentPagerAdapter {
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
            return frags[position];
        }
    }
}
