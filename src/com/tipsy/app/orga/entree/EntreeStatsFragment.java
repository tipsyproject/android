package com.tipsy.app.orga.entree;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Achat;

/**
 * Created by tech on 12/02/14.
 */
public class EntreeStatsFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView progressText;

    private EntreeListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EntreeListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_stats, container, false);

        /* BARRE DE SUIVI DES ENTREES */
        progressText = (TextView) view.findViewById(R.id.progressText);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateProgress();
    }

    /* MISE A JOUR DE LA BARRE DE PROGRESSION */
    public void updateProgress() {
        int entreesValidees = 0;
        for (Achat entree : callback.getEntrees())
            if (entree.isUsed())
                entreesValidees++;
        progressBar.setMax(callback.getEntrees().size());
        progressBar.setProgress(entreesValidees);
        progressText.setText("" + entreesValidees + "/" + callback.getEntrees().size());
    }

}


