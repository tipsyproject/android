package tipsy.app.orga;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import tipsy.app.R;
import tipsy.commun.Event;

/**
 * Created by valoo on 27/12/13.
 */

public class EventHomeFragment extends Fragment {
    private OrgaListener callback;
    private Event event;
    private LinearLayout buttonBilleterie;
    private LinearLayout buttonBar;
    private LinearLayout buttonAcces;
    private LinearLayout buttonInfos;

    public static EventHomeFragment init(Event e) {
        EventHomeFragment frag = new EventHomeFragment();
        Bundle args = new Bundle();
        args.putParcelable("Event", e);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OrgaListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_event_home, container, false);
        event = getArguments().getParcelable("Event");
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        /* CONTRÔLE D'ACCES */
        buttonAcces = (LinearLayout) view.findViewById(R.id.button_acces);
        buttonAcces.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToAccess(event);
            }
        });


        /* BAR */
        buttonBar = (LinearLayout) view.findViewById(R.id.button_bar);
        buttonBar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Fermé pour cause de VOMIS #RER_B", Toast.LENGTH_LONG).show();
            }
        });

        /* BILLETTERIE */
        buttonBilleterie = (LinearLayout) view.findViewById(R.id.button_billetterie);
        buttonBilleterie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToBilletterie(event);
            }
        });



        /* SETTINGS EVENT */
        buttonInfos = (LinearLayout) view.findViewById(R.id.button_infos);
        buttonInfos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.onEventEdit(event, false);
            }
        });

        return view;
    }


}