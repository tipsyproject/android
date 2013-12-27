package tipsy.app.orga;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.commun.Event;

/**
 * Created by valoo on 27/12/13.
 */

public class EventHomeFragment extends Fragment {
    private OrgaListener callback;
    private Event event;
    private Button buttonBilleterie;
    private Button buttonBar;
    private Button buttonAcces;
    private Button buttonInfos;

    public EventHomeFragment(Event e){
        super();
        event = e;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OrgaListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_event_home, container, false);

        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);


        buttonBilleterie = (Button) view.findViewById(R.id.button_billetterie);
        buttonBar = (Button) view.findViewById(R.id.button_bar);
        buttonAcces = (Button) view.findViewById(R.id.button_acces);
        buttonInfos = (Button) view.findViewById(R.id.button_infos);

        buttonInfos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.onEventEdit(event);
            }
        });

        return view;
    }


}