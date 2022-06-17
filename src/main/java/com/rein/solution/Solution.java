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

    /**
     * Constructeur de Solution par valeur.
     * @param instance Instance pour laquelle la solution est créée.
     * */
    public Solution(Instance instance) {
        this.instance = instance;
        this.listeSequences = new ArrayList<>();
        this.benefMedicalTotal = 0;
    }

    /**
     * Constructeur par copie de Solution.
     * @param s Solution à copier.
     * */
    public Solution(Solution s) {
        this(s.instance);
        this.benefMedicalTotal = s.benefMedicalTotal;
        for(int i=0; i<s.listeSequences.size(); i++)
            this.listeSequences.add((Sequence) s.listeSequences.toArray()[i]);
    }

    /**
     * Méthode permettant d'ajouter le contenu d'un objet SequencesPossibles dans la solution courante.
     * Les chaines et cycles contenus sont ajoutés à la solution, et la somme des benefs médicaux totaux est affectée à la solution.
     * @param s SequencesPossibles à ajouter à la solution, objet retourné par la méthode de détection des séquences.
     * */
    public void ajouterSequencesSelectionnees(SequencesPossibles s) {
        this.listeSequences.addAll(s.getChaines());
        this.listeSequences.addAll(s.getCycles());

        this.benefMedicalTotal += s.getBenefTotal();
    }


    public Solution() {
        this.listeSequences = null;
        this.benefMedicalTotal = 0;
    }

    /**
     * Méthode d'ajout d'une séquence à la solution courante.
     * Met à jour la liste des séquences, et le benef médical total de la solution
     * @param s Sequence à ajouter à la solution.
     * */
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

    /**
     * Méthode de calcul du bénéfice
     * Vide les séquences vides et calcule les bénéfices de chaque séquence.
     * Fait appel à la méthode suppressionSequencesVides().
     * */
    public void calculBenefice(){
        this.suppressionSequencesVides();
        for (Sequence seq : listeSequences){
            if (seq instanceof Chaine)
            seq.calculBenefice(instance.getEchanges());
                this.benefMedicalTotal += seq.getBenefMedicalSequence();
        }
    }

    /**
     * Méthode de suppression des chaines vides. Pour chaque sequence de la solution courante,
     * retire la séquence si cette dernière est vide.
     * */
    public void suppressionSequencesVides(){
        Collection<Sequence> listeSequencesCopy = new ArrayList<>(listeSequences);
        for (Sequence seq: listeSequencesCopy){
            //si la séquence comporte 0 ou 1 noeud on la supprime de la solution finale
            if (seq.getListeNoeuds().size()<=1) {
                this.listeSequences.remove(seq);
            }
        }
    }

    /**
     * Méthode de récupération du bénéfice médical de la solution courante.
     * @return le bénéfice médical de la solution courante.
     * */
    public int getBenefMedicalTotal() {
        return benefMedicalTotal;
    }

    /**
     * Méthode de récupération de la liste de séquences de la solution courante.
     * @return la liste de séquences de la solution courante.
     * */
    public Collection<Sequence> getListeSequences() {
        return listeSequences;
    }

    /**
     * Méthode de récupération de l'instance associée à la solution courante.
     * @return l'instance associée à la solution courante.
     * */
    public Instance getInstance() {
        return instance;
    }

    /**
     * Méthode toString de la classe Solution.
     * @return la chaine descriptive de la solution courante.
     * */
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
     * Fait appel aux méthodes filles verifInstanceAssociee(), verifSequencesValides() et verifBenefMedicalCorrect().
     * @return un boolean true si la solution est valide, false sinon.
     * **/
    public boolean check() {
        return (verifInstanceAssociee() && verifSequencesValides() && verifBenefMedicalCorrect());
    }

    /**
     * Méthode de vérification de l'instance Associée à la solution courante.
     * @return un boolean true si la solution découle d'une instance existante, false sinon.
     * **/
    private boolean verifInstanceAssociee() {
        return (this.instance.getNom() != "" && this.instance.getNom() != null);
    }

    /**
     * Méthode de vérification du benef médical de la solution courante.
     * @return un boolean true si le benef medical total est bien la somme des benefs medicaux de toutes les séquences, false sinon.
     * **/
    private boolean verifBenefMedicalCorrect() {
        int somme = 0;
        for (Sequence s: this.listeSequences)
            somme += s.getBenefMedicalSequence();
        return (somme == this.benefMedicalTotal);
    }

    /**
     * Méthode de vérification de la validité des séquences de la solution courante.
     * @return un boolean true si l'ENSEMBLE des séquences de la solution sont valides, false sinon.
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

    /**
     * Méthode d'export de la solution courante.
     * @return une chaine contenant l'export de la solution courante.
     * */
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
     * Méthode fille de la méthode de recherche locale.
     * Permet d'effectuer un mouvement au sein de séquences manipulées pour générer une solution.
     * Fait appel à la méthode fille doMouvementIfRealisable().
     * @param infos operateur local associé à la transaction.
     * @return un boolean true so l'opération a bien été effectuée, false si elle est impossible.
     * */
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
     *
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
