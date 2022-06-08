/**
 *
 * @author Mathis
 * Project : rein-poo-le4
 * Cycle in transplantation
 * Description : Cycle class
 */
package com.rein.transplantation;
import com.rein.instance.Altruiste;
import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.io.InstanceReader;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
public class Cycle extends Sequence {
    public Cycle(int tailleMax){
        this.setTailleMaxSequence(tailleMax);
    }

    //Constructeur chargé de créer un cycle à partir d'un LinkedHashSet d'ids
    //Prend en paramètres le LinkedHasSet d'ids 'idsDeNoeuds'
    //Retourne en sortie le nouveau cycle créé
    public Cycle(LinkedHashSet<Integer> idsDeNoeuds, Instance i) {
        Iterator it = idsDeNoeuds.iterator();
        int idPrec, idCour;
        //Cycle c = new Cycle(i.getTailleMaxCycles());
        Noeud nPrec, nCour, nPremier;
        int countBenefMedical = 0;

        idCour = (int) it.next();
        nPremier = nCour = new Noeud(i.getTabNoeud()[idCour-1]);
        this.getListeNoeuds().add(nCour);

        while (it.hasNext()) {
            //On récupère une copie du noeud, on l'ajoute et on calcule le benef medical
            idPrec = idCour;
            idCour = (Integer) it.next();
            nPrec = nCour;
            nCour = new Noeud(i.getTabNoeud()[idCour-1]);
            this.getListeNoeuds().add(nCour);

            /*System.out.println(nPrec.getBenefMedicalVers(nCour));
            System.out.println("id : " + nPrec.getId() + " - " + nCour.getId());
            System.out.println(nPrec.getListeEchanges());*/
            countBenefMedical += nPrec.getBenefMedicalVers(nCour);
            //System.out.println("Le benef medical est de  : " + countBenefMedical);
        }
        countBenefMedical += nCour.getBenefMedicalVers(nPremier);


        //SetUp du cycle
        this.setTailleMaxSequence(i.getTailleMaxCycles());
        this.setBenefMedicalTotal(countBenefMedical);
    }

    public String toString() {
        return "Cycle : { \n"
                + "\tlisteNoeuds : "+this.getListeNoeuds() + "\n"
                +"\tbenefMedical : "+this.getBenefMedicalSequence() + "\n"
                +"\ttaille max : "+this.getTailleMaxSequence() + "\n"
                +"}";
    }
    //--------------- Checker de cycle ---------------
    /**
     * Un cycle est valide si :
     * - Le dernier noeud donne au premier && Le premier reçoit du dernier.
     * - Ne contient pas d'altruiste.
     * - Chaque noeud n'est présent qu'une seule fois.
     * - Le benef medical de la séquence est correctement calculé.
     * - La taille max de la séquence est respectée
     * */
    public boolean check() {
        return (verifTailleMax() && verifNoAltruiste() && verifBenefMedical());
    }
    private boolean verifTailleMax() {
        if (this.getListeNoeuds().size() <= this.getTailleMaxSequence())
            return true;
        else
            return false;
    }
    private boolean verifBenefMedical() {
        List listeNoeuds = this.getListeNoeuds();
        int somme = 0, i;
        Noeud noeudCourant, noeudSuivant;
        //Pour chaque noeud de listeNoeuds, on calcule le benef médical vers le noeud suivant (attention à la fin on reboucle sur le premier noeud)
        for (i=0 ; i<(listeNoeuds.size()-1) ; i++) { //On s'arrête à l'indice 'taille-1', pour manuellement calculer le bénéfice vers le 1er noeud (boucle)
            noeudCourant = (Noeud) listeNoeuds.get(i);
            noeudSuivant = (Noeud) listeNoeuds.get(i+1);
            somme += noeudCourant.getBenefMedicalVers(noeudSuivant);
        }
        noeudCourant = (Noeud) listeNoeuds.get(i);
        noeudSuivant = (Noeud) listeNoeuds.get(0);
        somme += noeudCourant.getBenefMedicalVers(noeudSuivant);
        return (somme == this.getBenefMedicalSequence());
    }
    private boolean verifNoAltruiste() {
        for (Noeud n: this.getListeNoeuds()) {
            if (n instanceof Altruiste)
                return false;
        }
        return true;
    }
    //-------------------------------------------------
    //Méthode chargée d'ajouter un noeud dans un cycle.
    //1) Vérifie si le noeud est ajoutable en position n
    //2) Insère le noeud en retranchant le benef médical précédent, et ajoutant les nouveaux benefs médicaux.
    // NB : 3 cas principaux :
    //      - Ajout en 'début' de cycle (au début de la liste)
    //      - Ajout en 'fin' de cycle (à la fin de la liste)
    //      - Ajout en 'milieu' de cycle (au milieu du cycle)
    // Attention : La position match avec l'index (ex : une insertion en 1ère position est isNoeudCompatible(0))
    public boolean ajouterNoeud(Noeud n, int position) {
        Noeud noeudPrecedent;
        Noeud noeudSuivant;
        int benefMedical = this.getBenefMedicalSequence();
        if (this.getListeNoeuds().size() == 0) {
            this.getListeNoeuds().add(n);
           // System.out.println("Methode rustine 1");
            return true;
        }
        if (this.getListeNoeuds().size() == 1 && (position == 0 || position == 1) ) {
            int benef1 = n.getBenefMedicalVers(this.getListeNoeuds().get(0));
            int benef2 = this.getListeNoeuds().get(0).getBenefMedicalVers(n);
            /*System.out.println(this.getBenefMedicalSequence());
            System.out.println(benef1);
            System.out.println(benef2);
            System.out.println(this.getListeNoeuds().get(0));
            System.out.println(n);*/
            this.getListeNoeuds().add(position, n);
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
            this.getListeNoeuds().add(position, n);
            return true;
        }else {
           // System.out.println("Peut pas ajouter le noeud");
            return false;
        }
    }
    // Vérifie si le noeud n à insérer est compatible avec les noeuds en position 'position-1' et 'posittion'
    // NB : 3 cas principaux :
    //      - Ajout en 'début' de cycle (au début de la liste)
    //      - Ajout en 'fin' de cycle (à la fin de la liste)
    //      - Ajout en 'milieu' de cycle (au milieu du cycle)
    //      Si le noeud est ajouté en bout de cycle, la compatibilité n'est pas testée avec le neoud suivant (puisqu'il n'y en a pas)
    private boolean isNoeudCompatible(Noeud n, int position) {
        Noeud noeudPrecedent;
        Noeud noeudSuivant;
        if (position == 0) { // Ajout en début de cycle
            noeudPrecedent = this.getListeNoeuds().get(this.getListeNoeuds().size()-1);
            noeudSuivant = this.getListeNoeuds().get(0);
            //System.out.println("Ajout en milieu de cycle");
        }else if (position == this.getListeNoeuds().size()) { //Ajout en fin de cycle
            noeudPrecedent = this.getListeNoeuds().get(this.getListeNoeuds().size()-1);
            noeudSuivant = this.getListeNoeuds().get(0);
            //System.out.println("Ajout en debut de cycle");
        }else { // Ajout en milieu de cycle
            noeudPrecedent = this.getListeNoeuds().get(position-1);
            noeudSuivant = this.getListeNoeuds().get(position);
            //System.out.println("Ajout en milieu de cycle");
        }
        if (noeudPrecedent.getBenefMedicalVers(n) != -1 && n.getBenefMedicalVers(noeudSuivant) != 1) { //Compatibilité OK
            //System.out.println("Noeud compatible");
            return true;
        }else {
            //System.out.println("Noeud non compatible");
            return false;
        }
    }
    // Vérifie que le noeud est ajoutable (en terme de taille)
    private boolean isNoeudAjoutable(int position) {
        if ( (this.getListeNoeuds().size() < this.getTailleMaxSequence()) && (position <= this.getListeNoeuds().size()) ) {
            return true;
        }else {
            //System.out.println("Noeud non ajoutable");
            return false;
        }
    }



