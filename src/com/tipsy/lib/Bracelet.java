package com.tipsy.lib;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by valoo on 22/01/14.
 */
@ParseClassName("Bracelet")
public class Bracelet extends ParseObject {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public Bracelet(){}

    public Bracelet(String tagID){
        setTagID(tagID);
    }

    public Participant getParticipant() {
        return (Participant) getParseObject("participant");
    }

    public void setParticipant(Participant participant) {
        put("participant", participant);
    }

    public TipsyUser getUser() {
        return (TipsyUser) getParseObject("user");
    }

    public void setUser(TipsyUser user) {
        put("user",user);
    }

    public String getTagID() {
        return getString("tag");
    }

    protected void setTagID(String tagID) {
        put("tag",tagID);
    }

    public boolean isFree(){
        return getUser() == null && getParticipant() == null;
    }

    public void giveMeMyFreedom(){
        remove("user");
        remove("participant");
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    /* Recherche un bracelet par son tagID */
    public static void isKnown(String tagID, final GetBraceletCallback callback){
        ParseQuery<Bracelet> query = ParseQuery.getQuery(Bracelet.class);
        query.include("user");
        query.include("participant");
        query.whereEqualTo("tag",tagID);
        query.findInBackground(new FindCallback<Bracelet>() {
            @Override
            public void done(List<Bracelet> bracelets, ParseException e) {
                if(bracelets != null && !bracelets.isEmpty()){
                    callback.done(bracelets.get(0), e);
                }else callback.done(null, e);
            }
        });

    }


    public static abstract class GetBraceletCallback {
        public abstract void done(Bracelet bracelet, ParseException e);
    }
}
