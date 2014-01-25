package com.tipsy.lib;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by valoo on 22/01/14.
 */
@ParseClassName("Bracelet")
public class Bracelet extends ParseObject {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public Bracelet(){}

    public TipsyUser getUser() {
        return (TipsyUser) getParseObject("user");
    }

    public void setUser(TipsyUser user) {
        put("user",user);
    }

    public Participant getParticipant() {
        return (Participant) getParseObject("participant");
    }

    public void setParticipant(Participant participant) {
        put("participant",participant);
    }

    public String getTagID() {
        return getString("tag");
    }

    protected void setTagID(String tagID) {
        put("tag",tagID);
    }

    public void setTagID(byte[] bytes) {
        setTagID(bytesToHex(bytes));
    }

    public boolean isFree(){
        return ( getUser() == null && getParticipant() == null );
    }

    public boolean isMembre(){
        return getUser() != null;
    }

    public boolean isParticipant(){
        return getParticipant() != null;
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
}
