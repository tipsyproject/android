package tipsy.commun.commerce;

/**
 * Created by valoo on 18/01/14.
 */
public abstract class WalletCallback {
    public abstract void onWait();

    public abstract void onSuccess();

    public abstract void onFailure(Exception e);
}
