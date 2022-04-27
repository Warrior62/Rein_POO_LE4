/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rein.solution;

import com.rein.instance.Instance;
import com.rein.io.InstanceReader;
import com.rein.transplantation.Sequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tryla
 */
public class Solution {

    private int benefMedicalTotal;
    private Collection<Sequence> listeSequences;
    private Instance instance;

    public Solution(Instance instance) {
        this.instance = instance;
        this.listeSequences = new ArrayList<>();
    }

    public Solution(Solution s) {
        this(s.instance);
        this.benefMedicalTotal = s.benefMedicalTotal;
        for(int i=0; i<s.listeSequences.size(); i++)
            this.listeSequences.add((Sequence) s.listeSequences.toArray()[i]);
    }

    public int getBenefMedicalTotal() {
        return benefMedicalTotal;
    }

    public Collection<Sequence> getListeSequences() {
        return listeSequences;
    }

    @Override
    public String toString() {
        String res = "";
        for(Sequence s : listeSequences)
            res += s.getId() + " ";
        return "Solution{" +
                "benefMedicalTotal=" + benefMedicalTotal +
                ", listeSequences=[" + res +
                "], " + instance + '}';
    }


    /**
     * Checker de Solution :
     * Condition de validité :
     * - Est reliée à une instance
     * - Le Bénéf médical est correctement calculé
     * - Chaque séquence est valide
     * **/

    public boolean check() {

        return (isInstanceAssociee() && areSequencesValides() && isBenefMedicalCorrect());
    }

    /**
     * Return true si la solution découle d'une instance existante, false sinon.
     * **/
    private boolean isInstanceAssociee() {
        return (this.instance.getNom() != "" && this.instance.getNom() != null);
    }

    /**
     * Return true si le benef medical total est bien la somme des benefs medicaux de toutes les séquences, false sinon.
     * **/
    private boolean isBenefMedicalCorrect() {
        int somme = 0;
        for (Sequence s: this.listeSequences)
            somme += s.getBenefMedicalTotal();
        return (somme == this.benefMedicalTotal);
    }

    /**
     * Return true si l'ENSEMBLE des séquences de la solution sont valides, false sinon.
     * **/
    private boolean areSequencesValides() {
        for (Sequence s: this.listeSequences)
            if (!s.check())
                return false;
        return true;
    }

    public static void main(String[] args) {
        InstanceReader reader;
        try {
            reader = new InstanceReader("instancesInitiales/KEP_p9_n0_k3_l0.txt");
            Instance i = reader.readInstance();
            Solution sZeroEchange = new Solution(i);
            System.out.println("Solution à 0 échange: \n\t" + sZeroEchange);
        } catch (Exception ex) {
            Logger.getLogger(Instance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
