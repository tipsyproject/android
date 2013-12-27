package tipsy.app.billetterie;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mobsandgeeks.saripaar.Validator;

import tipsy.app.R;
import tipsy.commun.Billetterie;

/**
 * Created by valoo on 27/12/13.
 */
public class ListBilletsFragment extends Fragment {

    private Billetterie billetterie;

    public ListBilletsFragment(Billetterie b){
        super();
        billetterie = b;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_billetterie_list_billets, container, false);

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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
