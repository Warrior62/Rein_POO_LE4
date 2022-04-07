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
    private final int tailleMaxCycles;
    private final int tailleMaxChaines;
    
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
        this.tailleMaxCycles = cycles;
        this.tailleMaxChaines = chaines;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nom);
        return hash;
    }

    @Override
    public String toString() {
        String s = "Instance{" + "nom=" + nom + ", nbPaires=" + nbPaires + ", nbAltruistes=" + nbAltruistes + ", tailleMaxCycles=" + tailleMaxCycles + ", tailleMaxChaines=" + tailleMaxChaines + ",\ntabNoeuds= {";
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

    public int getNbPaires() {
        return nbPaires;
    }

    public int getNbAltruistes() {
        return nbAltruistes;
    }

    public int getTailleMaxCycles() {
        return tailleMaxCycles;
    }

    public int getTailleMaxChaines() {
        return tailleMaxChaines;
    }
}
