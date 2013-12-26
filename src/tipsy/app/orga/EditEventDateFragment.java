package tipsy.app.orga;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import tipsy.app.R;
import tipsy.commun.Event;

/**
 * Created by Valoo on 05/12/13.
 */

public class EditEventDateFragment extends Fragment {

    private Event event;
    private EditEventFragment parent;
    public static SimpleDateFormat dateFormatter = new SimpleDateFormat ("dd-MM-yyyy");
    public static SimpleDateFormat timeFormatter = new SimpleDateFormat ("hh:mm");

    public EditEventDateFragment(EditEventFragment frag, Event e){
        super();
        event = e;
        parent = frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_edit_event_date, container, false);

        // Instanciation des Widgets
        final Button buttonDateDebut = (Button) view.findViewById(R.id.button_date_debut);
        final Button buttonTimeDebut = (Button) view.findViewById(R.id.button_time_debut);

        // Liaison des Date/Time Pickers aux inputs
        buttonDateDebut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog(buttonDateDebut);
            }
        });
        buttonTimeDebut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialog(buttonTimeDebut);
            }
        });

        parent.onDateFragCreated(view);
        return view;
    }

    public void showDatePickerDialog(TextView v) {
        DialogFragment newFragment = new DatePickerFragment(v);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(TextView v) {
        DialogFragment newFragment = new TimePickerFragment(v);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }




    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private TextView viewResultat;

        public DatePickerFragment(TextView resultat){
            super();
            viewResultat = resultat;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar cal = Calendar.getInstance();
            try{
                Date date = dateFormatter.parse(viewResultat.getText().toString());
                cal.setTime(date);
            }catch(ParseException e){
                // Use the current date as the default date in the picker
            }
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            GregorianCalendar cal = new GregorianCalendar(year,month,day);
            viewResultat.setText(dateFormatter.format(new Date(cal.getTimeInMillis())));
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        private TextView viewResultat;

        public TimePickerFragment(TextView resultat){
            super();
            viewResultat = resultat;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar cal = Calendar.getInstance();
            try{
                Date date = timeFormatter.parse(viewResultat.getText().toString());
                cal.setTime(date);
            }catch(ParseException e){
                // Use the current date as the default date in the picker
            }
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
            cal.set(Calendar.MINUTE,minute);
            viewResultat.setText(timeFormatter.format(new Date(cal.getTimeInMillis())));
        }
    }

}
