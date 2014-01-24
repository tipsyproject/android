package com.tipsy.lib;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by valoo on 24/01/14.
 */

@ParseClassName("Event")
public class Event extends ParseObject {

    public Event() {}

    public Date getDebut() {
        return getDate("debut");
    }

    public void setDebut(Date debut) {
        put("debut",debut);
    }

    public String getLieu() {
        return getString("lieu");
    }

    public void setLieu(String lieu) {
        put("lieu",lieu);
    }

    public String getNom() {
        return getString("nom");
    }

    public void setNom(String nom) {
        put("nom",nom);
    }

    public String getOrganisateur() {
        return getString("organisateur");
    }

    public void setOrganisateur(String organisateur) {
        put("organisateur",organisateur);
    }

}
