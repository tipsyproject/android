package tipsy.app.membre;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tipsy.app.R;
import tipsy.app.TipsyApp;
import tipsy.commun.Billetterie;
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

    public EventBilletsFragment(Billetterie b) {
        super();
        this.billetterie = b;
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
        View view = inflater.inflate(R.layout.frag_event_billets, container, false);
        listView = (ListView) view.findViewById(R.id.list);

        panier.setPrixTotalView((TextView) view.findViewById(R.id.prix_total));

        items = billetterie.getItems(panier);
        adapter = new ItemArrayAdapter(getActivity(), items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setNombreBillets(items.get(position));
            }
        });
        return view;
    }

    // NumberPicker pour définir le nombre de billets choisis
    public void setNombreBillets(Item item){
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new QuantiteDialogFragment(adapter,item);
        dialog.show(getActivity().getSupportFragmentManager(), "NombreBilletDialogFragment");
    }







}
