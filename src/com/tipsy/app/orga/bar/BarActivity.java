package com.tipsy.app.orga.bar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.lib.Event;
import com.tipsy.lib.Ticket;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;

/**
 * Created by tech on 10/03/14.
 */
public class BarActivity extends FragmentActivity implements BarListener {

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
        goToHomeBar();
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
        ft.commit();
    }

    public void goToConso(int index) {
        BarConsoFragment fragment = new BarConsoFragment();
        Bundle args = new Bundle();
        args.putInt("CONSO_INDEX", index);
        args.putString("EVENT_ID", getIntent().getStringExtra("EVENT_ID"));
        fragment.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public Event getEvent() {
        return null;
    }

    @Override
    public ArrayList<Ticket> getBilletterie() {
        return null;
    }

    @Override
    public ArrayList<Ticket> getConso() {
        return null;
    }

    @Override
    public void loadEventBilletterie(String eventId, QueryCallback callback) {

    }
}
