package com.rein.solution;

import com.rein.instance.Altruiste;
import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.instance.Paire;
import com.rein.io.InstanceReader;
import com.rein.transplantation.Cycle;
import com.rein.transplantation.Sequence;

import java.text.CollationElementIterator;
import java.util.*;

public class Arbre implements Comparable {

    private int id;
    private Noeud noeudRacine;
    private ArrayList<Arbre> listeFils;
    private int niveauProfondeur;
    private int profondeurMax;
    private int largeurMax;
    private Instance instance;

    ///////////////////////////////////////////////////////////////////////////

    //Constructeurs
    /**
     * Constructeur par donnée d'Arbre de détection de séquences.
     * @param i Instance associée à l'arbre.
     * @param noeudRacine Noeud duquel l'arbre commence.
     * @param largeur Largeur de l'arbre.
     * @param profondeur profondeur de l'arbre.
     * */
    public Arbre(Noeud noeudRacine, Instance i, int profondeur, int largeur) {
        this.id = noeudRacine.getId();
        this.noeudRacine = noeudRacine;
        this.listeFils = new ArrayList<>();
        this.niveauProfondeur = 0;
        this.instance = i;
        this.profondeurMax = profondeur;
        this.largeurMax = largeur;
    }

    /**
     * @param i Instance associée à l'arbre.
     * @param noeudRacine Noeud duquel l'arbre commence.
     * @param largeur Largeur de l'arbre.
     * @param profondeur profondeur de l'arbre.
     * @param listeFils liste D'arbres fils, utilisés pour la génération récursive.
     * */
    public Arbre(Noeud noeudRacine, Instance i, ArrayList<Arbre> listeFils, int profondeur, int largeur) {
        this(noeudRacine, i, profondeur, largeur);
        this.listeFils = listeFils;
    }

    /**
     * Constructeur par copie d'Arbre de détection de séquences.
     * @param arbre Arbre à copier.
     * */
    public Arbre(Arbre arbre) {
        this.noeudRacine = arbre.noeudRacine;
        this.listeFils = arbre.listeFils;
    }

    /**
     * Constructeur par donnée de base d'Arbre de détection de séquences.
     * @param i Instance associée à l'arbre.
     * */
    public Arbre(Instance i) {
        this.instance = i;

        this.id = 0;
        this.noeudRacine = null;
        this.listeFils = new ArrayList<>();
        this.niveauProfondeur = 0;
        this.profondeurMax = 0;
    }

    ///////////////////////////////////////////////////////////////////////////

    //Getters
    public int getId() {
        return id;
    }

    public int getProfondeurMax() {
        return profondeurMax;
    }

    public Noeud getNoeudRacine() {
        return noeudRacine;
    }

    public ArrayList<Arbre> getListeFils() {
        return listeFils;
    }

