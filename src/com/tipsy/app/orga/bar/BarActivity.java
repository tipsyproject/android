package com.tipsy.app.orga.bar;

import android.app.Activity;
import android.os.Bundle;

import com.tipsy.app.R;

/**
 * Created by tech on 10/03/14.
 */
public class BarActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_bar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Bar");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_close_translate);
    }
}
