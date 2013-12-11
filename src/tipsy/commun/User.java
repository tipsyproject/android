package tipsy.commun;

import android.content.Context;

import com.stackmob.sdk.model.StackMobUser;

import java.io.FileOutputStream;

/**
 * Created by vquefelec on 11/12/13.
 */
public abstract class User extends StackMobUser implements UtilisateurTipsy {

    protected String nom = null;

    /* CONSTRUCTEUR */
    public User(String username, String password) {
        super(User.class, username, password);
    }

    /********************************
    * Retourne le nom du fichier    *
    * contenant les paramètres User *
    ********************************/
    public static String getConfigFilename(){
        return "lastlogin";
    }

    public String getEmail(){
        return this.getUsername();
    }

    public void remember(Context context){
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(getConfigFilename(), Context.MODE_PRIVATE);
            outputStream.write(this.getEmail().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
