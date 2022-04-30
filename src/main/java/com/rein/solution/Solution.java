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
import java.util.Collections;
import java.util.List;
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

    public void calculBenefice(){
        System.out.println("calcul de bénéfice total");
        this.suppressionSequencesVides();
        for (Sequence seq : listeSequences){
            seq.calculBenefice(instance.getEchanges());
                this.benefMedicalTotal += seq.getBenefMedicalSequence();
        }
    }

    public void suppressionSequencesVides(){
        Collection<Sequence> listeSequencesCopy = new ArrayList<>(listeSequences);
        for (Sequence seq: listeSequencesCopy){
            //si la séquence comporte 0 ou 1 noeud on la supprime de la solution finale
            if (seq.getListeNoeuds().size()<=1) {
                this.listeSequences.remove(seq);
            }
        }
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

        return "Solution{" +
                "benefMedicalTotal=" + benefMedicalTotal +
                ", \nlisteSequences=[" + listeSequences +
                "] }";
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
