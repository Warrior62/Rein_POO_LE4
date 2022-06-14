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
    private int profondeurMax;
    private int largeurMax;
    private Instance instance;

    // --------------------------------------------------
    // --------------------------------------------------

    public Arbre(Noeud noeudRacine, Instance i, int profondeur, int largeur) {
        this.id = noeudRacine.getId();
        this.noeudRacine = noeudRacine;
        this.listeFils = new ArrayList<>();
        this.niveauProfondeur = 0;
        this.instance = i;
        this.profondeurMax = profondeur;
        this.largeurMax = largeur;
    }
    public Arbre(Noeud noeudRacine, Instance i, ArrayList<Arbre> listeFils, int profondeur, int largeur) {
        this(noeudRacine, i, profondeur, largeur);
        this.listeFils = listeFils;
    }

    public Arbre(Instance i) {
        this.instance = i;

        this.id = 0;
        this.noeudRacine = null;
        this.listeFils = new ArrayList<>();
        this.niveauProfondeur = 0;
        this.profondeurMax = 0;
    }

    public int getId() {
        return id;
    }

    public int getProfondeurMax() {
        return profondeurMax;
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
    public void remplirListeFils(){
        for(Map.Entry echange : this.noeudRacine.getListeEchanges().entrySet()){
            Noeud noeudFils = (Noeud) echange.getKey();
            Arbre a = new Arbre(noeudFils, this.instance, this.profondeurMax, this.largeurMax);
            a.niveauProfondeur = this.niveauProfondeur + 1;
            this.listeFils.add(a);
        }
    }


    // -- Methode récursive chargée de parcourir l'arbre et en extraire les chaines et cycles possibles
    // Fait ensuite appel de façon récursive à
    // Retourne un objet sequencesPossibles contenant les cycles et séquences possibles en LinkedHAshSet d'ids
    public void recurrArbre(LinkedHashSet<Integer> listeId, int profondeur, LinkedHashSet<Sequence> listeChainesPossibles, LinkedHashSet<Sequence> listeCyclesPossibles){
        profondeur++;
        LinkedHashSet<Integer> listeIdBis = new LinkedHashSet<Integer>();
        listeIdBis.addAll(listeId);
        if (listeIdBis.add(this.getId())) {
            if(profondeur < this.getProfondeurMax()){
                this.remplirListeFils();       //Récupération de ses fils
                //System.out.println("Current : " + this.getId());
                //System.out.println("////////////////////////");
                for(Arbre fils : this.getListeFils()) {
                    fils.recurrArbre(listeIdBis, profondeur, listeChainesPossibles, listeCyclesPossibles);
                }
            }else {
                if (chaineAjoutable(listeIdBis, listeChainesPossibles, instance)) {
                    //listeChainesPossibles.add(new Chaine(listeIdBis, this.instance));
                    ajouterChaine(listeIdBis, listeChainesPossibles, instance);
                }
            }
        } else { //Lorsque l'on détecte un cycle, il faut enregistrer le cycle et la chaîne que cela peut aussi former
            if (chaineAjoutable(listeIdBis, listeChainesPossibles, instance)) {
                ajouterChaine(listeIdBis, listeChainesPossibles, instance);
            }
            Iterator it = listeIdBis.iterator();
            int idCourant = (int) it.next();
            while (idCourant != this.getId()) {
                it.remove();
                idCourant = (int) it.next();
            }
            if (cycleAjoutable(listeIdBis, listeCyclesPossibles, instance))
                listeCyclesPossibles.add(new Cycle(listeIdBis, this.instance));
        }
    }


    public static void ajouterChaine(LinkedHashSet<Integer> listeId, LinkedHashSet<Sequence> listeChainesPossibles, Instance i) {
        LinkedHashSet<Integer> listeIdTemp = new LinkedHashSet<>();
        Iterator it = listeId.iterator();
        int id = (Integer) it.next();
        listeIdTemp.add(id);
        while (it.hasNext()) {
            id = (Integer) it.next();
            listeIdTemp.add(id);
            listeChainesPossibles.add(new Chaine(listeIdTemp, i));
        }
    }


    static boolean cycleAjoutable(LinkedHashSet<Integer> listeId, LinkedHashSet<Sequence> listeCyclesPossibles, Instance i) {
        //System.out.println("Cycle potentiel : " + listeId);
        /*System.out.println("Tentative d'ajout de cycle : " + listeId);*/
        //System.out.println("cycles actuels : ");
        /*for (Sequence ch : listeCyclesPossibles) {
            System.out.println(ch.toStringShort());
        }*/
        /*Iterator it = listeId.iterator();
        int id = (int) it.next(); //id du 1er noeud de la chaine (Altruiste potentiel)
        Noeud n = i.getTabNoeud()[id-1];*/

        if ( (listeId.size() <= i.getTailleMaxCycles()) ) {
            Cycle c = new Cycle(listeId, i);
            for (Sequence ch1 : listeCyclesPossibles) {
                if (ch1.equals(c)) {
                    //System.out.println("------------ refusé : deja ajouté");
                    return false;
                }
            }
            //System.out.println("------------ Accepté");
            return true;
        }else {
            /*System.out.println(listeId.size());
            System.out.println(i.getTailleMaxCycles());
            System.out.println("------------ refusé : tailleMAx");*/
            return false;
        }
    }


    //Méthode statique chargée de déterminée si une chaine peut être ajoutée aux chaines détectées.
    //3 critères sont appliqués :
    // - La chaine commence bien par un altruiste
    // - la chaine respecte le critère de tailleMAxChaine
    // - La chaine n'est pas déjà présente dans les chaines détectées
    // listeIdBis == liste d'ids représentant la chaine
    // i == Instance associée
    static boolean chaineAjoutable(LinkedHashSet<Integer> listeId, LinkedHashSet<Sequence> listeChainesPossibles, Instance i) {

        /*System.out.println("Tentative d'ajout de chaine : " + listeId);
        System.out.println("Chaines actuelles : ");
        for (Sequence ch : listeChainesPossibles) {
            System.out.println(ch.toStringShort());
        }*/
        Iterator it = listeId.iterator();
        int id = (int) it.next(); //id du 1er noeud de la chaine (Altruiste potentiel)
        Noeud n = i.getTabNoeud()[id-1];

        if ( (n instanceof Altruiste) && (listeId.size() <= i.getTailleMaxChaines()) ) {
            Chaine c = new Chaine(listeId, i);
            for (Sequence ch1 : listeChainesPossibles) {
                if (ch1.equals(c)) {
                    //System.out.println("------------ Ajout refusé");
                    return false;
                }
            }
            //System.out.println("------------ Ajout validé");
            return true;
        }else {
            //System.out.println("------------ Ajout refusé");
            return false;
        }
    }


    // Méthode "interface" permettant d'appeler les méthodes récursives de parcours de l'arbre de Noeuds afin de détecter toutes les séquences possibles.
    // Cette méthode n'est appelée qu'à partir d'un objet Arbre, et génère un arbre récursif sur les n premiers Noeuds (altruiste si possibles) de l'instance.
    // Retourne ensuite un objet SequencesPossibles contenant les sequences détectées.
    public SequencesPossibles detectionChainesCycles(int nbreArbres, int profondeur) {
        System.out.println("Detection chaines et cycles");

        LinkedHashSet<Sequence> listeChainesPossibles = new LinkedHashSet<Sequence>();
        LinkedHashSet<Sequence> listeCyclesPossibles = new LinkedHashSet<Sequence>();
        LinkedHashSet<Integer> listeId = new LinkedHashSet<Integer>();
        SequencesPossibles s = new SequencesPossibles();

        if (this.instance.getNbAltruistes() > 0) { //Si l'instance contient les altruistes
            for (int i=0; i<nbreArbres && i<this.instance.getTabAltruistes().size()  ; i++) {
                Arbre racine = new Arbre(this.instance.getTabAltruistes().get(i), this.instance, profondeur, this.largeurMax);
                //System.out.println("Current2 : ");
                //System.out.println(racine.getId());
                racine.recurrArbre(listeId, 0, listeChainesPossibles, listeCyclesPossibles);
            }
        }else { //si l'instance ne contient que des paires
            for (int i=0; i<nbreArbres && i<this.instance.getTabPaire().size()  ; i++) {
                Arbre racine = new Arbre(this.instance.getTabPaire().get(i), this.instance, profondeur, this.largeurMax);
                //System.out.println("Current1 : ");
                //System.out.println(racine.getId());
                racine.recurrArbre(listeId, 0, listeChainesPossibles, listeCyclesPossibles);
            }
        }

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
            return true;
        }else  {
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
            if (areNoeudsDisponibles(chaineCourante, noeudsIndisponibles)) {
                for (Altruiste a : listeAltruistes)
                if (isAltruisteCompatible(a, chaineCourante)) {
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
        long startTime = System.nanoTime();
        try{
            // --> Init <-- //
            InstanceReader reader = new InstanceReader("instances/KEP_p250_n0_k3_l0.txt");
            Instance i = reader.readInstance();
            Arbre a = new Arbre(i);
            // --> Détection chaines et cycles <-- //
            SequencesPossibles sequencesDetectees = a.detectionChainesCycles(7, 12);

            System.out.println("Sequeces Détectées : ");
            System.out.println(sequencesDetectees);

            Selecteur selecteur = new Selecteur(sequencesDetectees);
            SequencesPossibles solution, sol;
            solution = new SequencesPossibles();
            Iterator it = selecteur.getSequencesPossibles().getCycles().iterator();
            while (it.hasNext()) {
                Sequence s = (Sequence) it.next();
                sol = selecteur.arbreBestSol(s, i, 16, 60);
                if (sol.getBenefTotal() > solution.getBenefTotal())
                    solution = sol;
                System.out.println("_________________________________________________________________");
            }
            System.out.println("//////////////////");
            System.out.println(solution);
            System.out.println("//////////////////");

            long endTime = System.nanoTime();
            System.out.println("Tps : " + (endTime - startTime)/1000000 + " ms");  //divide by 1000000 to get milliseconds.

        } catch(Exception e){
            System.err.println(e);
        }
    }

    public void setPROFONDEUR_MAX(int i) {
    }
}
