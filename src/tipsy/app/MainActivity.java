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

import com.stackmob.android.sdk.common.StackMobAndroid;
import java.util.List;
import java.util.Vector;

import tipsy.commun.TypeUtilisateur;
import tipsy.commun.User;

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

        // Si l'utilisateur n'a jamais vu le Help
        // ou qu'il demande à y accéder depuis 'Aide'
        if (!Help.skipHelp(this)){
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
                    Help.setSkip(MainActivity.this,true);
                    nextStep();
                }
            });

        }else{
            nextStep();
        }

    }

    private void nextStep(){
        User user = new User(User.getLastUsername(this));
        /* S'il n'y pas de précédent user, on redirige vers le login */
        if( user.getUsername().equals("") || !user.isLoggedIn()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }else {
            if(user.getType() == TypeUtilisateur.ORGANISATEUR){
                startActivity(new Intent(MainActivity.this, tipsy.app.orga.HomeActivity.class));
            }
            else{
                startActivity(new Intent(MainActivity.this, tipsy.app.HomeActivity.class));
            }
        }
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
