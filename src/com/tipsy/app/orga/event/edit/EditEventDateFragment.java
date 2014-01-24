package com.tipsy.app.orga.event.edit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.tipsy.app.R;

/**
 * Created by Valoo on 05/12/13.
 */

public class EditEventDateFragment extends Fragment {

    private Date debut;
    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm");
    private EditEventListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EditEventListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null && savedInstanceState.containsKey("DEBUT")){
            debut = (Date) savedInstanceState.getSerializable("DEBUT");
            Log.d("TOUTAFAIT", "date recup");
        }else if(callback.getEvent().getDebut() != null)
            debut = callback.getEvent().getDebut();
        else debut = new Date();

        View view = inflater.inflate(R.layout.frag_edit_event_date, container, false);

        /* DATE DEBUT EVENT */
        callback.setInputDateDebut((TextView) view.findViewById(R.id.input_date_debut));
        final DatePickerDialog.OnDateSetListener dpDebut = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                GregorianCalendar cal = new GregorianCalendar(year, month, day);
                callback.getInputDateDebut().setText(dateFormatter.format(new Date(cal.getTimeInMillis())));
            }

        };
        callback.getInputDateDebut().setText(dateFormatter.format(debut));

        callback.getInputDateDebut().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(debut);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getActivity(), dpDebut, year, month, day).show();
            }
        });


        /* HORAIRES DEBUT EVENT */
        callback.setInputTimeDebut( (TextView) view.findViewById(R.id.input_time_debut) );
        final TimePickerDialog.OnTimeSetListener tpDebut = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int min) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, min);
                callback.getInputTimeDebut().setText(timeFormatter.format(new Date(cal.getTimeInMillis())));
            }
        };

        callback.getInputTimeDebut().setText(timeFormatter.format(debut));

        // Liaison des Date/Time Pickers aux inputs
        callback.getInputTimeDebut().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(debut);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);
                new TimePickerDialog(getActivity(), tpDebut, hour, min, true).show();
            }
        });

        return view;
    }

    // Sauvegarde des dates prédéfinies
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(outState==null)
            outState = new Bundle();
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        String dateDebut = callback.getInputDateDebut().getText().toString()
                + " " + callback.getInputTimeDebut().getText().toString();
        try {
            debut = f.parse(dateDebut);
        } catch (ParseException e) {}
        outState.putSerializable("DEBUT", debut);
        super.onSaveInstanceState(outState);
    }
}
