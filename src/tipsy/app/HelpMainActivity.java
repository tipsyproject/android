package tipsy.app;

import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by Alexandre on 12/12/13.
 */
public class HelpMainActivity extends FragmentActivity {

    private PagerAdapter mPagerAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        // Création de la liste de Fragments que fera défiler le PagerAdapter
        List fragments = new Vector();

        // Ajout des Fragments dans la liste
        fragments.add(Fragment.instantiate(this, HelpOneFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, HelpTwoFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, HelpThreeFragment.class.getName()));

        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager) super.findViewById(R.id.pager);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);

        final Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(HelpMainActivity.this, HomeAnonymousActivity.class));
                finish();
            }
        });
        final Button inscription_or_connect = (Button) findViewById(R.id.inscription_or_connect);
        inscription_or_connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(HelpMainActivity.this, LoginActivity.class));
            }
        });
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
