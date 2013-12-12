package tipsy.commun;


import java.util.ArrayList;

/**
 * Created by valoo on 07/12/13.
 */
public class Organisateur extends User{

    private ArrayList<Event> events = null;


     /****************
     * CONSTRUCTEURS *
     ****************/
    public Organisateur(String username, String password) {
        super(username, password);
    }

    // Constructeur pour l'inscription
    public Organisateur(String username, String password, String nom) {
        super(username, password);
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

}
