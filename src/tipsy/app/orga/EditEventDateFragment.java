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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

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
    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm");


    public static EditEventDateFragment init(Event e){
        EditEventDateFragment frag = new EditEventDateFragment();
        Bundle args = new Bundle();
        args.putSerializable("Event",e);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_edit_event_date, container, false);
        event = (Event) getArguments().getSerializable("Event");
        // Instanciation des Widgets
        final TextView inputDateDebut = (TextView) view.findViewById(R.id.input_date_debut);
        final TextView inputTimeDebut = (TextView) view.findViewById(R.id.input_time_debut);

        // Liaison des Date/Time Pickers aux inputs
        inputDateDebut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog(inputDateDebut, event.getDebut());
            }
        });
        inputTimeDebut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialog(inputTimeDebut, event.getDebut());
            }
        });

        ((EditEventFragment)getParentFragment()).onDateFragCreated(view);
        return view;
    }

    public void showDatePickerDialog(TextView v, Date date) {
        DialogFragment newFragment = new DatePickerFragment(v, date);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(TextView v, Date date) {
        DialogFragment newFragment = new TimePickerFragment(v, date);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }


    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private TextView viewResultat;
        private Date date;

        public DatePickerFragment(TextView resultat, Date date) {
            super();
            viewResultat = resultat;
            this.date = date;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            setRetainInstance(true);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            GregorianCalendar cal = new GregorianCalendar(year, month, day);
            viewResultat.setText(dateFormatter.format(new Date(cal.getTimeInMillis())));
        }
    }

    public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        private TextView viewResultat;
        private Date date;

        public TimePickerFragment(TextView resultat, Date date) {
            super();
            viewResultat = resultat;
            this.date = date;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            setRetainInstance(true);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            viewResultat.setText(timeFormatter.format(new Date(cal.getTimeInMillis())));
        }
    }

}
