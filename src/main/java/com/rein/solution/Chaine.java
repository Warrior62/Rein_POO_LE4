/**
 *
 * @author Mathis
 * Project : rein-poo-le4
 * Chaine in transplantation
 * Description : Chaine class
 */

package com.rein.solution;

import com.rein.instance.*;
import com.rein.io.InstanceReader;
import com.rein.solveur.StrategieBasique;
import com.rein.transplantation.Sequence;

import java.util.List;

public class Chaine extends Sequence {

    public Chaine(int tailleMax,Altruiste a){
        super();
        this.setTailleMaxSequence(tailleMax);
        super.getListeNoeuds().add(a);
    }

    public Altruiste getAltruiste() {
        return (Altruiste) this.getListeNoeuds().get(0);
    }

    //Peut être supprimée à priori
    public void setAltruiste(Altruiste altruiste) {
        this.getListeNoeuds().add(0, altruiste);
    }

    @Override
    public String toString() {
        return "Chaine : { \n"
                + "\taltruiste : " + this.getAltruiste().toString() + "\n"
                + "\tlisteNoeuds : "+this.getListeNoeuds() + "\n"
                +"\tbenefMedical : "+this.getBenefMedicalSequence() + "\n"
                +"\ttaille max : "+this.getTailleMaxSequence() + "\n"
                +"}";
    }



    // Vérifie que le noeud est ajoutable (en terme de taille)
    // Attention
    private boolean isNoeudAjoutable(int position) {
        /*System.out.println(this.getListeNoeuds().size() < this.getTailleMaxSequence());
        System.out.println(position <= this.getListeNoeuds().size());
        System.out.println(position);
        System.out.println(this.getListeNoeuds().size());*/
        if ( (this.getListeNoeuds().size() < this.getTailleMaxSequence()) && (position > 0) && (position <= this.getListeNoeuds().size()) ) {
            return true;
        }else {
            System.out.println("Noeud non ajoutable");
            return false;
        }
    }

    // Vérifie si le noeud n à insérer est compatible avec les noeuds en position 'position-1' et 'posittion'
    // NB : Si le noeud est ajouté en bout de chaine, la compatibilité n'est pas testée avec le neoud suivant (puisqu'il n'y en a pas)
    private boolean isNoeudCompatible(Noeud n, int position) {
        Noeud noeudPrecedent = this.getListeNoeuds().get(position-1);
        //System.out.println(noeudPrecedent);
        if (this.getListeNoeuds().size() == position) { // Si le noeud est ajouté en bout de chaine
            System.out.println("Ajout en bout de chaine");
            if ( noeudPrecedent.getBenefMedicalVers(n) != -1 ) {
                System.out.println("Noeud compatible");
                return true;
            }else {
                System.out.println("Noeud non compatible");
                return false;
            }
        }else {
            System.out.println("Ajout en milieu de chaine");
            Noeud noeudSuivant = this.getListeNoeuds().get(position);
            if ( (noeudPrecedent.getBenefMedicalVers(n) != -1 ) && (n.getBenefMedicalVers(noeudSuivant) > -1) ) {
                System.out.println("Noeud compatible");
                return true;
            }else {
                System.out.println("Noeud non compatible");
                return false;
            }
        }
    }

    //Permet d'ajouter le noeud dans la chaine.
    //1) Vérifie si le noeud est ajoutable en position n
    //2) Insère le noeud en retranchant le benef médical précédent, et ajoutant les nouveaux benefs médicaux.
    // Attention : La position match avec l'index (une insertion en 1ère position est isNoeudCompatible(0))
    // Si position passé en paramètre est 0, l'ajout n'est pas effecté car c'est la position de l'altruiste
    public boolean ajouterNoeud(Noeud n, int position) {
        System.out.println(" ---- vv ---- Ajout ---- vv ----");
        //NB : La position 0 est réservée à l'altruiste... On ne peut donc pas y ajouter un noeud
        if ( (position != 0) && this.isNoeudAjoutable(position) && this.isNoeudCompatible(n, position) ) {
            if (this.getListeNoeuds().size() == position) { //Si le noeud est ajouté en bout de chaine, on ne faut qu'ajouter le nouveau benef médical, sans en soustraire
                //Variables utiles
                System.out.println("aaaa");
                Noeud noeudPrecedent = this.getListeNoeuds().get(position-1);
                int benefMedical = this.getBenefMedicalSequence();
                //Traitements
                benefMedical += noeudPrecedent.getBenefMedicalVers(n);
                this.setBenefMedicalTotal(benefMedical);
                this.getListeNoeuds().add(n);
                System.out.println("Noeud ajouté avec succes en fin de chaine");
                System.out.println(" ---- ^^ ---- Ajout ---- ^^ ----");
                return true;
            }else {
                System.out.println("bbb");
                //Variables utiles
                Noeud noeudPrecedent = this.getListeNoeuds().get(position-1);
                Noeud noeudSuivant = this.getListeNoeuds().get(position);
                int benefMedical = this.getBenefMedicalSequence();
                //Traitements
                benefMedical -= noeudPrecedent.getBenefMedicalVers(noeudSuivant);
                benefMedical += noeudPrecedent.getBenefMedicalVers(n);
                benefMedical += n.getBenefMedicalVers(noeudSuivant);
                this.setBenefMedicalTotal(benefMedical);
                this.getListeNoeuds().add(position, n);
                System.out.println("Noeud ajouté avec succes en milieu de chaine");
                System.out.println(" ---- ^^ ---- Ajout ---- ^^ ----");
                return true;
            }

        }else {
            System.out.println("Peut pas ajouter le noeud");
            System.out.println(" ---- ^^ ---- Ajout ---- ^^ ----");
            return false;
        }

    }


