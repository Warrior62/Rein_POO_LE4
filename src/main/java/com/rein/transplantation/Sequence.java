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
import com.rein.operateur.*;
import com.rein.solution.Chaine;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
public abstract class Sequence implements Comparable {
    //private int id = 0;
=======
public abstract class Sequence {

>>>>>>> selecteurs-merge
    private int benefMedicalSequence = 0;
    private ArrayList<Noeud> listeNoeuds =new ArrayList<>();
    private int tailleMaxSequence;

    public void calculBenefice(List<Echange> listeEchanges){
       if(this.listeNoeuds.size()>1){
            for(int i=0;i<this.listeNoeuds.size()-1;i++){
                Noeud donneur = this.listeNoeuds.get(i);
                Noeud receveur = this.listeNoeuds.get(i+1);
                //calcul du bénéfice (a vers b)
                for(Echange ech: listeEchanges){
                    if(donneur.getId() == ech.getDonneur().getId() && receveur.getId() == ech.getReceveur().getId())
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

    private boolean isPositionValide(int position){
        if(position >= 0 && position <= this.getNbNoeuds()-1)
            return true;
        return false;
    }

    public int getNbNoeuds() {
        return this.listeNoeuds.size();
    }

    public Noeud getNoeud(int position){
        if(this.isPositionValide(position))
            return this.getListeNoeuds().get(position);
        return null;
    }

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
    public String getListeIdNoeuds() {
        String noeuds = "";
        for(Noeud n : listeNoeuds)
            noeuds += n.getId() + " ";
        return noeuds;
    }

    public boolean doInsertion(InsertionNoeud infos){
        System.out.println("doInsertion()");
        if(infos == null) return false;
        if(!infos.isMouvementRealisable()) return false;

        Noeud noeud = infos.getNoeud();

        this.benefMedicalSequence += infos.getDeltaBeneficeMedical();
        this.listeNoeuds.add(infos.getPosition(), noeud);

        //this.ajouterNoeudNonUtilise(noeud, infos.getPosition());

        System.out.println(noeud.getId() + " : " + noeud.getClass() + " ajouté dans " + infos.getSequence());
        return true;
    }

    private boolean isNoeudAjoutable(int position) {
        if ( (this.getListeNoeuds().size() < this.getTailleMaxSequence()) && (position > 0) && (position <= this.getListeNoeuds().size()) ) {
            return true;
        }else {
            return false;
        }
    }

    private boolean isNoeudCompatible(Noeud n, int position) {
        Noeud noeudPrecedent = this.getListeNoeuds().get(position-1);
        if (this.getListeNoeuds().size() == position) { // Si le noeud est ajouté en bout de chaine
            if ( noeudPrecedent.getBenefMedicalVers(n) != -1 ) {
                return true;
            }else {
                return false;
            }
        }else {
            Noeud noeudSuivant = this.getListeNoeuds().get(position);
            if ( (noeudPrecedent.getBenefMedicalVers(n) != -1 ) && (n.getBenefMedicalVers(noeudSuivant) > -1) ) {
                return true;
            }else {
                return false;
            }
        }
    }

    public boolean ajouterNoeudNonUtilise(Noeud n, int position){
        if(this instanceof Chaine){
            //NB : La position 0 est réservée à l'altruiste... On ne peut donc pas y ajouter un noeud
            if ( (position != 0) && this.isNoeudAjoutable(position) && this.isNoeudCompatible(n, position) ) {
                if (this.getListeNoeuds().size() == position) { //Si le noeud est ajouté en bout de chaine, on ne faut qu'ajouter le nouveau benef médical, sans en soustraire
                    //Variables utiles
                    Noeud noeudPrecedent = this.getListeNoeuds().get(position-1);
                    int benefMedical = this.getBenefMedicalSequence();
                    //Traitements
                    benefMedical += noeudPrecedent.getBenefMedicalVers(n);
                    this.setBenefMedicalTotal(benefMedical);
                    this.getListeNoeuds().add(n);
                    //this.listeNoeuds.add(position, n);
                    return true;
                }else {
                    //Variables utiles
                    Noeud noeudPrecedent = this.getListeNoeuds().get(position-1);
                    Noeud noeudSuivant = this.getListeNoeuds().get(position);
                    int benefMedical = this.getBenefMedicalSequence();
                    //Traitements
                    benefMedical -= noeudPrecedent.getBenefMedicalVers(noeudSuivant);
                    benefMedical += noeudPrecedent.getBenefMedicalVers(n);
                    benefMedical += n.getBenefMedicalVers(noeudSuivant);
                    this.setBenefMedicalTotal(benefMedical);
                    this.listeNoeuds.add(position, n);
                    return true;
                }
            }else {
                return false;
            }
        }
        if(this instanceof Cycle){
            Noeud noeudPrecedent;
            Noeud noeudSuivant;
            int benefMedical = this.getBenefMedicalSequence();
            if (this.getListeNoeuds().size() == 0) {
                this.listeNoeuds.add(n);
                // System.out.println("Methode rustine 1");
                return true;
            }
            if (this.getListeNoeuds().size() == 1 && (position == 0 || position == 1) ) {
                int benef1 = n.getBenefMedicalVers(this.getListeNoeuds().get(0));
                int benef2 = this.getListeNoeuds().get(0).getBenefMedicalVers(n);

                this.listeNoeuds.add(position, n);
                this.setBenefMedicalTotal(benef1 + benef2);
                //System.out.println("Methode rustine 2");
                return true;
            }
            if (this.isNoeudAjoutable(position) && this.isNoeudCompatible(n, position)) {
                if (position == 0) { // Ajout en début de cycle
                    noeudPrecedent = this.getListeNoeuds().get(this.getListeNoeuds().size()-1);
                    noeudSuivant = this.getListeNoeuds().get(0);
                    //System.out.println("Noeud ajouté avec succes en debut de cycle");
                }else if (position == this.getListeNoeuds().size()) { //Ajout en fin de cycle
                    noeudPrecedent = this.getListeNoeuds().get(this.getListeNoeuds().size()-1);
                    noeudSuivant = this.getListeNoeuds().get(0);
                }else { // Ajout en milieu de cycle
                    noeudPrecedent = this.getListeNoeuds().get(position-1);
                    noeudSuivant = this.getListeNoeuds().get(position);
                    //System.out.println("Noeud ajouté avec succes en milieu de cycle");
                }
                benefMedical -= noeudPrecedent.getBenefMedicalVers(noeudSuivant);
                benefMedical += noeudPrecedent.getBenefMedicalVers(n);
                benefMedical += n.getBenefMedicalVers(noeudSuivant);
                this.setBenefMedicalTotal(benefMedical);
                this.listeNoeuds.add(position, n);
                return true;
            }else {
                // System.out.println("Peut pas ajouter le noeud");
                return false;
            }
        }
        return true;
    }

    // indique s'il est possible d'insérer un client en position position
    public boolean isPositionInsertionValide(int position) {
        if(position < 0 || position > this.getNbNoeuds())
            return false;
        return true;
    }

    private int deltaCoutInsertionFin(Noeud noeudToAdd){
        return this.deltaCoutInsertion(this.getNbNoeuds(), noeudToAdd);
    }

    public int deltaCoutInsertion(int position, Noeud noeudToAdd) {
        if(!this.isPositionInsertionValide(position) || noeudToAdd == null)
            return Integer.MAX_VALUE;

        int deltaCout = 0;

        if(!this.listeNoeuds.isEmpty()){
            Noeud currentNoeud = this.getCurrent(position);
            Noeud lastNoeud = this.getPrec(position);

            deltaCout -= lastNoeud.getBenefMedicalVers(currentNoeud);
            deltaCout += lastNoeud.getBenefMedicalVers(noeudToAdd);
            deltaCout += noeudToAdd.getBenefMedicalVers(currentNoeud);
        }

        return deltaCout;
    }

    // renvoie le noeud de la séquence qui précéde la position position
    // position doit être comprise entre 0 et n-1
    public Noeud getPrec(int position){
        if(position == 0) return this.getListeNoeuds().get(position);
        return this.getListeNoeuds().get(position-1);
    }

    // renvoie le noeud de la séquence qui est actuellemment  la position position
    public Noeud getCurrent(int position) {
        if(position == this.getNbNoeuds()) return this.getListeNoeuds().get(0);
        return this.getListeNoeuds().get(position);
    }

    public abstract boolean equals(Sequence c);

    @Override
    public int compareTo(Object o) {
        Sequence s = (Sequence) o;
        //System.out.println("Comparaison : " + (s.getBenefMedicalSequence() - this.getBenefMedicalSequence()));
        return s.getBenefMedicalSequence() - this.getBenefMedicalSequence();
    }

    @Override
    public String toString() {
        return "\nSequence {" +
                "benefMedicalTotal=" + benefMedicalSequence +
                ", listeIdNoeuds=[" + this.getListeIdNoeuds() + "]}";
    }

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
