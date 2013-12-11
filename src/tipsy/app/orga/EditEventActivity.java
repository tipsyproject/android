package tipsy.app.orga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import tipsy.app.R;

/**
 * Created by Valoo on 05/12/13.
 */

public class EditEventActivity extends FragmentActivity {
    // Nombre de colonnes
    static final int NUM_ITEMS = 4;
    // Gestionnaires de vues
    MyAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orga_editer_event);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.event_pager);
        mPager.setAdapter(mAdapter);
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        private static final String[] titres = { "Description", "Localisation", "Date", "Paramètres" };

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        // Affiche le fragment voulu en fonction de la position
        public Fragment getItem(int position) {
            // Dans l'ordre de gauche à droite
            switch(position){
                case 0:
                    return EditEventDescFragment.init(position);
                case 1:
                    return EditEventLocFragment.init(position);
                case 2:
                    return EditEventDateFragment.init(position);
                default:
                    return EditEventSettingsFragment.init(position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titres[position];
        }
    }

    public static class ArrayListFragment extends ListFragment {
        int mNum;
        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();
            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        // When creating, retrieve this instance's number from its arguments.
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_orga_editer_event, container, false);
            return v;
        }

    }
}
