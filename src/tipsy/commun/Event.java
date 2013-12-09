package tipsy.commun;

import android.location.Address;
import android.media.Image;
import java.util.Date;

/**
 * Created by Valentin on 07/12/13.
 */
public class Event {
    private Address         adresse;
    /*
    private Bar             bar;
    private Billetterie     billetterie;
    */
    private Date            debut;
    private String          description;
    private EtatPublication etat;
    private Date            fin;
    private int             idOrganisateur;
    private Image           image;
    private String          nom;
    private TypeEvent       type;

    public Event(String nom, Date debut, Address adresse){
        this.nom            = nom;
        this.debut          = debut;
        this.adresse        = adresse;
    }

    public Address getAdresse() { return adresse; }
    public void setAdresse(Address adresse) { this.adresse = adresse; }
/*
    public Bar getBar() { return bar; }
    public void setBar(Bar bar) { this.bar = bar; }

    public Billetterie getBilletterie() { return billetterie; }
    public void setBilletterie(Billetterie billetterie) { this.billetterie = billetterie; }
*/
    public Date getDebut() { return debut; }
    public void setDebut(Date debut) { this.debut = debut; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public EtatPublication getEtat() { return etat; }
    public void setEtat(EtatPublication etat) { this.etat = etat; }

    public Date getFin() { return fin; }
    public void setFin(Date fin) { this.fin = fin; }

    public int getIdOrganisateur() { return idOrganisateur; }


    public Image getImage() { return image; }
    public void setImage(Image image) { this.image = image; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public TypeEvent getType() { return type; }
    public void setType(TypeEvent type) { this.type = type; }
}
