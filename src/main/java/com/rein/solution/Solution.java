/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rein.solution;
import com.rein.instance.Instance;
import com.rein.io.InstanceReader;
import com.rein.operateur.InsertionNoeud;
import com.rein.transplantation.Cycle;
import com.rein.transplantation.Sequence;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
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

    public boolean ajouterSequence(Sequence s) {
        try {
            this.listeSequences.add(s);
            this.benefMedicalTotal += s.getBenefMedicalSequence();
            return true;
        }catch (Error e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void calculBenefice(){
        System.out.println("calculBenefice()");
        this.suppressionSequencesVides();
        for (Sequence seq : listeSequences){
            //if (seq instanceof Chaine)
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

    public Solution generationSolution(SequencesPossibles sequencesSolution, Instance instance){
        Solution s = new Solution(instance);
        LinkedHashSet<Sequence> tabCycle = sequencesSolution.getCycles();
        LinkedHashSet<Sequence> tabChaine = sequencesSolution.getChaines();
        for (Sequence seq : tabCycle){
            s.ajouterSequence(seq);
        }
        for (Sequence seq : tabChaine){
            s.ajouterSequence(seq);
        }

        return s;

    }

    public int getBenefMedicalTotal() {
        return benefMedicalTotal;
    }
    public Collection<Sequence> getListeSequences() {
        return listeSequences;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setBenefMedicalTotal(int benefMedicalTotal) {
        this.benefMedicalTotal = benefMedicalTotal;
    }

    @Override
    public String toString() {
        String res = "";
        return "Solution{" +
                "benefMedicalTotal=" + benefMedicalTotal +
                ", \nlisteSequences=[" + listeSequences +
                "] }";
    }

    /**
     * Checker de Solution :
     * Condition de validité :
     * - Est reliée à une instance
     * - Le Bénéf médical est correctement calculé
     * - Chaque séquence est valide
     * **/
    public boolean check() {
        return (verifInstanceAssociee() && verifSequencesValides() && verifBenefMedicalCorrect());
    }
    /**
     * Return true si la solution découle d'une instance existante, false sinon.
     * **/
    private boolean verifInstanceAssociee() {
        System.out.println("verifInstance : " + (this.instance.getNom() != "" && this.instance.getNom() != null));
        return (this.instance.getNom() != "" && this.instance.getNom() != null);
    }
    /**
     * Return true si le benef medical total est bien la somme des benefs medicaux de toutes les séquences, false sinon.
     * **/
    private boolean verifBenefMedicalCorrect() {
        int somme = 0;
        for (Sequence s: this.listeSequences)
            somme += s.getBenefMedicalSequence();
        System.out.println("verifBenefMedicalCorrect : " + (somme == this.benefMedicalTotal));
        return (somme == this.benefMedicalTotal);
    }
    /**
     * Return true si l'ENSEMBLE des séquences de la solution sont valides, false sinon.
     * **/
    private boolean verifSequencesValides() {
        boolean ans = false;
        for (Sequence s: this.listeSequences){
            if (s instanceof Chaine){
                ans = ((Chaine) s).check();
            }
            else {
                ans = ((Cycle) s).check();
            }
            if (!ans)
                return false;
        }
        System.out.println("verifSequencesValides : " + ans);
        return true;
    }

    public String exportSol() {
        StringBuilder stringSol = new StringBuilder("// Cout total de la solution\n" +
            this.getBenefMedicalTotal() +
            "\n\n// Description de la solution\n" +
            "// Cycles\n");
            for(Sequence s : this.getListeSequences()) {
                if (s.getClass().toString().equals("class com.rein.transplantation.Cycle")){
                    stringSol.append(s.getListeIdNoeuds() + "\n");
                }
            }
            stringSol.append("\n// Chaines\n");
            for(Sequence s : this.getListeSequences()) {
                if (s.getClass().toString().equals("class com.rein.solution.Chaine")){
                    stringSol.append(s.getListeIdNoeuds() + "\n");
                }
            }
            stringSol.append("\n");
        return stringSol.toString();
    }



    /**
     * Retourne true si la solution comporte au moins une séquence de la classe (Chaine/Cycle) passée en argument
     */
    public boolean hasSequenceOfClass(Class classe){
        for(Sequence s : this.getListeSequences()) {
            if (s.getClass() == classe){
                return true;
            }
        }
        return false;
    }

    public boolean doInsertion(InsertionNoeud infos){
        if(infos == null) return false;
        if(!this.listeSequences.contains(infos.getSequence())) return false;
        if(!infos.doMouvementIfRealisable()) return false;

        this.benefMedicalTotal += infos.getDeltaBeneficeMedical();
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
