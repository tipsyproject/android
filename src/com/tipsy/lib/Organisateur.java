package com.tipsy.lib;


import com.stackmob.sdk.api.StackMobFile;
import com.stackmob.sdk.model.StackMobModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by valoo on 07/12/13.
 */
public class Organisateur extends StackMobModel implements User.TipsyUserold {

    private ArrayList<Event_old> eventOlds;
    private String nom;
    private String telephone;
    private User user;
    private StackMobFile avatar;

    public Organisateur() {
        super(Organisateur.class);
        this.eventOlds = new ArrayList<Event_old>();
    }

    public Organisateur(String username, String password, String nom) {
        super(Organisateur.class);
        user = new User(username, password, TypeUser.ORGANISATEUR);
        this.eventOlds = new ArrayList<Event_old>();
        this.nom = nom;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public StackMobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(StackMobFile avatar) {
        this.avatar = avatar;
    }

    public ArrayList<Event_old> getEventOlds() {
        return eventOlds;
    }

    public Event_old creerEvent(String nom) {
        Event_old e = new Event_old(nom);
        e.setOrganisateur(getUser().getEmail());
        eventOlds.add(e);
        return e;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getType() {
        return TypeUser.ORGANISATEUR;
    }

    public User getUser() {
        return user;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Event_old> getEventsByDate() {
        Collections.sort(eventOlds, new Comparator<Event_old>(){
            public int compare(Event_old a, Event_old b) {
                return -a.getDebut().compareTo(b.getDebut());
            }
        });
        return eventOlds;
    }
}
