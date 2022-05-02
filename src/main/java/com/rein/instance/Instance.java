//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.rein.instance;

import java.util.ArrayList;
import java.util.Objects;

public class Instance {
    private final String nom;
    private final int nbPaires;
    private final int nbAltruistes;
    private final int tailleMaxCycles;
    private final int tailleMaxChaines;
    private Noeud[] tabNoeud;
    private ArrayList<Echange> echanges;

    public Instance(String nom, int paires, int altruistes, int cycles, int chaines, Noeud[] tabNoeud) {
        this.nom = nom;
        this.nbPaires = paires;
        this.nbAltruistes = altruistes;
        this.tailleMaxCycles = cycles;
        this.tailleMaxChaines = chaines;
        this.echanges = new ArrayList();
        this.tabNoeud = tabNoeud;
    }

    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nom);
        return hash;
    }

    public String toString() {
        return "Instance{nom='" + this.nom + "', nbPaires=" + this.nbPaires + ", nbAltruistes=" + this.nbAltruistes + ", tailleMaxCycles=" + this.tailleMaxCycles + ", tailleMaxChaines=" + this.tailleMaxChaines + ", echanges=" + this.echanges + "}";
    }

    public String getNom() {
        return this.nom;
    }

    public int getNbPaires() {
        return this.nbPaires;
    }

    public int getNbAltruistes() {
        return this.nbAltruistes;
    }

    public int getTailleMaxCycles() {
        return this.tailleMaxCycles;
    }

    public int getTailleMaxChaines() {
        return this.tailleMaxChaines;
    }

    public ArrayList<Echange> getEchanges() {
        return this.echanges;
    }

    private void setEchanges(ArrayList<Echange> echanges) {
        this.echanges = echanges;
    }

    public Noeud[] getTabNoeud() {
        return this.tabNoeud;
    }

    private void setTabNoeud(Noeud[] tabNoeud) {
        this.tabNoeud = tabNoeud;
    }

    public Noeud[] addAltruiste(int indice) {
        this.tabNoeud[indice] = new Altruiste(indice + 1);
        return (Noeud[])this.tabNoeud.clone();
    }

    public Noeud[] addPaire(int indice) {
        this.tabNoeud[indice] = new Paire(indice + 1);
        return (Noeud[])this.tabNoeud.clone();
    }
}
