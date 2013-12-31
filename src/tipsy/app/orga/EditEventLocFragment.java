package tipsy.app.orga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tipsy.app.R;
import tipsy.commun.Event;

/**
 * Created by Valoo on 05/12/13.
 */

public class EditEventLocFragment extends Fragment {

    private Event event;
    private EditEventFragment parent;

    public EditEventLocFragment(EditEventFragment frag, Event e){
        super();
        event = e;
        parent = frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_edit_event_loc, container, false);
        parent.onLocFragCreated(view);
        return view;
    }
}
