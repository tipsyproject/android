package com.tipsy.app.orga.vestiaire;

import android.app.Activity;
import android.os.Bundle;

import com.tipsy.app.R;

/**
 * Created by tech on 10/03/14.
 */
public class VestiaireActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.animator.activity_open_translate, R.animator.activity_close_scale);
        setContentView(R.layout.act_vestiaire);
        super.onCreate(savedInstanceState);
    }
}
