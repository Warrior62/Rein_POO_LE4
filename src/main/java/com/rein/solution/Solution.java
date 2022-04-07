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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tryla
 */
public class Solution {

    private int benefMedicalTotal;
    private ArrayList<Sequence> listeSequences;
    private Instance instance;

    public Solution(Instance instance) {
        this.instance = instance;
        this.listeSequences = new ArrayList<>();
    }

    public Solution(Solution s) {
        this.instance = s.instance;
        this.benefMedicalTotal = s.benefMedicalTotal;
        ArrayList<Sequence> list = new ArrayList<>();
        for(int i=0; i<s.listeSequences.size(); i++)
            list.add(i, s.listeSequences.get(i));
        this.listeSequences = list;
    }

    public int getBenefMedicalTotal() {
        return benefMedicalTotal;
    }

    public ArrayList<Sequence> getListeSequences() {
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

    public static void main(String[] args) {
        InstanceReader reader;
        try {
            reader = new InstanceReader("instancesInitiales/KEP_p9_n0_k3_l0.txt");
            Instance i = reader.readInstance();
            Solution sZeroEchange = new Solution(i);
            System.out.println("Solution à 0 échange: \n\t" + sZeroEchange.toString());
        } catch (Exception ex) {
            Logger.getLogger(Instance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
