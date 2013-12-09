package tipsy.commun;

import android.location.Address;
import com.stackmob.sdk.model.StackMobUser;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by valoo on 07/12/13.
 */
public class Organisateur extends StackMobUser implements UtilisateurTipsy {

    private ArrayList<Event>    events;
    private String              nom;

    public Organisateur(String username, String password){
        super(Organisateur.class, username, password);
    }

    public ArrayList<Event> getEvents() { return events; }
    protected void setEvents(ArrayList<Event> events) { this.events = events; }

    public void creerEvent(String nom, Date debut, Address adresse){
        Event e = new Event(nom,debut,adresse);
        this.events.add(e);
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public TypeUtilisateur getTypeUtilisateur(){
        return TypeUtilisateur.ORGANISATEUR;
    }
}