    //Setters
    public void setNoeudRacine(Noeud noeudRacine) {
        this.noeudRacine = noeudRacine;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Méthode de calcul de la taille d'un arbre récursif de détection.
     * @ar
     * */
    public int taille(Arbre a){
        if(a == null) return 0;
        int tailleFils = 0;
        for(Arbre fils : this.listeFils)
            tailleFils += taille(fils);
        return 1 + tailleFils;
    }

    /**
     * Méthode de récupération et remplissage des Arbres fils de l'arbre courant.
     * Pour chaque échange possible, crée un Arbre (seulement si le noeud de ce dernier est disponible / non utilisé),
     * et ajoute cet arbre à la liste des arbres fils de l'arbre courant.
     * La liste est arrâtée si la largeur max de l'arbre est atteinte.
     * */
    public void remplirListeFils(){
        for(Map.Entry echange : this.noeudRacine.getListeEchanges().entrySet()){
            Noeud noeudFils = (Noeud) echange.getKey();
            if ((this.instance.getTabPaires().contains(noeudFils)) || this.instance.getTabAltruistes().contains(noeudFils) ) {
                Arbre a = new Arbre(noeudFils, this.instance, this.profondeurMax, this.largeurMax);
                a.niveauProfondeur = this.niveauProfondeur + 1;

                this.listeFils.add(a);
                Collections.sort(this.listeFils);
                if (this.listeFils.size() >= this.largeurMax) {
                    break;
                }
            }
        }
    }

    /**
     * Methode récursive chargée de parcourir l'arbre et en extraire les chaines et cycles possibles.
     * Stocke les ids des noeuds parcourus dans listeId, et détecte un cycle si 2 fois le même id y est ajouté.
     * Une chaine est détectée lorsque un cycle est détecté, ou qur la profondeur max est atteinte.
     * @param listeId liste des ids des noeuds parcourus par a recherche récursive.
     * @param profondeurCourante profondeur du noeud couramment analysé.
     * @param listeChainesPossibles LinkedHashSet des chaines détectées.
     * @param listeCyclesPossibles LinkedHashSet des cycles détectés.
     * */
    public void recurrArbre(LinkedHashSet<Integer> listeId, int profondeurCourante, LinkedHashSet<Sequence> listeChainesPossibles, LinkedHashSet<Sequence> listeCyclesPossibles){;
        profondeurCourante++;
        LinkedHashSet<Integer> listeIdBis = new LinkedHashSet<Integer>();
        listeIdBis.addAll(listeId);
        if (listeIdBis.add(this.getId())) {
            if(profondeurCourante < this.getProfondeurMax()){
                //System.out.println("Noeud courant : " + this.getId());
                //System.out.println("fils : ");
                this.remplirListeFils();       //Récupération de ses fils
                for(Arbre fils : this.getListeFils()) {
                    fils.recurrArbre(listeIdBis, profondeurCourante, listeChainesPossibles, listeCyclesPossibles);
                }
            }else {
                //System.out.println("Chaine détectée");
                //System.out.println(listeIdBis);
                if (chaineAjoutable(listeIdBis, listeChainesPossibles, instance)) {
                    ajouterChaine(listeIdBis, listeChainesPossibles, instance);
                }
            }
        } else { //Lorsque l'on détecte un cycle, il faut enregistrer le cycle et la chaîne que cela peut aussi former
            //System.out.println("Chaine détectée");
            //System.out.println(listeIdBis);
            if (chaineAjoutable(listeIdBis, listeChainesPossibles, instance)) {
                ajouterChaine(listeIdBis, listeChainesPossibles, instance);
            }
            Iterator it = listeIdBis.iterator();
            int idCourant = (int) it.next();
            while (idCourant != this.getId()) {
                it.remove();
                idCourant = (int) it.next();
            }
            //System.out.println("Cycle détecté");
            //System.out.println(listeIdBis);
            if (cycleAjoutable(listeIdBis, listeCyclesPossibles, instance)) {
                listeCyclesPossibles.add(new Cycle(listeIdBis, this.instance));
            }
        }
    }

    /**
     * Méthode fille de recurrArbre(), chargée de déterminer si une chaine est ajoutable aux chaines détectées.
     * Critères d'ajoutabilité :
     *    - La chaine commence bien par un altruiste
     *    - la chaine respecte le critère de tailleMAxChaine
     *    - La chaine n'est pas déjà présente dans les chaines détectées
     * @param listeId liste des ids int des noeuds à transformer en chaine.
     * @param listeChainesPossibles Liste des chaines déjà ajoutées.
     * @param i instance associée à la solution.
     * */
    static boolean chaineAjoutable(LinkedHashSet<Integer> listeId, LinkedHashSet<Sequence> listeChainesPossibles, Instance i) {
        Iterator it = listeId.iterator();
        int id = (int) it.next(); //id du 1er noeud de la chaine (Altruiste potentiel)
        Noeud n = i.getTabNoeud()[id-1];

        if ( (n instanceof Altruiste) && (listeId.size() <= i.getTailleMaxChaines()) ) {
            Chaine c = new Chaine(listeId, i);
            for (Sequence ch1 : listeChainesPossibles) {
                if (ch1.equals(c)) {
                    return false; //Ajout refusé : Chaine deja prsente
                }
            }
            return true; //Ajout validé
        }else {
            return false; //Ajout refusé : PAs altruiste ou taille trop grande
        }
    }

    /**
     * Méthode statique fille de recurrArbre(), chargée de créer et d'ajouter une chaine à la liste des chaines possibles,
     * à partir de sa liste d'ids.
     * @param listeId liste d'ids int des noeuds, à transformer en chaine.
     * @param listeChainesPossibles liste des chaines possibles détectées, à laquelle la nouvelle chaine doit être ajoutée.
     * @param i instance associée à la solution.
     * */
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

    /**
     * Méthode fille de recurrArbre(), chargée de déterminer si un cycle est ajoutable aux cycles détectés.
     * Critères d'ajoutabilité : le cycle n'a pas déjà été ajouté, et sa taille n'excède pas la taille max de cycle.
     * @param listeId liste des ids int des noeuds à transformer en cycle.
     * @param listeCyclesPossibles Liste des cycles déjà ajoutés.
     * @param i instance associée à la solution.
     * */
    static boolean cycleAjoutable(LinkedHashSet<Integer> listeId, LinkedHashSet<Sequence> listeCyclesPossibles, Instance i) {

        if ( (listeId.size() <= i.getTailleMaxCycles()) ) {
            Cycle c = new Cycle(listeId, i);
            for (Sequence ch1 : listeCyclesPossibles) {
                if (ch1.equals(c))
                    return false; //refusé : deja ajouté
            }
            return true; //accepté
        }else {
            return false; //refusé : tailleMax de cycle dépassée
        }
    }

    /**
     * Méthode statique fille de recurrArbre(), chargée de créer et d'ajouter un cycle à la liste des cycles possibles,
     * à partir de sa liste d'ids.
     * @param cycle liste d'ids int des noeuds, à transformer en cycle.
     * @param cyclesChoisis liste des chaines possibles détectées, à laquelle la nouvelle chaine doit être ajoutée.
     * @param noeudsIndisponibles Noeuds déjà pris.
     * */
    public static void ajouterCycle(LinkedHashSet<Integer> cycle, LinkedHashSet<LinkedHashSet> cyclesChoisis, LinkedHashSet<Integer> noeudsIndisponibles) {
        cyclesChoisis.add(cycle);

        for (int n : cycle) {
            noeudsIndisponibles.add(n);
        }
    }

    /**
     * Méthode 'interface' de détection des chaines et cycles. Fait appel à recurrArbre(), avec les
     * bons paramètres pour lancer la détection.
     * @param largeurMaxi Largeur max de l'arbre de détection.
     * @param profondeurMaxi profondeur max de l'arbre de détection.
     * @return un objet SequencesPossibles contenant l'ensemble des séquences détectées.
     * */
    public SequencesPossibles detectionChainesCycles(int largeurMaxi, int profondeurMaxi) {

        LinkedHashSet<Sequence> listeChainesPossibles = new LinkedHashSet<Sequence>();
        LinkedHashSet<Sequence> listeCyclesPossibles = new LinkedHashSet<Sequence>();
        LinkedHashSet<Integer> listeId = new LinkedHashSet<Integer>();
        SequencesPossibles s = new SequencesPossibles();
        ArrayList<Noeud> noeudsDepart = new ArrayList<>();

        //Ajout des Altruistes et Paires non détectés à noeudsDepart, pour que les noeuds déjà détectés soient exclus de la détection.
        if (this.instance.getNbAltruistes() > 0) //Si l'instance contient les altruistes
            noeudsDepart.addAll(this.instance.getTabAltruistes());

        noeudsDepart.addAll(this.instance.getTabPaires());

        // Run de 10 arbres de détection
        for (int i=0; i<10 && i<noeudsDepart.size()  ; i++) {
            Arbre racine = new Arbre(noeudsDepart.get(i), this.instance, profondeurMaxi, largeurMaxi);
            racine.recurrArbre(listeId, 0, listeChainesPossibles, listeCyclesPossibles);
        }

        //Ajout des sequences détectées à l'objet SequencesPossibles
        s.setCycles(listeCyclesPossibles);
        s.setChaines(listeChainesPossibles);
        return s;
    }

    /**
     * Méthode qui détecte si la séquence d'ids passés en paramètre est valide, en terme de disponibilité des noeuds.
     * Si oui retourne true, false ninon.
     * @param sequence séquence à analyser
     * @param noeudsIndisponibles noeuds disponibles.
     * @return true si la séquence est valide, false sinon.
     * */
    static boolean areNoeudsDisponibles(LinkedHashSet<Integer> sequence, LinkedHashSet<Integer> noeudsIndisponibles) {
        for (int n : noeudsIndisponibles) {
            if (sequence.contains(n))
                return false;
        }
        return true;
    }

    /**
     * Détermine si un altruiste est compatible avec une chaine d'ints.
     * @param a Altruiste analysé.
     * @param chaine Chaine avec la compatibilité est étudiée.
     * @return un boolean true si a est compatible avec chaine, false sinon.
     * */
    static boolean isAltruisteCompatible(Altruiste a, LinkedHashSet<Integer> chaine) {
        Paire premierePaire = new Paire(chaine.iterator().next());
        if (a.getBenefMedicalVers(premierePaire) > -1)
            return true;
        else
            return false;
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

    /**
     * Méthode toString de la classe Arbre.
     * @return la chaine descriptive de l'Arbre courant.
     * */
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
            SequencesPossibles sequencesDetectees = a.detectionChainesCycles(5, 20);

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

    @Override
    public int compareTo(Object o) {
        Arbre a = (Arbre) o;
        //return this.getNoeudRacine().getScorePopularite() - a.getNoeudRacine().getScorePopularite();
        //return a.getNoeudRacine().getScorePopularite() - this.getNoeudRacine().getScorePopularite();
        return a.getNoeudRacine().getScoreBenefice() - this.getNoeudRacine().getScoreBenefice();
        //return this.getNoeudRacine().getScoreBenefice() - a.getNoeudRacine().getScoreBenefice();
    }
}
