package tipsy.app.orga.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import tipsy.app.R;
import tipsy.app.orga.OrgaActivity;
import tipsy.app.orga.acces.AccesActivity;
import tipsy.app.orga.billetterie.BilletterieActivity;
import tipsy.app.orga.event.edit.EditEventActivity;
import tipsy.commun.Event;

/**
 * Created by valoo on 22/01/14.
 */
public class EventOrgaActivity extends FragmentActivity implements EventOrgaListener {

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_billetterie);
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey("Event")){
            event =  savedInstanceState.getParcelable("Event");
        }else{
            event = getIntent().getParcelableExtra("Event");
            home(false);
        }
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(event.getNom());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(outState==null)
            outState = new Bundle();
        outState.putParcelable("Event", event);
        // Add variable to outState here
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                backToOrga();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Event getEvent(){
        return event;
    }

    public void home(boolean addTobackStack){
        HomeEventOrgaFragment frag = new HomeEventOrgaFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        if(addTobackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void backToOrga(){
        Intent intent = new Intent(this, OrgaActivity.class);
        startActivity(intent);
    }

    // clique sur le bouton de la Billetterie
    public void goToAcces() {
        Intent intent = new Intent(this, AccesActivity.class);
        intent.putExtra("Event", event);
        startActivity(intent);
    }

    public void goToBilletterie() {
        Intent intent = new Intent(this, BilletterieActivity.class);
        intent.putExtra("Event", event);
        startActivity(intent);
    }

    // Modification d'un événement
    public void goToEditEvent() {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("Event",event);
        startActivity(intent);
    }
}
