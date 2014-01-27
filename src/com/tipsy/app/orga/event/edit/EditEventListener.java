package com.tipsy.app.orga.event.edit;

import android.widget.EditText;
import android.widget.TextView;

import com.tipsy.lib.Event;

/**
 * Created by valoo on 22/01/14.
 */
public interface EditEventListener {

    public Event getEvent();

    public EditText getInputNom();

    public void setInputNom(EditText inputNom);

    public EditText getInputLieu();

    public void setInputLieu(EditText inputLieu);

    public TextView getInputDateDebut();

    public void setInputDateDebut(TextView inputDateDebut);

    public TextView getInputTimeDebut();

    public void setInputTimeDebut(TextView inputTimeDebut);

    public void backToEventOrga();

    public void backToOrga();

}
