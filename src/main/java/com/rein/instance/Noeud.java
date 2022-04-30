package com.rein.instance;

import java.util.HashMap;
import java.util.Map;

public class Noeud {
    private final int id;
    private Map<Noeud, Integer> listeEchanges;

    public Noeud(int id) {
        this.id = id;
        this.listeEchanges = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public Map<Noeud, Integer> getListeEchanges() { return listeEchanges; }

    private void setListeEchanges(Map<Noeud, Integer> listeEchanges) {
        this.listeEchanges = listeEchanges;
    }

    //FONCTION POUR CLONER UN TABLEAU MAP
    public Map<Noeud, Integer> clone(Map<Noeud, Integer> original)
    {
        Map<Noeud, Integer> copy = new HashMap<>();
        copy.putAll(original);
        return copy;
    }


    //FONCTION POUR RECUPERER LE N MEILLEUR BENEFICE
    public Noeud MeilleurNBenefice(int numero){
        Map<Noeud, Integer> listeEchangesTest = clone(listeEchanges);
        Noeud noeud = null;

        if(numero <= listeEchanges.size()) {
            for (int j = 0; j < numero; j++) {
                Map.Entry<Noeud, Integer> entry = listeEchangesTest.entrySet().stream()
                        .max((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue())).get();

                //System.out.println("L'entree avec la valeur la plus elevee est (key=" + entry.getKey() + ", value=" + entry.getValue() + ")");
                listeEchangesTest.remove(entry.getKey());
                noeud = entry.getKey();
            }
        }
        return noeud;

    }

    //FONCTION POUR RECUPERER LE MEILLEUR BENEFICE
    public Noeud MeilleurBenefice(){
        Map.Entry<Noeud, Integer> entry = listeEchanges.entrySet().stream()
                .max((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue())).get();
        //System.out.println("L'entree ayant la valeur la plus elevee est (key=" + entry.getKey() + ", value=" + entry.getValue() + ")");
        return entry.getKey();
    }

    public boolean isPossible(Noeud n){
        boolean possible = this.listeEchanges.containsKey(n);
        return possible;
    }
    @Override
    public String toString() {
        String s = "Noeud{" +
                "id=" + id;
        /*if (listeEchanges.size()>0) {
            s += ", listeEchanges=[" + listeEchanges + "]";
        }*/
            s += "}";
        return s;
    }
}
