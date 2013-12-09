package tipsy.commun;

import android.location.Address;
import android.media.Image;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by valoo on 07/12/13.
 */
public class Organisateur implements UtilisateurTipsy {

    private Address             adresse;
    private ArrayList<Event>    events;
    private Image               logo;
    private String              mail;
    private String              nom;
    private String              password;
    private Contact             responsable;
    private String              url;

    public Address getAdresse() { return adresse; }
    public void setAdresse(Address adresse) { this.adresse = adresse; }

    public ArrayList<Event> getEvents() { return events; }
    protected void setEvents(ArrayList<Event> events) { this.events = events; }

    public void creerEvent(String nom, Date debut, Address adresse){


    }

    public Image getLogo() { return logo; }
    public void setLogo(Image logo) { this.logo = logo; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Contact getResponsable() { return responsable; }
    public void setResponsable(Contact responsable) { this.responsable = responsable; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public TypeUtilisateur getTypeUtilisateur(){
        return TypeUtilisateur.ORGANISATEUR;
    }
}
