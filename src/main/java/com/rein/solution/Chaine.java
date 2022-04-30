/**
 *
 * @author Mathis
 * Project : rein-poo-le4
 * Chaine in transplantation
 * Description : Chaine class
 */

package com.rein.solution;

import com.rein.instance.Altruiste;
import com.rein.transplantation.Sequence;

public class Chaine extends Sequence {
    private Altruiste altruiste;

    public Chaine(int tailleMax,Altruiste a){
        super();
        this.setTailleMaxSequence(tailleMax);
        this.altruiste = a;
        super.getListeNoeuds().add(a);
    }
    public Altruiste getAltruiste() {
        return altruiste;
    }

    public void setAltruiste(Altruiste altruiste) {
        this.altruiste = altruiste;
    }

    @Override
    public String toString() {
        return "Chaine { altruiste=" + altruiste.toString() + " listeNoeuds = "+this.getListeNoeuds()+'}';
    }

}
