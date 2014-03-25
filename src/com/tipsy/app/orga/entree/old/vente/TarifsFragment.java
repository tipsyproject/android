package com.tipsy.app.orga.entree.old.vente;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.old.EntreeListener;
import com.tipsy.lib.util.TarifGridAdapter;

/**
 * Created by valoo on 25/01/14.
 */
public class TarifsFragment extends Fragment {

    private EntreeListener callback;
    private GridView gridView;
    private TarifGridAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_tarifs, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setEmptyView(view.findViewById(R.id.empty_liste));
        adapter = new TarifGridAdapter(getActivity(), callback.getBilletterie());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                callback.setTarifVente(callback.getBilletterie().get(i));
            }
        });
        return view;
    }
}
