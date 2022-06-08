package com.rein.solution;

import com.rein.instance.Altruiste;
import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.instance.Paire;
import com.rein.io.InstanceReader;
import com.rein.transplantation.Cycle;
import com.rein.transplantation.Sequence;

import java.util.*;

public class Arbre {

    private int id;
    private Noeud noeudRacine;
    private ArrayList<Arbre> listeFils;
    private int niveauProfondeur;
    private int PROFONDEUR_MAX = 1;
    private Instance instance;

    // --------------------------------------------------
    // --------------------------------------------------

    public Arbre(Noeud noeudRacine, Instance i) {
        this.id = noeudRacine.getId();
        this.noeudRacine = noeudRacine;
        this.listeFils = new ArrayList<>();
        this.niveauProfondeur = 0;
        this.instance = i;
    }
    public Arbre(Noeud noeudRacine, Instance i, ArrayList<Arbre> listeFils) {
        this(noeudRacine, i);
        this.listeFils = listeFils;
    }

    public int getId() {
        return id;
    }
    public Arbre(Arbre arbre) {
        this.noeudRacine = arbre.noeudRacine;
        this.listeFils = arbre.listeFils;
    }
    public Noeud getNoeudRacine() {
        return noeudRacine;
    }
    public ArrayList<Arbre> getListeFils() {
        return listeFils;
    }
    public void setNoeudRacine(Noeud noeudRacine) {
        this.noeudRacine = noeudRacine;
    }
    public int taille(Arbre a){
        if(a == null) return 0;
        int tailleFils = 0;
        for(Arbre fils : this.listeFils)
            tailleFils += taille(fils);
        return 1 + tailleFils;
    }

    public int getPROFONDEUR_MAX() {
        return PROFONDEUR_MAX;
    }

    public void setPROFONDEUR_MAX(int PROFONDEUR_MAX) {
        this.PROFONDEUR_MAX = PROFONDEUR_MAX;
    }

    public void remplirListeFils(){
        for(Map.Entry echange : this.noeudRacine.getListeEchanges().entrySet()){
            Noeud noeudFils = (Noeud) echange.getKey();
            Arbre a = new Arbre(noeudFils, this.instance);
            a.setPROFONDEUR_MAX(this.PROFONDEUR_MAX);
            a.niveauProfondeur = this.niveauProfondeur + 1;
            this.listeFils.add(a);
        }
    }


    // -- Methode récursive chargée de parcourir l'arbre et en extraire les chaines et cycles possibles
    // Fait ensuite appel de façon récursive à
    // Retourne un objet sequencesPossibles contenant les cycles et séquences possibles en LinkedHAshSet d'ids
    public void recurrArbre(LinkedHashSet<Integer> listeId, int profondeur, LinkedHashSet<Sequence> listeChainesPossibles, LinkedHashSet<Sequence> listeCyclesPossibles){
        profondeur++;
        LinkedHashSet<Integer> listeIdBis = new LinkedHashSet<>(listeId);
        if (listeIdBis.add(this.getId())) {
            if(profondeur < this.PROFONDEUR_MAX){
                this.remplirListeFils();       //Récupération de ses fils
                for(Arbre fils : this.getListeFils()) {
                    fils.recurrArbre(listeIdBis, profondeur, listeChainesPossibles, listeCyclesPossibles);
                }
            }else {
                if (listeIdBis.size() <= this.instance.getTailleMaxChaines())
                    listeChainesPossibles.add(new Chaine(listeIdBis, this.instance));

            }
        }else { //Lorsque l'on détecte un cycle, il faut enregistrer le cycle et la chaîne que cela peut aussi former
            if (listeIdBis.size() <= this.instance.getTailleMaxChaines())
                listeChainesPossibles.add(new Chaine(listeIdBis, this.instance));
            Iterator it = listeIdBis.iterator();
            int idCourant = (int) it.next();
            while (idCourant != this.getId()) {
                it.remove();
                idCourant = (int) it.next();
            }
            if (listeIdBis.size() <= this.instance.getTailleMaxCycles())
                listeCyclesPossibles.add(new Cycle(listeIdBis, this.instance));
        }
    }

    //Fonction chargée de convertir le LinkedHashSet d'id 'listeIdCycle' en cycle
    //Retourne le cycle créé
    //Prend en entrée la liste d'id des noeuds dy cycle à former et
    /*public static Cycle ajouterCyclePossible(LinkedHashSet<Integer> listeIdCycle, Instance i) {
        Iterator it = listeIdCycle.iterator();
        Integer idCourant;
        Cycle c = new Cycle(i.getTailleMaxCycles());
        Noeud n;

        while (it.hasNext()) {
            idCourant = (Integer) it.next();
            n = i.getCopieNoeud(idCourant);
            c.ajouterNoeud()
        }
    }*/

