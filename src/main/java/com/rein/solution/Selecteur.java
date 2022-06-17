package com.rein.solution;

import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.transplantation.Sequence;

import java.util.*;

public class Selecteur {

    private static int BENEFMAX = 0;
    private int profondeurMax;
    private int largeurMax;
    private LinkedHashSet<Sequence> sequencesFinales;
    private int benefFinal;
    private final SequencesPossibles sequencesPossibles;

    /**
     * Constructeur par valeur de Selecteur.
     * @param sequencesPossibles Sequences possibles parmi lesquelles le selecteur devra sélectionner la solution
     * */
    public Selecteur(SequencesPossibles sequencesPossibles) {
        this.sequencesPossibles = sequencesPossibles;
        this.sequencesFinales = new LinkedHashSet<>();
        this.benefFinal = 0;
    }


    /////////////////////////////////////////////////////////////////////////

    //Getters
    public SequencesPossibles getSequencesPossibles() {
        return sequencesPossibles;
    }

    /////////////////////////////////////////////////////////////////////////

    public SequencesPossibles arbreBestSol(Sequence sequenceRacine, Instance i, int profondeurArbre, int largeurArbre) {

        this.profondeurMax = profondeurArbre;
        this.largeurMax = largeurArbre;

        LinkedHashSet<Sequence> sequencesRestantes = new LinkedHashSet<Sequence>();
        sequencesRestantes.addAll(this.sequencesPossibles.getChaines());
        sequencesRestantes.addAll(this.sequencesPossibles.getCycles());

        Noeud[] noeudsRestants = i.getTabNoeud();
        SequencesPossibles solutionTrouvee = arbreSequences(sequenceRacine, 0, sequencesRestantes, noeudsRestants);

        return solutionTrouvee;
    }

    public SequencesPossibles arbreSequences(Sequence sequenceCourante, int profondeur, LinkedHashSet<Sequence> sequencesRestantes, Noeud[] noeudsRestants) {
        int limite = 0;
        int profondeurBis = profondeur + 1;
        SequencesPossibles bestPossibilites = new SequencesPossibles();
        Noeud[] noeudsRestantsBis = noeudsRestants.clone();

        LinkedHashSet<Sequence> sequencesFilles = getSequencesFilles(sequenceCourante, sequencesRestantes, noeudsRestantsBis);

        if (profondeurBis < this.profondeurMax && !(sequencesFilles.isEmpty()) ) {
            for (Sequence seq: sequencesFilles) {
                if (limite < this.largeurMax) {
                    limite++;
                    SequencesPossibles s = arbreSequences(seq, profondeurBis, sequencesFilles, noeudsRestantsBis);
                    if (s.getBenefTotal() > bestPossibilites.getBenefTotal())
                        bestPossibilites = new SequencesPossibles(s);
                }else
                    break;
            }
        }

        bestPossibilites.ajouterSequence(sequenceCourante);

        return bestPossibilites;
    }

    public static LinkedHashSet<Sequence> getSequencesFilles(Sequence sequence, LinkedHashSet<Sequence> sequencesRestantes, Noeud[] noeudsRestants) {

        LinkedHashSet<Sequence> sequencesRestantesBis = new LinkedHashSet<Sequence>(sequencesRestantes);
        LinkedHashSet<Sequence> sequencesFilles = new LinkedHashSet<Sequence>(); //Pour les sequences filles
        LinkedHashSet<Noeud> noeudRestants;
        Sequence s;
        // Pour chaque séquence potentielle,
        // si la séquence ne contient aucun noeud des noeuds de la séquence courante,
        // alors on l'ajoute aux séquences filles
        Set diffTest = new HashSet();

        Iterator it = sequencesRestantesBis.iterator();
        while (it.hasNext()) {
            s = (Sequence) it.next(); //sequence restante
            diffTest.clear();
            diffTest.addAll(s.getListeNoeuds());
            diffTest.addAll(sequence.getListeNoeuds());

            if ( (s.getListeNoeuds().size() + sequence.getListeNoeuds().size()) == diffTest.size() ) {
                sequencesFilles.add(s);
            }
        }

        return sequencesFilles;
    }

    /**
     * Méthode permettant de récupérer un objet SequencesPossibles à transformer en objet solution
     * Critère de sélection des cycles et chaines : aléatoire.
     * @return Objet SequencesPossibles contenant les cycles et chaines formant la solution.
     * Contient également la liste des noeuds utilisés.
     * */
    public SequencesPossibles selectionRandom_v1() {
        int i;
        Sequence seqCourante;
        Iterator it;
        //Sequences recevant les séquences sélectionnées
        SequencesPossibles sequencesChoisies = new SequencesPossibles();
        //Set permettant de stocker les noeuds utilisés
        Set<Integer> noeudsUtilises = new HashSet<Integer>();
        //Tableaux dans lesquels sont transférés les cycles et chaines pour permettre un accès random
        ArrayList<Sequence> tabCycles = new ArrayList<Sequence>();
        ArrayList<Sequence> tabChaines = new ArrayList<Sequence>();

        tabCycles.addAll(this.sequencesPossibles.getCycles());
        tabChaines.addAll(this.sequencesPossibles.getChaines());

        //Selection des cycles
        Collections.shuffle(tabCycles);
        for (i=0 ; i<tabCycles.size() ; i++ ) {
            ajouterSequenceSiDispo(tabCycles.get(i), sequencesChoisies);
        }

        //Selection des chaines
        Collections.shuffle(tabChaines);
        for (i=0 ; i<tabChaines.size() ; i++ ) {
            ajouterSequenceSiDispo(tabChaines.get(i), sequencesChoisies);
        }

        return sequencesChoisies;
    }

