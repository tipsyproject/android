package tipsy.app.orga;

import android.app.ActionBar;
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

import tipsy.app.R;

/**
 * Created by Valoo on 05/12/13.
 */

public class EditEventActivity extends FragmentActivity implements ActionBar.TabListener {
    // Nombre de colonnes
    static final int NUM_ITEMS = 4;
    private static final int[] icones = {
            R.drawable.ic_action_about,
            R.drawable.ic_action_place,
            R.drawable.ic_action_go_to_today,
            R.drawable.ic_action_settings
    };
    // Gestionnaires de vues
    MyAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orga_editer_event);

        final ActionBar actionBar = getActionBar();

        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.event_pager);
        mPager.setAdapter(mAdapter);

        actionBar.setHomeButtonEnabled(false);
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (int icon : icones) {
            actionBar.addTab(actionBar.newTab().setIcon(icon).setTabListener(this));
        }


        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    public static class MyAdapter extends FragmentPagerAdapter {


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
