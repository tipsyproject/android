package tipsy.app.membre;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.commun.billetterie.Billet;
import tipsy.commun.billetterie.Billetterie;
import tipsy.commun.commerce.Item;
import tipsy.commun.commerce.ItemArrayAdapter;
import tipsy.commun.commerce.Panier;
import tipsy.commun.commerce.QuantiteDialogFragment;

/**
 * Created by Valentin on 30/12/13.
 */
public class EventBilletsFragment extends Fragment {

    private Panier panier;
    private ItemArrayAdapter adapter;
    private Billetterie billetterie;
    private ArrayList<Item> items;
    private ListView listView;
    private MembreListener callback;


    public static EventBilletsFragment init(Billetterie<Billet> b) {
        EventBilletsFragment frag = new EventBilletsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("Billetterie", b);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MembreListener) activity;
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        panier = app.getPanier();
        panier.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        billetterie = (Billetterie) getArguments().getParcelableArrayList("Billetterie");
        items = billetterie.getItems(panier);

        View view = inflater.inflate(R.layout.frag_event_billets, container, false);
        listView = (ListView) view.findViewById(R.id.list);

        panier.setPrixTotalView((TextView) view.findViewById(R.id.prix_total));

        adapter = new ItemArrayAdapter(getActivity(), items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setNombreBillets(items.get(position));
            }
        });

        Button buttonPay = (Button) view.findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (panier.size() == 0)
                    Toast.makeText(getActivity(), "Panier vide !", Toast.LENGTH_SHORT).show();
                else
                    callback.goToCommande();
            }
        });
        return view;
    }

    // NumberPicker pour définir le nombre de billets choisis
    public void setNombreBillets(Item item) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new QuantiteDialogFragment(adapter, item);
        dialog.show(getActivity().getSupportFragmentManager(), "NombreBilletDialogFragment");
    }


}
