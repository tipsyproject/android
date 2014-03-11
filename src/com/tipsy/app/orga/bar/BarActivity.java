package com.tipsy.app.orga.bar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Ticket;

/**
 * Created by tech on 10/03/14.
 */
public class BarActivity extends FragmentActivity {

    public final static int HARD = 0;
    public final static int SOFT = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_bar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Bar");
        super.onCreate(savedInstanceState);

        Bundle args = new Bundle();
        args.putString("EVENT_ID", getIntent().getStringExtra("EVENT_ID"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
    }

    public void onClickHard(View view) {
        goToConso(Ticket.CONSO_HARD);
    }

    public void onClickSoft(View view) {
        goToConso(Ticket.CONSO_SOFT);
    }


    public void goToHomeBar() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new BarHomeFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void goToConso(int index) {
        BarConsoFragment fragment = new BarConsoFragment();
        Bundle args = new Bundle();
        args.putInt("BILLET_INDEX", index);
        fragment.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
