package com.tipsy.app.orga.prevente;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tipsy.app.R;
import com.tipsy.lib.util.TarifGridAdapter;

/**
 * Created by valoo on 25/01/14.
 */
public class TarifsFragment extends Fragment {

    private PreventeListener callback;
    private GridView gridView;
    private TarifGridAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (PreventeListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_vendre, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setEmptyView(view.findViewById(R.id.empty_liste));
        adapter = new TarifGridAdapter(getActivity(), callback.getBilletterie());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TOUTAFAIT", "click listener");
                callback.getPrevente().setTicket(callback.getBilletterie().get(i));
                callback.goToParticipant();
            }
        });
        return view;
    }
}
