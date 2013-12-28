package tipsy.commun;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.api.StackMobQueryField;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.model.StackMobUser;
import com.sveinungkb.SecurePreferences;

import java.util.List;

import tipsy.app.LoginActivity;
import tipsy.app.TipsyApp;
import tipsy.app.membre.MembreActivity;
import tipsy.app.orga.OrgaActivity;


/**
 * Created by vquefelec on 11/12/13.
 */
public class User extends StackMobUser {
    protected int type;

    public User(String username, String password) {
        super(User.class, username, password);
    }

    public User(String username, String password, int type) {
        super(User.class, username, password);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getEmail() {
        return getUsername();
    }

    public void goHome(Activity a) {
        if (getType() == TypeUser.ORGANISATEUR)
            a.startActivity(new Intent(a, OrgaActivity.class));
        else if (getType() == TypeUser.MEMBRE)
            a.startActivity(new Intent(a, MembreActivity.class));
        else
            a.startActivity(new Intent(a, LoginActivity.class));
    }

    // Enregistre les identifiants de l'utilisateur dans le répertoire privé de l'appli
    public static void rememberMe(Activity a, String username, String password) {
        SecurePreferences prefs = new SecurePreferences(a.getApplicationContext(), "user", "7D465F9D5EA775D1C25FEFF588848", true);
        prefs.put(Prefs.USERNAME, username);
        prefs.put(Prefs.PASSWORD, password);
    }

    // Supprime les identifiants de l'utilisateur dans le répertoire privé de l'appli
    public static void forgetMe(Activity a) {
        SecurePreferences prefs = new SecurePreferences(a.getApplicationContext(), "user", "7D465F9D5EA775D1C25FEFF588848", true);
        prefs.put(Prefs.USERNAME, "");
        prefs.put(Prefs.PASSWORD, "");
    }

    // Enregistre les identifiants de l'utilisateur dans le répertoire privé de l'appli
    public static User doYouRememberMe(Activity a) {
        SecurePreferences prefs = new SecurePreferences(a.getApplicationContext(), "user", "7D465F9D5EA775D1C25FEFF588848", true);
        String username = prefs.getString(Prefs.USERNAME);
        String password = prefs.getString(Prefs.PASSWORD);
        if (username != null && password != null) {
            return new User(username, password);
        } else return null;
    }

    public static void keepCalmAndWaitForGoingHome(Activity act, User u) {
        final Activity a = act;
        final User user = u;
        if (user.getType() == TypeUser.ORGANISATEUR) {
            Organisateur.query(Organisateur.class,
                    new StackMobQuery().field(new StackMobQueryField("user").isEqualTo(user.getUsername())),
                    new StackMobQueryCallback<Organisateur>() {
                        @Override
                        public void success(List<Organisateur> result) {
                            final Organisateur orga = result.get(0);
                            orga.fetch(StackMobOptions.depthOf(3), new StackMobModelCallback() {
                                @Override
                                public void success() {
                                    TipsyApp app = (TipsyApp) a.getApplication();
                                    app.setOrga(orga);
                                    user.goHome(a);
                                }

                                @Override
                                public void failure(StackMobException e) {
                                    Log.d("TOUTAFAIT", "fetch orga KO:" + e.getMessage());
                                }
                            });
                        }

                        @Override
                        public void failure(StackMobException e) {
                            Log.d("TOUTAFAIT", "query association user/orga:" + e.getMessage());
                        }
                    });

        } else if (user.getType() == TypeUser.MEMBRE) {
            Membre.query(Membre.class,
                    new StackMobQuery().field(new StackMobQueryField("user").isEqualTo(user.getUsername())),
                    new StackMobQueryCallback<Membre>() {
                        @Override
                        public void success(List<Membre> result) {
                            final Membre membre = result.get(0);
                            membre.fetch(new StackMobModelCallback() {
                                @Override
                                public void success() {
                                    TipsyApp app = (TipsyApp) a.getApplication();
                                    app.setMembre(membre);
                                    user.goHome(a);
                                }

                                @Override
                                public void failure(StackMobException e) {
                                    Log.d("TOUTAFAIT", "fetch membre KO:" + e.getMessage());
                                }
                            });
                        }

                        @Override
                        public void failure(StackMobException e) {
                            Log.d("TOUTAFAIT", "query association user/membre:" + e.getMessage());
                        }
                    });
        } else {
            a.startActivity(new Intent(a, LoginActivity.class));
        }
    }

    // Si l'utilisateur est déjà connecté il est redirigé vers son tableau de bord
    // Sinon on regarde si on peut le reconnecter automatiquement:
    //  Si on n'y parvient pas on le redirige vers la page d'authentification
    //  Sinon...
    public static void tryLogin(Activity act) {
        final Activity a = act;
        User.getLoggedInUser(User.class, new StackMobQueryCallback<User>() {
            @Override
            public void success(List<User> list) {
                User user = list.get(0);
                User.keepCalmAndWaitForGoingHome(a, user);
            }

            @Override
            public void failure(StackMobException e) {
                final User user = doYouRememberMe(a);
                if (user == null) {
                    a.startActivity(new Intent(a, LoginActivity.class));
                } else {
                    user.login(new StackMobModelCallback() {
                        @Override
                        public void success() {
                            // initialisation de la session utilisateur
                            // et redirection vers tableau de bord
                            User.keepCalmAndWaitForGoingHome(a, user);
                        }

                        /* En cas d'echec, redirection vers LoginActivity */
                        @Override
                        public void failure(StackMobException e) {
                            a.startActivity(new Intent(a, LoginActivity.class));
                        }
                    });
                }
            }
        });

    }


    public interface TipsyUser {
        public int getType();

        public User getUser();

        public void save(StackMobCallback callback);
    }

}