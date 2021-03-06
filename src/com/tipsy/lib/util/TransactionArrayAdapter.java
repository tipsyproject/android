package com.tipsy.lib.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tipsy.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by valoo on 04/01/14.
 */

// Adapter Item
public class TransactionArrayAdapter extends ArrayAdapter<Transaction> {
    private Context context;
    private ArrayList<Transaction> transactions;

    public TransactionArrayAdapter(Context context, ArrayList<Transaction> transactions) {
        super(context, R.layout.frag_transaction, transactions);
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Définition de la vue
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.frag_transaction, parent, false);

        // Item courant
        Transaction transaction = transactions.get(position);

        TextView titre = (TextView) view.findViewById(R.id.titre);
        titre.setText(transaction.getTitre());

        TextView sousTitre = (TextView) view.findViewById(R.id.sous_titre);
        sousTitre.setText(transaction.getDescription());

        /* Montant transaction */
        TextView montant = (TextView) view.findViewById(R.id.montant);
        String signe = transaction.isDepot() ? "" : "-";
        montant.setText(signe + Commerce.prixToString(transaction.getMontant(), transaction.getDevise()));

        /* Date transaction */
        TextView date = (TextView) view.findViewById(R.id.date);
        SimpleDateFormat ft = new SimpleDateFormat("dd MMM");
        date.setText(ft.format(transaction.getCreatedAt()));

        return view;
    }
}
