package tipsy.app.billetterie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tipsy.app.R;
import tipsy.commun.commerce.Item;

/**
 * Created by valoo on 20/01/14.
 */
public class BilletArrayAdapter extends ArrayAdapter<Item> {
    private Context context;
    private ArrayList<Item> ventes;

    public BilletArrayAdapter(Context context, ArrayList<Item> ventes) {
        super(context, R.layout.frag_billetterie_vente, ventes);
        this.context = context;
        this.ventes = ventes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewVente = inflater.inflate(R.layout.frag_billetterie_vente, parent, false);
        TextView nomBillet = (TextView) viewVente.findViewById(R.id.nom_billet);
        TextView quantite = (TextView) viewVente.findViewById(R.id.quantite);
        Item item = ventes.get(position);
        nomBillet.setText(item.getProduit().getNom());
        quantite.setText(Integer.toString(item.getQuantite()));
        return viewVente;
    }
}