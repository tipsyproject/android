package com.tipsy.app.orga.entree.vente;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.tipsy.app.R;
import com.tipsy.app.orga.entree.EntreeFragment;
import com.tipsy.app.orga.entree.NFCCallback;
import com.tipsy.app.orga.event.EventOrgaActivity;
import com.tipsy.lib.util.Bracelet;

/**
 * Created by tech on 12/02/14.
 */
public class WaitFragment extends EntreeFragment {

    private boolean modePrevente = true;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_entree_mode_vente, container, false);

        final Switch toggle = (Switch) view.findViewById(R.id.switchModePrevente);
        toggle.setChecked(modePrevente);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(!modePrevente) Toast.makeText(getActivity(), "Mode prévente activé", Toast.LENGTH_SHORT).show();
                    modePrevente = true;
                } else if(modePrevente) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setPositiveButton("Désactiver", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            modePrevente = false;
                            Toast.makeText(getActivity(), "Mode prévente désactivé", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            toggle.setChecked(true);
                        }
                    });
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            toggle.setChecked(true);
                        }
                    });
                    builder.setTitle("Désactiver Mode Prévente !");
                    builder.setMessage("Les bracelets vendus seront automatiquement validés. Ils ne permettront pas l'accès à l'événement.");
                    builder.show();
                }
            }
        });

        return view;
    }


    public boolean modePrevente(){
        return modePrevente;
    }

}


