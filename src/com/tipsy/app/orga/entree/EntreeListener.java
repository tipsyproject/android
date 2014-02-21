package com.tipsy.app.orga.entree;

import com.tipsy.lib.Achat;
import com.tipsy.lib.util.EventModule;
import com.tipsy.lib.util.QueryCallback;

import java.util.ArrayList;

/**
 * Created by vquefele on 20/01/14.
 */
public interface EntreeListener extends EventModule {
    public ArrayList<Achat> getEntrees();

    public void init();

    public void updateEntrees(QueryCallback cb);

    public void updateProgress();

    public void backToEvent();
}
