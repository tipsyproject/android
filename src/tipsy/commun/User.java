package tipsy.commun;

import android.app.Activity;
import android.content.Context;

import com.stackmob.sdk.model.StackMobUser;

import java.io.FileOutputStream;

/**
 * Created by vquefelec on 11/12/13.
 */
public abstract class User extends StackMobUser implements UtilisateurTipsy {

    private static String lastUserFilename = "lastlogin";
    private static boolean loggedIn = false;
    protected String nom = null;

    /* CONSTRUCTEUR */
    public User(String username, String password) {
        super(User.class, username, password);
    }

    public String getEmail(){
        return this.getUsername();
    }

    public void remember(Context context){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(lastUserFilename, Context.MODE_PRIVATE);
            outputStream.write(this.getEmail().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLastUserFilename(Activity act){
        //A compléter
        return lastUserFilename;
    }

    public static void loggingIn(){
        User.loggedIn = true;
    }

    public static void loggingOut(){
        User.loggedIn = false;
    }
}
