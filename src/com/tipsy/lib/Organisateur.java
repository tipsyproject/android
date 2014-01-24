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

    private ArrayList<Event> events;
    private String nom;
    private String telephone;
    private User user;
    private StackMobFile avatar;

    public Organisateur() {
        super(Organisateur.class);
        this.events = new ArrayList<Event>();
    }

    public Organisateur(String username, String password, String nom) {
        super(Organisateur.class);
        user = new User(username, password, TypeUser.ORGANISATEUR);
        this.events = new ArrayList<Event>();
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

    public ArrayList<Event> getEvents() {
        return events;
    }

    public Event creerEvent(String nom) {
        Event e = new Event(nom);
        e.setOrganisateur(getUser().getEmail());
        events.add(e);
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

    public ArrayList<Event> getEventsByDate() {
        Collections.sort(events, new Comparator<Event>(){
            public int compare(Event a, Event b) {
                return -a.getDebut().compareTo(b.getDebut());
            }
        });
        return events;
    }
}
