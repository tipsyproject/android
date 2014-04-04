package com.tipsy.app.orga.vestiaire.in;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.tipsy.app.R;

/**
 * Created by Alextoss on 13/03/2014.
 */
public class TicketNumberFragment extends DialogFragment {

    private EditText number;
    private TicketNumberListener listener;
    private String title;

    public TicketNumberFragment(String title){
        this.title = title;
    }

    public interface TicketNumberListener {
        public void onNumberDefined(int num);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (TicketNumberListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement TicketNumberListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_numberpicker, null);
        number = (EditText) view.findViewById(R.id.textNumber);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
            .setTitle(title)
            .setView(view)
            .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    listener.onNumberDefined(Integer.parseInt(number.getText().toString()));
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

        return builder.create();
    }
}
