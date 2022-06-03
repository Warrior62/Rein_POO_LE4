package com.rein.instance;

import com.rein.io.InstanceReader;
import com.rein.io.exception.ReaderException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

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
    private final int nbPaires;
    private final int nbAltruistes;
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

    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nom);
        return hash;
    }

    @Override
    public String toString() {
        return "Instance{nom='" + this.nom + "', nbPaires=" + this.nbPaires + ", nbAltruistes=" + this.nbAltruistes + ", tailleMaxCycles=" + this.tailleMaxCycles + ", tailleMaxChaines=" + this.tailleMaxChaines + ", echanges=" + this.echanges + "}";
    }

    public ArrayList<Altruiste> getTabAltruistes() {
        return tabAltruistes;
    }

    public ArrayList<Paire> getTabPaire() {
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

        private void setEchanges(ArrayList<Echange> echanges) {
            this.echanges = echanges;
        }

        public Noeud[] getTabNoeud() {
            return this.tabNoeud;
        }

        private void setTabNoeud(Noeud[] tabNoeud) {
            this.tabNoeud = tabNoeud;
        }

        //Methode chargée de renvoyer une copie du noeud d'id 'id'
        //Prend en paramètre l'id du noeud à récupérer.
        //Renvoie une copie du noeud correspondant à l'id 'id'.
        /*public Noeud getCopieNoeud(int id) {
            Noeud n = new Noeud(this.getTabNoeud()[id-1]);
            return n;
        }*/

        public Noeud[] addAltruiste(int indice) {
            this.tabNoeud[indice] = new Altruiste(indice + 1);
            this.tabAltruistes.add((Altruiste) this.tabNoeud[indice]);
            return (Noeud[])this.tabNoeud.clone();
        }

        public Noeud[] addPaire(int indice) {
            this.tabNoeud[indice] = new Paire(indice + 1);
            this.tabPaires.add((Paire) this.tabNoeud[indice]);
            return (Noeud[])this.tabNoeud.clone();
        }
    }


