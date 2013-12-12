package tipsy.commun;

import com.stackmob.sdk.model.StackMobUser;


/**
 * Created by vquefelec on 11/12/13.
 */
public class User extends StackMobUser {

    protected String nom;
    protected int type;

    public User(String username, String password){
        super(User.class, username, password);
    }

    public User(String username, String password, String nom){
        super(User.class, username, password);
        this.nom = nom;
    }

    public int getType() {
        return type;
    }

    public String getEmail(){
        return getUsername();
    }

    /*
    public void login(){
        try{
            super.login(new StackMobModelCallback() {
                @Override
                public void success() {
                    fetch(new StackMobModelCallback() {
                        @Override
                        public void success() {
                        }
                        @Override
                        public void failure(StackMobException e) {
                        }
                    });
                }
                @Override
                public void failure(StackMobException e) {
                }
            });
        }catch(Exception e){

        }
    }*/

}
