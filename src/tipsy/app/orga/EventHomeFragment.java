package tipsy.app.orga;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

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


        buttonBilleterie = (LinearLayout) view.findViewById(R.id.button_billetterie);
        buttonBar = (LinearLayout) view.findViewById(R.id.button_bar);
        buttonAcces = (LinearLayout) view.findViewById(R.id.button_acces);
        buttonInfos = (LinearLayout) view.findViewById(R.id.button_infos);

        buttonBar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Interdit à VOMITO !", Toast.LENGTH_LONG).show();
            }
        });

        buttonBilleterie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                event.fetch(StackMobOptions.depthOf(3), new StackMobModelCallback() {
                    @Override
                    public void success() {
                        callback.onBilletterieEdit(event);
                    }

                    @Override
                    public void failure(StackMobException e) {
                    }
                });
            }
        });
        buttonInfos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.onEventEdit(event);
            }
        });

        return view;
    }


}