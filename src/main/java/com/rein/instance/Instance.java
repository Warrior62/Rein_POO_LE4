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
    
    private final int paires;
    private final int altruistes;
    private final int cycles;
    private final int chaines;
    
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
        this.paires = paires;
        this.altruistes = altruistes;
        this.cycles = cycles;
        this.chaines = chaines;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nom);
        return hash;
    }

    @Override
    public String toString() {
        String s = "Instance{" + "nom=" + nom + ", paires=" + paires + ", altruistes=" + altruistes + ", cycles=" + cycles + ", chaines=" + chaines + ",\ntabNoeuds= {";
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
        return paires;
    }

    public int getAltruistes() {
        return altruistes;
    }

    public int getCycles() {
        return cycles;
    }

    public int getChaines() {
        return chaines;
    }
}
