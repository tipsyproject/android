package tipsy.app.billetterie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.ArrayList;

import tipsy.app.R;
import tipsy.commun.Billet;
import tipsy.commun.Billetterie;

/**
 * Created by valoo on 27/12/13.
 */
public class ListBilletsFragment extends Fragment {

    private BilletsArrayAdapter adapter;
    private Billetterie billetterie;
    private ListView listView;
    private BilletterieListener callback;

    public ListBilletsFragment(Billetterie b){
        super();
        billetterie = b;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (BilletterieListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_billetterie_list_billets, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        adapter = new BilletsArrayAdapter(getActivity(),billetterie.getBillets());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editBillet(billetterie.getBillets().get(position));
            }
        });
        return view;
    }

    // Redéfinition de l'actionBar: Bouton de validation
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_billetterie_list_billets, menu);
    }

    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_new_billet:
                editBillet(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void editBillet(Billet b){
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new EditBilletDialogFragment(b);
        dialog.show(getActivity().getSupportFragmentManager(), "EditBilletDialogFragment");
    }

    // Adapter BILLETS
    public class BilletsArrayAdapter extends ArrayAdapter<Billet> {
        private Context context;
        private ArrayList<Billet> billets;
        public BilletsArrayAdapter(Context context, ArrayList<Billet> billets) {
            super(context, R.layout.frag_billet_list, billets);
            this.context = context;
            this.billets = billets;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewBillet = inflater.inflate(R.layout.frag_billet_list, parent, false);
            TextView nomBillet = (TextView) viewBillet.findViewById(R.id.nom_billet);
            TextView prixBillet = (TextView) viewBillet.findViewById(R.id.prix_billet);
            nomBillet.setText(billets.get(position).getNom());
            prixBillet.setText(Double.toString(billets.get(position).getPrix()));
            return viewBillet;
        }
    }


    // Dialog Fragment permettant de créer/modifier un billet

    public class EditBilletDialogFragment extends DialogFragment implements  Validator.ValidationListener{

        private Billet billet = null;
        @Required(order=1)
        private EditText inputNom;
        @Required(order=2)
        @NumberRule(order=3, type=NumberRule.NumberType.DOUBLE)
        private EditText inputPrix;
        private Validator validator;

        public EditBilletDialogFragment(Billet b){
            super();
            billet = b;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            // Definition du titre du Dialog
            builder.setCustomTitle(inflater.inflate(R.layout.frag_edit_billet_title, null));
            // Definition du contenu du Dialog
            final View view = inflater.inflate(R.layout.frag_edit_billet, null);
            builder.setView(view);

            inputNom = (EditText) view.findViewById(R.id.input_nom);
            inputPrix = (EditText) view.findViewById(R.id.input_prix);
            // Préremplissage des widgets avec les valeur du billet si c'est une modification
            // sinon rien pour une creation
            if(billet != null){
                inputNom.setText(billet.getNom());
                inputPrix.setText(Double.toString(billet.getPrix()));
            }


            validator = new Validator(this);
            validator.setValidationListener(this);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Validation du formulaire de Billet
                    validator.validate();
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

        public void onValidationSucceeded() {
            // On recupère le contenu des champs
            String nomBillet = inputNom.getText().toString();
            double prixBillet = Double.parseDouble(inputPrix.getText().toString());

            // Puis on enregistre les modifs

            // Si c'est une creation de billet
            if(billet == null){
                billetterie.creerBillet(nomBillet,prixBillet);
            }else{
                billet.setNom(nomBillet);
                billet.setPrix(prixBillet);
            }
            adapter.notifyDataSetChanged();
            billetterie.save(StackMobOptions.depthOf(2), new StackMobModelCallback() {
                @Override
                public void success() {
                }

                @Override
                public void failure(StackMobException e) {
                }
            });

        }

        public void onValidationFailed(View failedView, Rule<?> failedRule) {
            String message = failedRule.getFailureMessage();
            if (failedView instanceof EditText) {
                failedView.requestFocus();
                ((EditText) failedView).setError(message);
            } else {
            }
        }
    }

}