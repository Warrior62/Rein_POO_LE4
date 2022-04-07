package com.rein.instance;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author MartinFrmx
 * 1 avr. 2022
 * Project : rein-poo-le4
 * InstanceReader in io
 * Description : Instance reader class
 */


public class Instance {
    
    // le nom doit être unique
    private final String nom;
    
    private final int nbPaires;
    private final int nbAltruistes;
    private final int nbCycles;
    private final int nbChaines;

    private int tailleMaxCycle, tailleMaxChaine;
    
    private int[][] TabNoeuds;

    /**
     * Constructeur d'Instances.
     * @param nom nom du fichier d'instance
     * @param paires nombre de paires patient-donneur P
     * @param altruistes  nombre de donneurs altruistes N
     * @param cycles  taille maximale des cycles K
     * @param chaines  taille maximale des chaines L
     */
    public Instance(String nom, int paires, int altruistes, int cycles, int chaines) {
        this.nom = nom;
        this.nbPaires = paires;
        this.nbAltruistes = altruistes;
        this.nbCycles = cycles;
        this.nbChaines = chaines;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nom);
        return hash;
    }

    @Override
    public String toString() {
        String s = "Instance{" + "nom=" + nom + ", paires=" + nbPaires + ", altruistes=" + nbAltruistes + ", cycles=" + nbCycles + ", chaines=" + nbChaines + ",\ntabNoeuds= {";
        for(int[] l : this.TabNoeuds)
        {
            s += Arrays.toString(l) + "\n";
        }
        s += "}}";
        return s;
    }

    public int[][] getTabNoeuds() {
        return TabNoeuds;
    }

    public void setTabNoeuds(int[][] TabNoeuds) {
        this.TabNoeuds = TabNoeuds;
    }

    public String getNom() {
        return nom;
    }

    public int getPaires() {
        return nbPaires;
    }

    public int getAltruistes() {
        return nbAltruistes;
    }

    public int getCycles() {
        return nbCycles;
    }

    public int getChaines() {
        return nbChaines;
    }
}
