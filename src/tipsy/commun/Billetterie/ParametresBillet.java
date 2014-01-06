package tipsy.commun.Billetterie;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by vquefele on 06/01/14.
 */
public class ParametresBillet extends StackMobModel{
    private int nbMax = 0;

    public interface ParamBillet{
        public int getNbMax();
        public void setNbMax(int nbMax);
    }

    public ParametresBillet(){
        super(ParametresBillet.class);
    }

    public int getNbMax() {
        return nbMax;
    }

    public void setNbMax(int nbMax) {
        this.nbMax = nbMax;
    }

    // Nom du schema dans Stackmob
    public static String overrideSchemaName() {
        return "billet";
    }
}
