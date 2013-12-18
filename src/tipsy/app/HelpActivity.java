package tipsy.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.Vector;

import tipsy.commun.Prefs;

/**
 * Created by Alexandre on 12/12/13.
 */
public class HelpActivity extends FragmentActivity {

    private PagerAdapter mPagerAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.act_help);
        // Création de la liste de Fragments que fera défiler le PagerAdapter
        List fragments = new Vector();

        // Ajout des Fragments dans la liste
        fragments.add(Fragment.instantiate(this, Help1Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Help2Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Help3Fragment.class.getName()));

        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager) super.findViewById(R.id.pager);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                prefs.edit()
                        .putBoolean(Prefs.SKIP_HELP, true)
                        .commit();
                LoginActivity.rememberMe(HelpActivity.this);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        //On fournit à l'adapter la liste des fragments à afficher
        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

}
