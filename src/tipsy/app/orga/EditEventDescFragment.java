package tipsy.app.orga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tipsy.app.R;

/**
 * Created by Valoo on 05/12/13.
 */

public class EditEventDescFragment extends Fragment {

    int fragVal;

    static EditEventDescFragment init(int val) {
        EditEventDescFragment frag = new EditEventDescFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_orga_edit_event_desc, container,false);
        return layoutView;
    }
}
