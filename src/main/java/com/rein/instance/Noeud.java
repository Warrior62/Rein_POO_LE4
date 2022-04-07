package com.rein.instance;

import java.util.HashMap;
import java.util.Map;

public class Noeud {
    private int id = -1;
    private Map<Noeud, Echange> listeEchanges;

    public Noeud() {
        this.id++;
        this.listeEchanges = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public Map<Noeud, Echange> getListeEchanges() {
        return listeEchanges;
    }

    @Override
    public String toString() {
        String res = "";
        for(Echange e : listeEchanges.values())
            res += e + " ";
        return "Noeud{" +
                "id=" + id +
                ", listeEchanges=[" + listeEchanges + "]}";
    }

    public static void main(String[] args) {
        Noeud n = new Noeud();

    }
}
