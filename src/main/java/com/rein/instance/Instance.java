package com.rein.instance;

import com.rein.io.InstanceReader;
import com.rein.io.exception.ReaderException;

import java.util.ArrayList;
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

    private Noeud  tabNoeud[];
    private ArrayList<Echange> echanges;

        /**
     * Constructeur d'Instances.
     * @param nom nom du fichier d'instance
     * @param paires nombre de paires patient-donneur P
     * @param altruistes  nombre de donneurs altruistes N
     * @param cycles  taille maximale des cycles K
     * @param chaines  taille maximale des chaines L
     */
    public Instance(String nom, int paires, int altruistes, int cycles, int chaines, Noeud tabNoeud[]) {
        this.nom = nom;
        this.nbPaires = paires;
        this.nbAltruistes = altruistes;
        this.tailleMaxCycles = cycles;
        this.tailleMaxChaines = chaines;
        this.echanges = new ArrayList<>();
        this.tabNoeud = tabNoeud;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nom);
        return hash;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "nom='" + nom + '\'' +
                ", nbPaires=" + nbPaires +
                ", nbAltruistes=" + nbAltruistes +
                ", tailleMaxCycles=" + tailleMaxCycles +
                ", tailleMaxChaines=" + tailleMaxChaines +
                ", echanges=" + echanges +
                '}';
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

    public ArrayList<Echange> getEchanges() {
        return echanges;
    }

    private void setEchanges(ArrayList<Echange> echanges) {
        this.echanges = echanges;
    }

    public Noeud[] getTabNoeud() {
        return tabNoeud;
    }

    private void setTabNoeud(Noeud[] tabNoeud) {
        this.tabNoeud = tabNoeud;
    }

    public Noeud[] addAltruiste(int indice) {
        this.tabNoeud[indice] = new Altruiste(indice+1);
        return this.tabNoeud.clone();
    }

    public Noeud[] addPaire(int indice) {
        this.tabNoeud[indice] = new Paire(indice+1);
        return this.tabNoeud.clone();
    }
}
