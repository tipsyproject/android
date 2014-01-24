package com.tipsy.lib;

import com.parse.ParseUser;

/**
 * Created by vquefele on 23/01/14.
 */
public class TipsyUser extends ParseUser {

    public static int MEMBRE = 1;
    public static int ORGA = 2;
    public TipsyUser(){}

    @Override
    public void setUsername(String username){
        super.setUsername(username);
        super.setEmail(username);
    }

    public String getNom() {
        return getString("nom");
    }

    public void setNom(String nom) {
        put("nom",nom);
    }

    public String getPrenom() {
        return getString("prenom");
    }

    public void setPrenom(String prenom) {
        put("prenom",prenom);
    }

    public int getType() {
        return getInt("type");
    }

    public void setType(int type) {
        put("type",type);
    }
}