    // méthode trop random
    public SequencesPossibles selectionRandom_v2() {
        SequencesPossibles bestsequencesChoisies = new SequencesPossibles();
        SequencesPossibles sequencesChoisies = new SequencesPossibles();

        for (int i=0;i<=10;i++) {
            sequencesChoisies = selectionRandom_v1();
            if (sequencesChoisies.calculBenefTotal() > bestsequencesChoisies.calculBenefTotal()) {
                bestsequencesChoisies = sequencesChoisies;
            }
        }

            return bestsequencesChoisies;
    }

    /**
     * Méthode chargée de vérifier si la Séquence passé en paramètre peut être choisies dans la solution, à partir de la liste d'ids de Noeuds déjà selectionnée, dans sequencesChoisies.
     * Si la séquence peut être ajoutée, elle est ajoutée à sequencesChoisies et ses ids de Noeuds sont ajoutés à la liste d'ids de Noeuds utilisés, dans sequencesChoisies.
     * @param sequence Sequence à ajouter.
     * @param sequencesChoisies Objet SequencesPossibles permettant de tester si les noeuds de sequence sont disponibles, et dans laquelle elle est ajoutée si possible.
     * @return true si sequence est ajoutée à sequencesChoisies, retourne false si sequence n'est pas sequencesChoisies.
     * */
    private boolean ajouterSequenceSiDispo (Sequence sequence, SequencesPossibles sequencesChoisies) {
        Iterator it = sequence.getListeNoeuds().iterator();
        LinkedHashSet<Integer> noeudsSequenceCourante = new LinkedHashSet<Integer>(); //Permet de stocker les ids de la 'sequence', pour les ajouter aux noeuds utilisés, si elle est Dispo
        while (it.hasNext()) {
            Noeud n = (Noeud) it.next();
            noeudsSequenceCourante.add(n.getId());
            //si l'un des noeuds de la séquence courante est utilisé, on passe au suivant
            if (sequencesChoisies.getNoeudsUtilises().contains(n.getId())) {
                return false;
            }
        }
        sequencesChoisies.getNoeudsUtilises().addAll(noeudsSequenceCourante);

        if (sequence instanceof Chaine)
            sequencesChoisies.getChaines().add(sequence);
        else
            sequencesChoisies.getCycles().add(sequence);

        return true;
    }

    /**
     * Vérifier si un noeud donné est utilisé
     * dans les sequences possibles du selecteur
     * @param noeud dont il faut vérifier l'utilisation
     * @return si le noeud donné est déjà utilisé
     */
    public boolean isNoeudUtilise(Noeud noeud) {
        return (this.sequencesPossibles.getNoeudsUtilises().contains(noeud.getId()));
    }

    /**
     * Tri les séquences dans l'ordre croissant ou décroissant
     * des bénéfices
     * @param unsortedMap ensemble des séquences possibles
     * @param isAsc true : croissant, false : décroissant
     * @return les séquences dans l'ordre croissant ou décroissant des bénéfices
     */
    private HashMap<Sequence, Integer> sortSequences(HashMap<Sequence, Integer> unsortedMap, boolean isAsc) {
        LinkedHashMap<Sequence, Integer> sortedMap = new LinkedHashMap<>();
        if(isAsc){
            unsortedMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        } else {
            unsortedMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        }
        return sortedMap;
    }

    /**
     * Tri dans l'ordre croissant ou décroissant des bénéfices
     * les sequences
     * @param type "cycle" ou "chaine"
     * @return les sequences de type type rangées
     * dans l'ordre croissant ou décroissant des bénéfices
     */
    public HashMap<Sequence, Integer> sortSequencesByBenef(String type, boolean isAsc) {
        HashMap<Sequence, Integer> unsortedMap = new HashMap<>();

        switch (type){
            case "cycle":
                for(Sequence s : this.sequencesPossibles.getCycles())
                    unsortedMap.put(s, s.getBenefMedicalSequence());
                break;
            case "chaine":
                for(Sequence s : this.sequencesPossibles.getChaines())
                    unsortedMap.put(s, s.getBenefMedicalSequence());
                break;
        }

        return sortSequences(unsortedMap, isAsc);
    }

    /**
     * Sélectionne d'abord les cycles puis les chaines
     * avec les plus gros bénéfices
     * @param type "cycle" ou "chaine"
     * @return liste de sequences choisies selon le benefice
     */
    public LinkedHashSet<Sequence> selectionSequencesBenef(String type, boolean isAsc) {
        LinkedHashSet<Sequence> sequencesChoisies = new LinkedHashSet<>();
        for(Map.Entry<Sequence, Integer> entry : this.sortSequencesByBenef(type, isAsc).entrySet()){
            Sequence sequence = entry.getKey();
            boolean isPresent = false;
            for(Noeud noeud : sequence.getListeNoeuds())
                if(isNoeudUtilise(noeud))
                    isPresent = true;
            if(!isPresent){
                sequencesChoisies.add(sequence);
                for(Noeud noeud : sequence.getListeNoeuds())
                    this.sequencesPossibles.getNoeudsUtilises().add(noeud.getId());
            }
        }
        return sequencesChoisies;
    }

    /**
     * Sélectionne les cycles puis les chaines
     * avec les plus gros ou petits bénéfices
     * @param isAsc true : croissant, false : décroissant
     * @return des sequences choisies selon le critère du bénéfice
     */
    public SequencesPossibles selectionParBenef(boolean isAsc) {
        SequencesPossibles sequencesChoisies = new SequencesPossibles();

        sequencesChoisies.getCycles().addAll(this.selectionSequencesBenef("cycle", isAsc));
        sequencesChoisies.getChaines().addAll(this.selectionSequencesBenef("chaine", isAsc));

        return sequencesChoisies;
    }
}
