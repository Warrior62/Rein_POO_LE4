/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rein.solution;
import com.rein.instance.Instance;
import com.rein.io.InstanceReader;
import com.rein.operateur.OperateurInterSequence;
import com.rein.operateur.OperateurIntraSequence;
import com.rein.operateur.OperateurLocal;
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
        System.out.println("calcul de bénéfice total");
        this.suppressionSequencesVides();
        for (Sequence seq : listeSequences){
            if (seq instanceof Chaine)
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
        return (this.instance.getNom() != "" && this.instance.getNom() != null);
    }
    /**
     * Return true si le benef medical total est bien la somme des benefs medicaux de toutes les séquences, false sinon.
     * **/
    private boolean verifBenefMedicalCorrect() {
        int somme = 0;
        for (Sequence s: this.listeSequences)
            somme += s.getBenefMedicalSequence();
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
            else
            {ans = ((Cycle) s).check();
            }
            if (!ans)
                return false;
        }
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

    public boolean doMouvementRechercheLocale(OperateurLocal infos){
        if(infos == null) return false;
        if(!infos.doMouvementIfRealisable()) return false;

        this.benefMedicalTotal += infos.getDeltaBeneficeMedical();
        if(!this.check()){
            System.out.println("ERROR doMouvementRechercheLocale");
            System.out.println(infos);
            System.exit(-1);
        }
        return true;
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


   /* public Solution generationSolution(Instance i){
        Solution sol = new Solution(i);

        Arbre arbre = new Arbre(instance.getTabNoeud()[0], instance);
        SequencesPossibles sequencesUtilisables = arbre.detectionChainesCycles();

        Selecteur selecteur = new Selecteur(sequencesUtilisables);
        SequencesPossibles seqSol = selecteur.selectionRandom_v1();
        SequencesPossibles seqSol2 = selecteur.selectionPlusGrosBenef();


        //COMPARER BENEF  DES SEQUENCES ET PRENDRE LA MEILLEURE


        LinkedHashSet<Sequence> tabCycle = seqSol.getCycles();
        LinkedHashSet<Sequence> tabChaine = seqSol.getChaines();
        for (Sequence seq : tabCycle){
            sol.ajouterSequence(seq);
        }
        for (Sequence seq : tabChaine){
            sol.ajouterSequence(seq);
        }

        return sol;
    }*/

    /*private OperateurLocal getMeilleurOperateurIntra(TypeOperateurLocal type){
        return null;
        OperateurLocal best = OperateurLocal.getOperateur(type);
        for(Tournee t : this.listeTournees){
            for(int i=0; i<t.getClients().size(); i++) {
                for(int j=0; j<t.getClients().size()+1; j++) {
                    if(j < t.getClients().size()){
                        OperateurIntraTournee op = OperateurLocal.getOperateurIntra(type, t, i, j);
                        if(op.isMeilleur(best)) {
                            best = op;
                        }
                    }
                }
            }
        }
        return best;
    }*/

    /*private OperateurLocal getMeilleurOperateurInter(TypeOperateurLocal type){
        OperateurLocal best = OperateurLocal.getOperateur(type), op;
        for(Sequence s : this.listeSequences){
            for(Sequence s1 : this.listeSequences) {
                op = s.getMeilleurOperateurInter(type, s1);
                if(op.isMeilleur(best))
                    best = op;
            }
        }
        return best;
    }*/

   /* public OperateurLocal getMeilleurOperateurLocal(TypeOperateurLocal type){
        if(OperateurLocal.getOperateur(type) instanceof OperateurIntraSequence)
            return this.getMeilleurOperateurIntra(type);
        else if(OperateurLocal.getOperateur(type) instanceof OperateurInterSequence)
            return this.getMeilleurOperateurInter(type);
        //this.getMeilleurOperateurInter(type);
        return null;
    }*/

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
