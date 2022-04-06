/**
 *
 * @author Mathis
 * Project : rein-poo-le4
 * Chaine in transplantation
 * Description : Chaine class
 */

package com.rein.transplantation;

import com.rein.elements.Altruiste;

public class Chaine extends Situation{

    private int id;
    private int tailleMax;
    private Altruiste altruiste;

    public int getId() {
        return id;
    }

    public int getTailleMax() {
        return tailleMax;
    }

    public void setTailleMax(int tailleMax) {
        this.tailleMax = tailleMax;
    }

    public Altruiste getAltruiste() {
        return altruiste;
    }

    public void setAltruiste(Altruiste altruiste) {
        this.altruiste = altruiste;
    }

    @Override
    public String toString() {
        return "Chaine {" +
                "id=" + id +
                ", tailleMax=" + tailleMax +
                ", altruiste=" + altruiste.toString() +
                '}';
    }
}
