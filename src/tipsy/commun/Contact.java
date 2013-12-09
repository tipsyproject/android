package tipsy.commun;

/**
 * Created by Valentin on 07/12/13.
 */
public class Contact {
    private String  mail;
    private String  nom;
    private String  prenom;
    private Sexe    sexe;
    private String  telephone;

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public Sexe getSexe() { return sexe; }
    public void setSexe(Sexe sexe) { this.sexe = sexe; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}
