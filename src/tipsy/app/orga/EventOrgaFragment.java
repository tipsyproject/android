package tipsy.app.orga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import tipsy.app.R;

/**
 * Created by Alexandre on 23/12/13.
 */
public class EventOrgaFragment extends Fragment {

    public EventOrgaFragment() {
    }

    protected ImageButton buttonCreerEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.frag_orga_event, container, false);

        buttonCreerEvent = (ImageButton) fragmentView.findViewById(R.id.button_creer_event);

        buttonCreerEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditEventActivity.class));


    }
        });
        return fragmentView;
}
}
