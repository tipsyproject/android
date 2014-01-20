package tipsy.commun.billetterie;

import com.stackmob.sdk.model.StackMobModel;

import tipsy.commun.Event;
import tipsy.commun.Membre;
import tipsy.commun.commerce.Produit;

/**
 * Created by vquefele on 20/01/14.
 */
public class Participation extends StackMobModel {

    private Participant participant = null;
    private Membre membre = null;
    private Produit billet;
    private Event event;

    public Participation(){
        super(Participation.class);
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Produit getBillet() {
        return billet;
    }

    public void setBillet(Produit billet) {
        this.billet = billet;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public String getEmail(){
        return membre == null ? participant.getEmail() : membre.getEmail();
    }

    public String getNom(){
        return membre == null ? participant.getNom() : membre.getNom();
    }

    public String getPrenom(){
        return membre == null ? participant.getPrenom() : membre.getPrenom();
    }

    public boolean isParticipantDefined(){
        return (membre != null || participant != null );
    }
}