    //--------------- Checker de chaine ---------------
    /**
     * Une chaine est valide si :
     * - Le premier noeud est un altruiste.
     * - Ne contient qu'un seul altruiste
     * - Le benef medical de la séquence est correctement calculé.
     * - La taille max de la séquence est respectée
     * */
    public boolean check() {
        return (verifTailleMax() && verifAltruistes() && verifBenefMedical());
    }
    //--
    private boolean verifTailleMax() {
        if (this.getTailleMaxSequence() >= this.getListeNoeuds().size()-1) { //Le '-1' est nécessaire car l'altruiste n'est pas stocké dans la liste de Noeuds
            System.out.println("verifTailleMax() : OK");
            return true;
        }else {
            System.out.println("verifTailleMax() : NOT_OK");
            return false;
        }
    }
    //--
    private boolean verifBenefMedical() {
        List listeNoeuds = this.getListeNoeuds();
        int somme = 0, i;
        Noeud noeudCourant, noeudSuivant;
        if (this.getListeNoeuds().size() > 1){
            //Calcul du bénef médical de l'altruiste initiant la chaine vers la 1ère paire
            //Pour chaque noeud de listeNoeuds, on calcule le benef médical vers le noeud suivant
            for (i=0 ; i<(listeNoeuds.size()-1) ; i++) { //On s'arrête à l'indice 'taille-1', pour manuellement calculer le bénéfice vers le 1er noeud (boucle)
                noeudCourant = (Noeud) listeNoeuds.get(i);
                noeudSuivant = (Noeud) listeNoeuds.get(i+1);
                somme += noeudCourant.getBenefMedicalVers(noeudSuivant);
            }

            if (somme == this.getBenefMedicalSequence()) {
                System.out.println("verifBenefMedical() : OK");
                return true;
            }else {
                System.out.println("verifBenefMedical() : NOT_OK");
                return false;
            }
        }else {
            return (this.getBenefMedicalSequence() == 0);
        }

    }

    // Méthode métier du check vérifiant que l'altruiste du début de chaine est le seul Altruiste de la chaine
    private boolean verifAltruistes() {
        int nbAltruistes = 0;
        for (Noeud n: this.getListeNoeuds()) {
            if (n instanceof Altruiste)
                nbAltruistes++;
        }
        if ((this.getListeNoeuds().get(0) instanceof Altruiste) && nbAltruistes == 1) {
            System.out.println("verifAltruistes() : OK");
            return true;
        }else {
            System.out.println("verifAltruistes() : NOT_OK");
            return false;
        }
    }
    //-------------------------------------------------


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
            Chaine c1 = new Chaine(5, a1); //chaine a1 [benef 0]
            c1.ajouterNoeud(p2, 1); //chaine a1-p2 [benef 4]
            c1.ajouterNoeud(p5, 2); //chaine a1-p2-p5 [benef 14]
            c1.ajouterNoeud(p7, 4); //chaine a1-p2-p5 [benef 14] ajout impossible, indice trop grand
            c1.ajouterNoeud(p7, 3); //chaine a1-p2-p5-p7 [benef 22]
            c1.ajouterNoeud(p6, 2); //chaine a1-p2-p5-p6-p7 [benef 22] ajout impossible, incompatible
            c1.ajouterNoeud(p6, 3); //chaine a1-p2-p5-p6-p7 [benef 28]
            System.out.println(c1);
            // ===> TEST OK

            System.out.println("Checker : "+c1.check());

        } catch(Exception e){
            System.out.println("ERROR test ajout");
            System.out.println(e.toString());
        }
    }

}
