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
public abstract class Sequence {
    //private int id = 0;
    private int benefMedicalSequence = 0;
    private ArrayList<Noeud> listeNoeuds =new ArrayList<>();
    private int tailleMaxSequence;

    /*public Sequence(Sequence s) {
        this.setTailleMaxSequence(s.getTailleMaxSequence());
        this.setBenefMedicalTotal(s.getBenefMedicalSequence());
        this.getBenefMedicalSequence()
    }*/

    public void increaseBenefMedicalSequence(int nb){
        this.benefMedicalSequence += nb;
    }
    public void decreaseBenefMedicalTotal(int nb){
        this.benefMedicalSequence -= nb;
    }
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

    public void setListeNoeuds(ArrayList<Noeud> listeNoeuds) {
        this.listeNoeuds = listeNoeuds;
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
        return false;
        /*if(infos==null) return false;
        if(!infos.isMouvementRealisable()) return false;

        Client client = infos.getClient();

        this.coutTotal += infos.getDeltaCout();
        this.clients.add(infos.getPosition(), client);
        this.demandeTotale += client.getQuantiteAMeLivrer();

        if(!this.check()){
            System.out.println("ERROR doInsertion");
            System.out.println(infos);
            System.exit(-1);
        }
        return true;*/
    }

    public boolean doDeplacement(IntraDeplacement infos){
        return false;
        /*if(infos==null) return false;
        if(!infos.isMouvementRealisable()) return false;

        Client clientI = infos.getClientI();
        Client clientJ = infos.getClientJ();
        int positionI = infos.getPositionI();
        int positionJ = infos.getPositionJ();

        // déplacement du client I avant le client J
        //System.out.println("je veux déplacer le client°" + clientI.getId() + " en position " + positionI + ", avant le client°" + clientJ.getId() + " en position " + positionJ);
        this.clients.remove(positionI);
        if(positionI < positionJ) this.clients.add(positionJ-1, clientI);
        if(positionI > positionJ) this.clients.add(positionJ, clientI);
        //System.out.println("le client en position " + this.clients.indexOf(clientI) + " est le " + this.clients.get(this.clients.indexOf(clientI)).getId());

        for(Client c : this.clients)
            System.out.print(c.getId() + " ");

        // maj du coût du déplacement
        //this.coutTotal += this.deltaCoutDeplacement(positionI, positionJ);
        this.coutTotal += infos.getDeltaCout();

        if(!this.check()){
            System.out.println("ERROR doDeplacement intra");
            System.out.println(infos);
            System.exit(-1);
        }
        return true;*/
    }

    public boolean doDeplacement(InterDeplacement infos){
        return false;
        /*if(infos==null) return false;
        if(!infos.isMouvementAmeliorant()) return false;

        Client clientI = infos.getClientI();
        int positionI = infos.getPositionI();
        int positionJ = infos.getPositionJ();

        // déplacement du client I avant le client J
        this.clients.remove(positionI);
        infos.getAutreTournee().clients.add(positionJ, clientI);

        // maj du coût du déplacement
        this.coutTotal += infos.getDeltaCoutTournee();
        infos.getAutreTournee().coutTotal += infos.getDeltaCoutAutreTournee();

        this.demandeTotale -= clientI.getQuantiteAMeLivrer();
        infos.getAutreTournee().demandeTotale += clientI.getQuantiteAMeLivrer();

        if(!this.check()){
            System.out.println("ERROR doDeplacement inter tournee courante");
            System.out.println(infos);
            System.exit(-1);
        }
        if(!infos.getAutreTournee().check()){
            System.out.println("ERROR doDeplacement inter autre tournee");
            System.out.println(infos);
            System.exit(-1);
        }
        return true;*/
    }

    /*public OperateurLocal getMeilleurOperateurInter(TypeOperateurLocal type, Sequence autreSequence) {
        OperateurLocal best = OperateurLocal.getOperateur(type);
        if(this.equals(autreSequence)) return best;
        for(int i=0; i<this.listeNoeuds.size(); i++) {
            for(int j=0; j<autreSequence.listeNoeuds.size()+1; j++) {
                OperateurInterSequence op = OperateurLocal.getOperateurInter(type, this, autreSequence, i, j);
                if(op.isMeilleur(best) && !ListeTabou.getInstance().isTabou(op)) {
                    best = op;
                }
            }
        }
        return best;
    }*/

    // renvoie le noeud de la séquence qui précéde la position position
    // position doit être comprise entre 0 et n-1
    private Noeud getPrec(int position){
        if(position == 0) return this.getListeNoeuds().get(position);
        return this.getListeNoeuds().get(position-1);
    }

    // renvoie le noeud de la séquence qui est actuellemment  la position position
    private Noeud getCurrent(int position) {
        if(position == this.getNbNoeuds()) return this.getListeNoeuds().get(0);
        return this.getListeNoeuds().get(position);
    }

    public abstract boolean equals(Sequence c);

    @Override
    public String toString() {
        return "\nSequence {" +
                "benefMedicalTotal=" + benefMedicalSequence +
                ", listeIdNoeuds=[" + this.getListeIdNoeuds() + "]}";
    }

    public String toStringShort() {
        String s = "";
        s += "[";
        for (Noeud n : this.getListeNoeuds()) {
            s += n.getId() + " ";
        }
        s += "] : " + this.getBenefMedicalSequence();
        return s;
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
