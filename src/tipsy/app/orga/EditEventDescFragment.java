package tipsy.app.orga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import tipsy.app.R;
import tipsy.commun.Event;

/**
 * Created by Valoo on 05/12/13.
 */

public class EditEventDescFragment extends Fragment {

    private Event event;
    private EditEventFragment parent;

    public EditEventDescFragment(EditEventFragment frag, Event e){
        super();
        event = e;
        parent = frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_edit_event_desc, container, false);

        EditText inputNom = (EditText) view.findViewById(R.id.input_nom);

        parent.onDescFragCreated(view);
        return view;
    }
}