    public static void main(String[] args) {
        InstanceReader reader;
        try {
            /*reader = new InstanceReader("instancesInitiales/KEP_p9_n1_k3_l3.txt");
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
            System.out.println("Checker : "+c1.check());*/
            reader = new InstanceReader("instancesInitiales/KEP_p9_n1_k3_l3.txt");
            Instance i = reader.readInstance();
            //System.out.println(i);
            Noeud[] tab = i.getTabNoeud();
            /*Altruiste a1 = (Altruiste) tab[0]; //id=1 - compatibilités vers {Paire{id=9}=10, Paire{id=5}=5, Paire{id=2}=4}
            Noeud p2 = tab[1]; //id=2 - compatibilités vers {Paire{id=9}=4, Paire{id=3}=7, Paire{id=5}=10, Paire{id=8}=9, Paire{id=4}=4}
            Noeud p3 = tab[2]; //id=3 - compatibilités vers {Paire{id=9}=4, Paire{id=6}=1, Paire{id=8}=10, Paire{id=2}=2, Paire{id=4}=6, Paire{id=10}=2}
            Noeud p4 = tab[3]; //id=4 - compatibilités vers {Paire{id=9}=3, Paire{id=3}=6, Paire{id=6}=6, Paire{id=5}=8}
            Noeud p5 = tab[4]; //id=5 - compatibilités vers {Paire{id=6}=6, Paire{id=7}=8}
            Noeud p6 = tab[5]; //id=6 - compatibilités vers {Paire{id=3}=2, Paire{id=8}=9, Paire{id=7}=8, Paire{id=4}=9}
            Noeud p7 = tab[6]; //id=7 - compatibilités vers {Paire{id=3}=1, Paire{id=6}=8, Paire{id=8}=8, Paire{id=2}=10}
            Noeud p8 = tab[7]; //id=8 - compatibilités vers {Paire{id=9}=9, Paire{id=6}=6, Paire{id=5}=8, Paire{id=7}=7, Paire{id=10}=4}
            Noeud p9 = tab[8]; //id=9 - compatibilités vers {Paire{id=4}=8}*/
            //System.out.println(i.getEchanges());

            Noeud p4 = tab[3];
            Noeud p5 = tab[4];
            Noeud p6 = tab[5];
            System.out.println(p4.getBenefMedicalVers(p5));
            System.out.println(p5.getBenefMedicalVers(p6));
            System.out.println(p6.getBenefMedicalVers(p4));

            LinkedHashSet<Integer> set = new LinkedHashSet<Integer>();
            set.add(4);
            set.add(5);
            set.add(6);

            Cycle c = new Cycle(set, i);
            System.out.println("Test : ");
            System.out.println(c.toString());

            System.out.println(c.check());

        } catch(Exception e){
            System.out.println("ERROR test ajout");
            System.out.println(e.toString());
        }
    }
}