    public SequencesPossibles detectionChainesCycles() {
        LinkedHashSet<Sequence> listeChainesPossibles = new LinkedHashSet<Sequence>();
        LinkedHashSet<Sequence> listeCyclesPossibles = new LinkedHashSet<Sequence>();
        LinkedHashSet<Integer> listeId = new LinkedHashSet<Integer>();
        SequencesPossibles s = new SequencesPossibles();

        //Détection des séquences via méthode récursive d'arbre.... Renvoie les cycles et chaines potentielles dans des LinkedHashSet<LinkedHashSet> (listeCyclesPossibles et listeChainesPossibles)
        this.recurrArbre(listeId, 0, listeChainesPossibles, listeCyclesPossibles);



        s.setCycles(listeCyclesPossibles);
        s.setChaines(listeChainesPossibles);
        return s;
    }

    //Retourne true si la sequence passée en paramètre est valide, en terme de disponibilité des noeuds
    // Retourne false sinon.
    static boolean areNoeudsDisponibles(LinkedHashSet<Integer> sequence, LinkedHashSet<Integer> noeudsIndisponibles) {
        for (int n : noeudsIndisponibles) {
            if (sequence.contains(n)) {
                return false;
            }
        }
        return true;
    }

    static boolean isAltruisteCompatible(Altruiste a, LinkedHashSet<Integer> chaine) {
        Paire premierePaire = new Paire(chaine.iterator().next());
        if (a.getBenefMedicalVers(premierePaire) > -1) {
            //System.out.println("COMPATIBLE : " + a.getId() + " --> " + premierePaire.getId());
            //System.out.println(a.getBenefMedicalVers(premierePaire));
            return true;
        }else  {
            //System.out.println("NON COMPATIBLE : " + a.getId() + " --> " + premierePaire.getId());
            //System.out.println(a.getBenefMedicalVers(premierePaire));
            return false;
        }
    }

    public static void ajouterCycle(LinkedHashSet<Integer> cycle, LinkedHashSet<LinkedHashSet> cyclesChoisis, LinkedHashSet<Integer> noeudsIndisponibles) {
        cyclesChoisis.add(cycle);

        for (int n : cycle) {
            noeudsIndisponibles.add(n);
        }
    }

    public static LinkedHashSet<LinkedHashSet> selectionChainesPremieres(LinkedHashSet<LinkedHashSet> chainesPossibles, LinkedHashSet<Integer> noeudsIndisponibles, ArrayList<Altruiste> listeAltruistes) {
        Iterator it = chainesPossibles.iterator();
        LinkedHashSet listeAltruistesDisponibles = new LinkedHashSet(listeAltruistes);
        LinkedHashSet<LinkedHashSet> chainesChoisies = new LinkedHashSet<LinkedHashSet>();

        while (it.hasNext()) {
            LinkedHashSet<Integer> chaineCourante = (LinkedHashSet) it.next();
            System.out.println("Aanalyse chaine : ");
            System.out.println(chaineCourante);
            if (areNoeudsDisponibles(chaineCourante, noeudsIndisponibles)) {
                System.out.println("NOEUDS DISPONIBLES");
                for (Altruiste a : listeAltruistes)
                if (isAltruisteCompatible(a, chaineCourante)) {
                    System.out.println("ALTRUISTE COMPATIBLE");
                    ajouterChaine(chaineCourante, chainesChoisies, noeudsIndisponibles, a, listeAltruistesDisponibles);
                }
            }
        }

        return chainesChoisies;
    }

    public static void ajouterChaine(LinkedHashSet<Integer> chaineCourante, LinkedHashSet<LinkedHashSet> chainesChoisies, LinkedHashSet<Integer> noeudsIndisponibles, Altruiste a, LinkedHashSet<Altruiste> listeAltruistesDisponibles) {
        chaineCourante.add(a.getId());
        chainesChoisies.add(chaineCourante);
        listeAltruistesDisponibles.remove(a.getId());

        for (int n : chaineCourante) {
            noeudsIndisponibles.add(n);
        }


    }







    // --------------------------------------------------
    // --------------------------------------------------

    @Override
    public String toString() {
        String listeFils = "";
        for(Arbre fils : this.listeFils)
            listeFils += fils.getNoeudRacine().getId() + " ";

        return "\nArbre{" +
                "noeudRacine=" + noeudRacine.getId() +
                ", listeFils=[" + listeFils + "]}";
    }


    public static void main(String[] args) {
        try{
            // --> Init <-- //
            InstanceReader reader = new InstanceReader("instances/KEP_p250_n83_k5_l17.txt");
            Instance i = reader.readInstance();
            Arbre racine = new Arbre(i.getTabNoeud()[0], i);
            ArrayList<Altruiste> AltruistesDispo = i.getTabAltruistes();
            // --> Init <-- //

            // --> Algorithme <-- //
            SequencesPossibles sequencesDetectees = racine.detectionChainesCycles();
            System.out.println("$$");
            Selecteur selecteur = new Selecteur(sequencesDetectees);
            SequencesPossibles sequencesChoisies = selecteur.selectionPlusGrosBenef();
            System.out.println(sequencesChoisies);
            System.out.println("$$");
            // --> Algorithme <-- //

            System.out.println("Cycles : ");
            System.out.println(sequencesDetectees.getCycles());
            System.out.println("Chaines : ");
            System.out.println(sequencesDetectees.getChaines());
        } catch(Exception e){
            System.err.println(e);
        }
    }
}
