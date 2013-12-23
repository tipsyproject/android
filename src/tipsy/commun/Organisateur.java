package tipsy.commun;


import com.stackmob.sdk.model.StackMobModel;

import java.util.ArrayList;

/**
 * Created by valoo on 07/12/13.
 */
public class Organisateur extends StackMobModel implements User.TipsyUser {

    private ArrayList<Event> events;
    private String nom;
    private String telephone;
    private User user;

    public Organisateur(){
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

    public ArrayList<Event> getEvents() {
        return events;
    }

    public Event creerEvent(String nom) {
        Event e = new Event(nom);
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
}
