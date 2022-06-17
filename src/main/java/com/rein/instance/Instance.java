package com.rein.instance;

import com.rein.io.InstanceReader;
import com.rein.io.exception.ReaderException;

import java.util.*;

/**
 *
 * @author MartinFrmx
 * 1 avr. 2022
 * Project : rein-poo-le4
 * InstanceReader in io
 * Description : Instance reader class
 */


public class Instance {
    private final String nom;
    private int nbPaires;
    private int nbAltruistes;
    private final int tailleMaxCycles;
    private final int tailleMaxChaines;
    private Noeud[] tabNoeud;
    private ArrayList<Altruiste> tabAltruistes;
    private ArrayList<Paire> tabPaires;
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
            this.echanges = new ArrayList();
            this.tabNoeud = tabNoeud;
            this.tabAltruistes = new ArrayList<Altruiste>();
            this.tabPaires = new ArrayList<Paire>();
    }

    public Instance(Instance i) {
        this.nom = i.getNom();
        this.nbPaires = i.getNbPaires();
        this.nbAltruistes  =i.getNbAltruistes();
        this.tailleMaxCycles = i.getTailleMaxCycles();
        this.tailleMaxChaines = i.getTailleMaxChaines();
        this.echanges = i.getEchanges();
        this.tabNoeud = i.getTabNoeud();
        this.tabAltruistes = i.getTabAltruistes();
        this.tabPaires = i.getTabPaires();
    }

    /*public Instance(Instance i, ArrayList<Altruiste> altruistesUtilises, ArrayList<Paire> pairesUtilisees) {
        this.nom = i.getNom();
        this.nbPaires = i.getNbPaires() - pairesUtilisees.size();
        this.nbAltruistes = i.getNbAltruistes() - altruistesUtilises.size();
        this.tailleMaxCycles = i.tailleMaxCycles;
        this.tailleMaxChaines = i.tailleMaxChaines;

        ArrayList<Paire>
        this.echanges = i.getEchanges();
        this.tabNoeud = (Noeud[]) noeudsDispo.toArray();
        this.tabAltruistes = new ArrayList<Altruiste>(altruistesDispo);
        //this.tabAltruistes.addAll((Collection<? extends Altruiste>) altruistesDispo);
        this.tabPaires = new ArrayList<Paire>(pairesDispo);
        //this.tabPaires.addAll((Collection<? extends Paire>) pairesDispo);
    }*/

    

    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nom);
        return hash;
    }

    @Override
    public String toString() {
        return "Instance{nom='" + this.nom + "', nbPaires=" + this.nbPaires + ", nbAltruistes=" + this.nbAltruistes + ", tailleMaxCycles=" + this.tailleMaxCycles + ", tailleMaxChaines=" + this.tailleMaxChaines + ", echanges=" + this.echanges + "}";
    }

    public void setNbPaires(int nbPaires) {
        this.nbPaires = nbPaires;
    }


    public void setNbAltruistes(int nbAltruistes) {
        this.nbAltruistes = nbAltruistes;
    }

    public ArrayList<Altruiste> getTabAltruistes() {
        return tabAltruistes;
    }

    public ArrayList<Paire> getTabPaires() {
        return tabPaires;
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



    public Noeud[] getTabNoeud() {
        return this.tabNoeud;
    }

    /**
     * Ajouter l'altruiste à la position indice
     * dans tabAltruistes
     * @param indice de l'altruiste à ajouter
     * @return le tableau de noeuds mis à jour
     */
    public Noeud[] addAltruiste(int indice) {
        this.tabNoeud[indice] = new Altruiste(indice + 1);
        this.tabAltruistes.add((Altruiste) this.tabNoeud[indice]);
        return (Noeud[])this.tabNoeud.clone();
    }

    /**
     * Ajouter la paire à la position indice
     * dans tabPaires
     * @param indice de la paire à ajouter
     * @return le tableau de noeuds mis à jour
     */
    public Noeud[] addPaire(int indice) {
        this.tabNoeud[indice] = new Paire(indice + 1);
        this.tabPaires.add((Paire) this.tabNoeud[indice]);
        return (Noeud[])this.tabNoeud.clone();
    }
}


