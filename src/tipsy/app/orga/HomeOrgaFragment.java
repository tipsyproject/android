package tipsy.app.orga;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.commun.Event;

/**
 * Created by Alexandre on 23/12/13.
 */
public class HomeOrgaFragment extends Fragment {
    private OrgaListener callback;
    private RelativeLayout resumeEvent;
    private TextView textUpcoming;
    private Event upcomingEvent;

    public HomeOrgaFragment() {
        super();
        upcomingEvent = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OrgaListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_home, container, false);

        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        textUpcoming = (TextView) view.findViewById(R.id.text_upcoming);
        resumeEvent = (RelativeLayout) view.findViewById(R.id.resume_upcoming_event);

        LinearLayout buttonNewEvent = (LinearLayout) view.findViewById(R.id.button_new_event);
        buttonNewEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToNewEvent();
            }
        });

        LinearLayout buttonStats = (LinearLayout) view.findViewById(R.id.button_stats);
        buttonStats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Fonctionnalité à venir.", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setMenuTitle(MenuOrga.ACCUEIL);
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        upcomingEvent = app.getOrga().getUpcomingEvent();
        // Si aucun event n'est à venir
        if (upcomingEvent == null)
            textUpcoming.setText(R.string.no_upcoming_event);
            // Sinon affichage de la miniature de l'event
        else {
            resumeEvent.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    callback.goToEvent(upcomingEvent);
                }
            });
            textUpcoming.setText(upcomingEvent.getNom());
        }

    }


}