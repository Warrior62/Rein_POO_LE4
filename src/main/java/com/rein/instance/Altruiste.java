package com.rein.instance;

import com.rein.solution.Chaine;

public class Altruiste extends Noeud {
    private Chaine chaine;

    public Altruiste(int id) {
        super(id);
    }

    public Altruiste(Altruiste a) {
        super(a.getId());
        this.setListeEchanges(a.getListeEchanges());
        this.setChaine(a.getChaine());
    }

    public void setChaine(Chaine chaine) {
        this.chaine = chaine;
    }

    public Chaine getChaine() {
        return chaine;
    }
}
