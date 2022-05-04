/**
 *
 * @author Mathis
 * Project : rein-poo-le4
 * Situation in transplantation
 * Description : Séquence class
 */

package com.rein.transplantation;

import com.rein.instance.Echange;
import com.rein.instance.Instance;
import com.rein.instance.Noeud;

import java.util.ArrayList;
import java.util.List;

public abstract class Sequence {

    //private int id = 0;
    private int benefMedicalSequence = 0;
    private ArrayList<Noeud> listeNoeuds =new ArrayList<>();
    private int tailleMaxSequence;

    public void increaseBenefMedicalSequence(int nb){
        this.benefMedicalSequence += nb;
    }

    public void decreaseBenefMedicalTotal(int nb){
        this.benefMedicalSequence -= nb;
    }

    public void calculBenefice(List<Echange> listeEchanges){

        System.out.println("calcul benef sequence");

        if(this.listeNoeuds.size()>1){
            for(int i=0;i<this.listeNoeuds.size()-1;i++){
                Noeud donneur = this.listeNoeuds.get(i);
                Noeud receveur = this.listeNoeuds.get(i+1);
                //calcul du bénéfice (a vers b)
                for(Echange ech: listeEchanges){
                    if(donneur.getId() ==ech.getDonneur().getId() && receveur.getId()== ech.getReceveur().getId())
                        this.benefMedicalSequence += ech.getBenefMedical();
                }
                if(this instanceof Cycle){
                    //si c'est un cycle, on calcule le bénéfice retour (b vers a)
                    for(Echange ech2: listeEchanges){
                        if(receveur.getId() ==ech2.getDonneur().getId() && donneur.getId()== ech2.getReceveur().getId())
                            this.benefMedicalSequence += ech2.getBenefMedical();
                    }
                }
            }
        }
    }
    /*public int getId() {
        return id;
    }*/

    public int getBenefMedicalSequence() {
        return benefMedicalSequence;
    }

    public void setTailleMaxSequence(int tailleMaxSequence) {
        this.tailleMaxSequence = tailleMaxSequence;
    }

    public int getTailleMaxSequence() {
        return tailleMaxSequence;
    }

    public void setBenefMedicalTotal(int benefMedicalTotal) {
        this.benefMedicalSequence = benefMedicalTotal;
    }

    public ArrayList<Noeud> getListeNoeuds() {
        return listeNoeuds;
    }

    @Override
    public String toString() {
        String noeuds = "";
        for(Noeud n : listeNoeuds)
            noeuds += n.getId() + " ";
        return "\nSequence {" +
                "benefMedicalTotal=" + benefMedicalSequence +
                ", listeIdNoeuds=[" + noeuds + "]}";
    }

    //--

}
