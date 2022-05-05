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
import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.io.InstanceReader;
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
    public String getListeIdNoeuds()
    {
        String noeuds = "";
        for(Noeud n : listeNoeuds)
            noeuds += n.getId() + " ";
        return noeuds;
    }
    @Override
    public String toString() {
        return "\nSequence {" +
                "benefMedicalTotal=" + benefMedicalSequence +
                ", listeIdNoeuds=[" + this.getListeIdNoeuds() + "]}";
    }
    //--
    public static void main(String[] args) {
        InstanceReader reader;
        try {
            reader = new InstanceReader("instancesInitiales/KEP_p9_n1_k3_l3.txt");
            Instance i = reader.readInstance();
            //System.out.println(i);
            Noeud[] tab = i.getTabNoeud();
            Altruiste a1 = (Altruiste) tab[0]; //id=1 - compatibilités vers {Paire{id=9}=10, Paire{id=5}=5, Paire{id=2}=4}
            Noeud p2 = tab[1]; //id=2 - compatibilités vers {Paire{id=9}=4, Paire{id=3}=7, Paire{id=5}=10, Paire{id=8}=9, Paire{id=4}=4}
            Noeud p3 = tab[2]; //id=3 - compatibilités vers {Paire{id=9}=4, Paire{id=6}=1, Paire{id=8}=10, Paire{id=2}=2, Paire{id=4}=6, Paire{id=10}=2}
            Noeud p4 = tab[3]; //id=4 - compatibilités vers {Paire{id=9}=3, Paire{id=3}=6, Paire{id=6}=6, Paire{id=5}=8}
            Noeud p5 = tab[4]; //id=5 - compatibilités vers {Paire{id=6}=6, Paire{id=7}=8}
            Noeud p6 = tab[5]; //id=6 - compatibilités vers {Paire{id=3}=2, Paire{id=8}=9, Paire{id=7}=8, Paire{id=4}=9}
            Noeud p7 = tab[6]; //id=7 - compatibilités vers {Paire{id=3}=1, Paire{id=6}=8, Paire{id=8}=8, Paire{id=2}=10}
            Noeud p8 = tab[7]; //id=8 - compatibilités vers {Paire{id=9}=9, Paire{id=6}=6, Paire{id=5}=8, Paire{id=7}=7, Paire{id=10}=4}
            Noeud p9 = tab[8]; //id=9 - compatibilités vers {Paire{id=4}=8}
            // ### Test ajout (noeuds compatibles et incompatibles, ordres corrects et incorrects) ###
            Cycle c1 = new Cycle(5); // cycle vide [benef 0]
            c1.ajouterNoeud(p6, 0);
            c1.ajouterNoeud(p3, 0); // cycle p6-p3 benef 3
            c1.ajouterNoeud(p4, 2); // cycle p3-p6-p4 benef 16
            c1.ajouterNoeud(p8, 1); // cycle p3-p8-p6-p4 benef 16
            c1.ajouterNoeud(p8, 6); // cycle p3-p8-p6-p4 benef 16
            System.out.println(c1);
            // ===> TEST OK
            // ### Test ajout d'un altruiste ###
            System.out.println("Checker : "+c1.check());
        } catch(Exception e){
            System.out.println("ERROR test ajout");
            System.out.println(e.toString());
        }
    }
}
