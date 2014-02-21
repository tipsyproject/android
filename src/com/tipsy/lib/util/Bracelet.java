package com.tipsy.lib.util;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tipsy.lib.Participant;
import com.tipsy.lib.TipsyUser;

import java.util.List;

/**
 * Created by valoo on 22/01/14.
 */

public class Bracelet {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();


    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    /* Recherche un bracelet par son tagID */
    public static void isUsed(final String tagID, final GetBraceletCallback callback) {
        ParseQuery<Participant> query = ParseQuery.getQuery(Participant.class);
        query.whereEqualTo("bracelet", tagID);
        query.findInBackground(new FindCallback<Participant>() {
            @Override
            public void done(List<Participant> participants, ParseException e) {
                if (participants != null && !participants.isEmpty()) {
                    callback.done(true, e);
                } else {
                    ParseQuery<TipsyUser> query = ParseQuery.getQuery(TipsyUser.class);
                    query.whereEqualTo("bracelet", tagID);
                    query.findInBackground(new FindCallback<TipsyUser>() {
                        @Override
                        public void done(List<TipsyUser> users, ParseException e) {
                            if (users != null && !users.isEmpty()) {
                                callback.done(true, e);
                            } else callback.done(false, e);
                        }
                    });

                }
            }
        });

    }


    public static abstract class GetBraceletCallback {
        public abstract void done(boolean used, ParseException e);
    }
}
