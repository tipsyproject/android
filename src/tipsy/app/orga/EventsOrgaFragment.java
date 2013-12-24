package tipsy.app.orga;

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
public class EventsOrgaFragment extends Fragment {

    public EventsOrgaFragment() {
    }

    protected ImageButton buttonCreerEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.frag_orga_events, container, false);

        return fragmentView;
}
}
