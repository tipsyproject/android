package tipsy.commun.billetterie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tipsy.app.R;
import tipsy.commun.commerce.Achat;

/**
 * Created by valoo on 20/01/14.
 */
public class EntreeArrayAdapter extends ArrayAdapter<Achat> {
    private Context context;
    private ArrayList<Achat> entrees;

    public EntreeArrayAdapter(Context context, ArrayList<Achat> entrees) {
        super(context, R.layout.frag_entree, entrees);
        this.context = context;
        this.entrees = entrees;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.frag_entree, parent, false);

        Achat entree = entrees.get(position);

        TextView prenomParticipant = (TextView) view.findViewById(R.id.prenom_participant);
        prenomParticipant.setText(entree.getParticipant().getPrenom());

        TextView nomParticipant = (TextView) view.findViewById(R.id.nom_participant);
        nomParticipant.setText(entree.getParticipant().getNom());

        TextView nomBillet = (TextView) view.findViewById(R.id.nom_billet);
        nomBillet.setText(entree.getProduit().getNom());
        return view;
    }
}
