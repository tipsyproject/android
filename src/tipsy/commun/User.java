package tipsy.commun;

import com.stackmob.sdk.model.StackMobUser;


/**
 * Created by vquefelec on 11/12/13.
 */
public class User extends StackMobUser {
    protected int type;

    public User(String username, String password){
        super(User.class, username, password);
    }

    public User(String username, String password, int type){
        super(User.class, username, password);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getEmail(){
        return getUsername();
    }

}
