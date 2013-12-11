package tipsy.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.List;
import java.util.Vector;

import tipsy.app.orga.*;
import tipsy.commun.Organisateur;

/**
 * Created by Alexandre on 04/12/13.
 */

public class MainActivity extends FragmentActivity {

    private PagerAdapter mPagerAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initialisation de STACKMOB avec la clé publique
        StackMobAndroid.init(getApplicationContext(), 0, "eeedff37-f59d-408a-9279-27cd8fe7062e");

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_screen_slide);

        // Création de la liste de Fragments que fera défiler le PagerAdapter
        List fragments = new Vector();

        // Ajout des Fragments dans la liste
        fragments.add(Fragment.instantiate(this, Help_Main.class.getName()));
        fragments.add(Fragment.instantiate(this, Help_Two.class.getName()));
        fragments.add(Fragment.instantiate(this, Help_Three.class.getName()));

        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager) super.findViewById(R.id.pager);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);

        final Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(StackMob.getStackMob().isLoggedIn()) {
                    Organisateur.getLoggedInUser(Organisateur.class, new StackMobQueryCallback<Organisateur>() {
                        @Override
                        public void success(List<Organisateur> list) {
                            Organisateur loggedInUser = list.get(0);
                            startActivity(new Intent(MainActivity.this, tipsy.app.orga.HomeActivity.class));
                        }

                        @Override
                        public void failure(StackMobException e) {
                            Log.d("Connexion Orga", e.getMessage());
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                    });
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
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
