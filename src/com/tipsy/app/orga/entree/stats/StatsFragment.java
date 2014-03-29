package com.tipsy.app.orga.entree.stats;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeFragment;
import com.tipsy.lib.Achat;
import com.todddavies.components.progressbar.ProgressWheel;

/**
 * Created by tech on 12/02/14.
 */
public class StatsFragment extends EntreeFragment {
    private ProgressWheel progressWheel;
    private TextView progressText;

    @SuppressLint("WrongViewCast")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_stats, container, false);

        /* BARRE DE SUIVI DES ENTREES */
        progressText = (TextView) view.findViewById(R.id.progressText);
        progressWheel = (ProgressWheel) view.findViewById(R.id.progressBar);
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
        int entreesTotales = callback.getEntrees().size();
        for (Achat entree : callback.getEntrees())
            if (entree.isUsed())
                entreesValidees++;
        if(entreesTotales > 0)
            progressWheel.setProgress(entreesValidees*360/entreesTotales);
        progressText.setText("" + entreesValidees + "/" + callback.getEntrees().size());
    }

}


