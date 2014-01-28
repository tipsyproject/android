package com.tipsy.app.membre.event;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.tipsy.app.R;
import com.tipsy.lib.util.Commande;
import com.tipsy.lib.util.Item;
import com.tipsy.lib.util.ItemArrayAdapter;
import com.tipsy.lib.util.Panier;
import com.tipsy.lib.Ticket;

/**
 * Created by Valentin on 30/12/13.
 */
public class EventBilletsFragment extends Fragment {

    private Panier panier = new Panier();
    private ItemArrayAdapter adapter;
    private ListView listView;
    private TextView totalView;
    private EventMembreListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (EventMembreListener) activity;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putParcelable("Panier", panier);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            panier = new Panier();
            for(Ticket t : callback.getBilletterie()){
                panier.add(new Item(t,0));
            }
        }else{
            panier = savedInstanceState.getParcelable("Panier");
        }

        View view = inflater.inflate(R.layout.frag_event_billets, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        totalView = (TextView) view.findViewById(R.id.prix_total);

        adapter = new ItemArrayAdapter(getActivity(), panier, totalView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setNombreBillets(panier.get(position));
            }
        });

        Button buttonPay = (Button) view.findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (panier.isEmpty())
                    Toast.makeText(getActivity(), "Panier vide !", Toast.LENGTH_SHORT).show();
                else
                    callback.goToParticiper(new Commande(panier));
            }
        });
        return view;
    }


    // NumberPicker pour définir le nombre de billets choisis
    public void setNombreBillets(Item item) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new QuantiteDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("Item", item);
        args.putSerializable("Adapter", adapter);
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "NombreBilletDialogFragment");
    }

    // Dialog Fragment permettant de choisir le nombre d'item voulu
    public static class QuantiteDialogFragment extends DialogFragment {

        private Item item;
        private ItemArrayAdapter adapter;
        private NumberPicker quantite = null;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            item = getArguments().getParcelable("Item");
            adapter = (ItemArrayAdapter) getArguments().getSerializable("Adapter");

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            // Definition du titre du Dialog
            builder.setTitle("Quantité");
            // Definition du contenu du Dialog
            View view = inflater.inflate(R.layout.frag_item_quantite_picker, null);
            quantite = (NumberPicker) view.findViewById(R.id.quantite);
            quantite.setMinValue(0);
            quantite.setMaxValue(99);
            quantite.setValue(item.getQuantite());
            builder.setView(view);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    item.setQuantite(quantite.getValue());
                    adapter.notifyDataSetChanged();
                }
            })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


}
