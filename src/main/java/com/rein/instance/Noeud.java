package com.rein.instance;

import java.util.HashMap;
import java.util.Map;

public class Noeud {
    private int id = -1;
    private Map<Noeud, Echange> listeEchanges;

    public Noeud(int id) {
        this.id = id;
        this.listeEchanges = null;
    }

    public int getId() {
        return id;
    }

    public Map<Noeud, Echange> getListeEchanges() {
        return listeEchanges;
    }

    @Override
    public String toString() {
        String s = "Noeud{" +
                "id=" + id;
        if (listeEchanges!=null) {
            s += ", listeEchanges=[" + listeEchanges + "]";
        }
            s += "}";
        return s;
    }

    public static void main(String[] args) {
        Noeud n = new Noeud(1);

    }
}
