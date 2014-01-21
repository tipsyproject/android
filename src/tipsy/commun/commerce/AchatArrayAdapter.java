package tipsy.commun.commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tipsy.app.R;

/**
 * Created by valoo on 20/01/14.
 */
public class AchatArrayAdapter extends ArrayAdapter<Achat> {
    private Context context;
    private ArrayList<Achat> ventes;

    public AchatArrayAdapter(Context context, ArrayList<Achat> ventes) {
        super(context, R.layout.frag_achat_item, ventes);
        this.context = context;
        this.ventes = ventes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewVente = inflater.inflate(R.layout.frag_achat_item, parent, false);
        TextView nomAchat = (TextView) viewVente.findViewById(R.id.nom_achat);
        TextView nomParticipant = (TextView) viewVente.findViewById(R.id.nom_participant);
        TextView prenomParticipant = (TextView) viewVente.findViewById(R.id.prenom_participant);
        Achat achat = ventes.get(position);
        nomAchat.setText(achat.getProduit().getNom());
        nomParticipant.setText(achat.getParticipant().getNom());
        prenomParticipant.setText(achat.getParticipant().getPrenom());
        return viewVente;
    }
}
