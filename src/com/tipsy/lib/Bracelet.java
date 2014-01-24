package com.tipsy.lib;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by valoo on 22/01/14.
 */
public class Bracelet extends StackMobModel {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private Membre membre = null;
    private Participant participant = null;
    private String tagID;

    public Bracelet(){
        super(Bracelet.class);
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public String getTagID() {
        return tagID;
    }

    protected void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public void setTagID(byte[] bytes) {
        this.tagID = bytesToHex(bytes);
    }

    public boolean isFree(){
        return ( membre == null && participant == null );
    }

    public boolean isMembre(){
        return membre != null;
    }

    public boolean isParticipant(){
        return participant != null;
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
