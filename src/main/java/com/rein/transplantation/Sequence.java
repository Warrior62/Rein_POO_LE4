/**
 *
 * @author Mathis
 * Project : rein-poo-le4
 * Situation in transplantation
 * Description : Séquence class
 */

package com.rein.transplantation;

import com.rein.instance.Altruiste;
import com.rein.instance.Echange;
import com.rein.instance.Noeud;
import com.rein.instance.Paire;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Sequence {

    private int id;
    private int benefMedicalSequence = 0;
    private ArrayList<Echange> listeEchanges;
    private int tailleMaxSequence;

    public void increaseBenefMedicalTotal(int nb){
        this.benefMedicalSequence += nb;
    }

    public void decreaseBenefMedicalTotal(int nb){
        this.benefMedicalSequence -= nb;
    }

    public int getId() {
        return id;
    }

    public int getBenefMedicalTotal() {
        return benefMedicalSequence;
    }

    public void setBenefMedicalTotal(int benefMedicalTotal) {
        this.benefMedicalSequence = benefMedicalTotal;
    }

    public ArrayList<Echange> getListeEchanges() {
        return listeEchanges;
    }

    @Override
    public String toString() {
        String str = "Sequence{" +
                "id=" + id +
                ", benefMedicalSequence=" + benefMedicalSequence +
                ", tailleMaxSequence=" + tailleMaxSequence +
                ", listeEchanges=";
        for (Echange e: this.listeEchanges)
            str += e.toString() + "\n";

        str += '}';

        return str;
    }

    /*private int id;
    private int benefMedicalSequence = 0;
    private ArrayList<Noeud> listeNoeuds;
    private int tailleMaxSequence;*/
    /**
     * Checker de la classe Séquence.
     * Conditions de validité :
     * - La taille de listeEchanges < tailleMaxSequence
     * - Le benefice medical de la séquence est correctement calculé
     * - La structure de la chaine (ou cycle) est valide
     *      * Chaine :
     *          Une chaine commence bien par un altruiste
     *          Seul le dernier ne donne pas de rein
     *          Ne contient qu'un altruiste
     *          l'altruiste ne reçoit pas de rein
     *          chaque échange n'a lieu qu'1 seule fois
     *      * Cycle :
     *          Un cycle ne commence pas par un altruiste
     *          Tout le monde donne et reçoit 1 seule fois
     *          1 cycle ne contient pas d'altruiste.
     * **/
    public boolean check() {
        return (isTailleListeEchangesCorrecte() && isSequenceCorrecte() && isBenefMedicalCorrect());
    }

    private boolean isTailleListeEchangesCorrecte() {
        return (this.listeEchanges.size() < this.tailleMaxSequence);
    }

    private boolean isBenefMedicalCorrect() {
        int somme = 0;
        for (Echange e: this.listeEchanges){
            somme += e.getBenefMedical();
        }
        return (somme == this.benefMedicalSequence);
    }

    private boolean isSequenceCorrecte() {
        // Si la séquence est une chaine
        /*if (this.listeEchanges.get(0).getDonneur() instanceof Altruiste) {
            //seul le dernier ne donne pas de rein
            if (this.listeEchanges.get(this.listeEchanges.size()-1).getReceveur() != null) return false; //Le dernier donne un rein
            for (Echange e: this.listeEchanges) {
                if (e.getReceveur() == null) return false;
            }

            for (Echange e: this.listeEchanges) {

            }
            //l'altruiste ne reçoit pas de rein
            //chaque transplantation n'a lieu qu'1 seule fois
        }*/

        //Si la séquence est un cycle
        /*if (this.listeEchanges.get(0).getDonneur() instanceof Paire) {
            //Tout le monde donne et reçoit 1 seule fois
            //1 cycle ne contient pas d'altruiste.
        }*/
        return true;
    }

}
