package tipsy.commun;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by Valentin on 07/12/13.
 */
public class Event extends StackMobModel {
    //private Address adresse = null;
    /*
    private Bar             bar;
    private Billetterie     billetterie;
    */
    //private Date debut = null;
    /*private String description = null;
    private EtatPublication etat = EtatPublication.EN_PREPARATION;
    private Date fin = null;
    private Image image = null;*/
    private String nom = null;
    //private TypeEvent type = TypeEvent.UNDEFINED;
    private Organisateur organisateur = null;

    public Event() {
        super(Event.class);
    }

    public Event(String nom) {
        super(Event.class);
        this.nom = nom;
        //this.debut = debut;
    }

    /*
    public Address getAdresse() {
        return adresse;
    }

    public void setAdresse(Address adresse) {
        this.adresse = adresse;
    }
*/
    /*
        public Bar getBar() { return bar; }
        public void setBar(Bar bar) { this.bar = bar; }

        public Billetterie getBilletterie() { return billetterie; }
        public void setBilletterie(Billetterie billetterie) { this.billetterie = billetterie; }
    */
/*
    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }*/
/*
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EtatPublication getEtat() {
        return etat;
    }

    public void setEtat(EtatPublication etat) {
        this.etat = etat;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }


    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
*/
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Organisateur getOrganisateur() {
        return organisateur;
    }
/*
    public TypeEvent getType() {
        return type;
    }

    public void setType(TypeEvent type) {
        this.type = type;
    }*/
}
