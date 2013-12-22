package tipsy.app.orga;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tipsy.app.R;

/**
 * Created by valoo on 22/12/13.
 */
public class HomeOrgaFragment extends Fragment{
    private OrgaListener callback;
    private Button buttonNewEvent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OrgaListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_home, container, false);

        buttonNewEvent = (Button) view.findViewById(R.id.button_new_event);

        buttonNewEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.onEventNew();
            }
        });


        return view;
    }


}
