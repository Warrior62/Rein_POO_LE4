/**
 *
 * @author Mathis
 * Project : rein-poo-le4
 * Cycle in transplantation
 * Description : Cycle class
 */

package com.rein.transplantation;

public class Cycle extends Sequence {

    private int id;
    private int tailleMax;

    public int getId() {
        return id;
    }

    public int getTailleMax() {
        return tailleMax;
    }

    public void setTailleMax(int tailleMax) {
        this.tailleMax = tailleMax;
    }

    @Override
    public String toString() {
        return "Cycle {" +
                "id=" + id +
                ", tailleMax=" + tailleMax +
                '}';
    }
}
