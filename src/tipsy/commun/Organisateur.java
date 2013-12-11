package tipsy.commun;


import android.util.Log;

import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.model.StackMobUser;

import java.util.ArrayList;
import java.util.Date;

import tipsy.app.HomeActivity;

/**
 * Created by valoo on 07/12/13.
 */
public class Organisateur extends StackMobUser implements UtilisateurTipsy {

    private ArrayList<Event> events = null;
    private String nom = null;

    /**
     * *************
     * CONSTRUCTEURS *
     * **************
     */
    public Organisateur(String username, String password) {
        super(Organisateur.class, username, password);
    }

    // Constructeur pour l'inscription
    public Organisateur(String username, String password, String nom) {
        super(Organisateur.class, username, password);
        this.nom = nom;
    }


    public ArrayList<Event> getEvents() { return events; }
    protected void setEvents(ArrayList<Event> events) { this.events = events; }

    public void creerEvent(String nom){
        Event e = new Event(nom);
        e.save();
        this.events.add(e);
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public TypeUtilisateur getTypeUtilisateur() {
        return TypeUtilisateur.ORGANISATEUR;
    }
}
