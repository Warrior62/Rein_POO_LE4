package com.rein.solution;

import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.transplantation.Sequence;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.*;
import java.util.stream.Collectors;

public class Selecteur {


    private SequencesPossibles sequencesPossibles;
    private static int BENEFMAX = 0; //arbre
    private int profondeurMax; //arbre
    private int largeurMax; //arbre
    private LinkedHashSet<Sequence> sequencesFinales; //arbre
    private int benefFinal; //arbre

    public Selecteur(SequencesPossibles sequencesPossibles) {
        this.sequencesPossibles = sequencesPossibles;
        this.sequencesFinales = new LinkedHashSet<>();
        this.benefFinal = 0;
    }

    public SequencesPossibles arbreBestSol(Sequence sequenceRacine, Instance i, int profondeurArbre, int largeurArbre) {

        //System.out.println("Arbre best Sol !!");

        this.profondeurMax = profondeurArbre;
        this.largeurMax = largeurArbre;

        LinkedHashSet<Sequence> sequencesRestantes = new LinkedHashSet<Sequence>();
        sequencesRestantes.addAll(this.sequencesPossibles.getChaines());
        sequencesRestantes.addAll(this.sequencesPossibles.getCycles());

        Noeud[] noeudsRestants = i.getTabNoeud();

        //System.out.println(sequencesRestantes);

        SequencesPossibles solutionTrouvee = arbreSequences(sequenceRacine, 0, sequencesRestantes, noeudsRestants);

        return solutionTrouvee;
    }

    /////////////////////////////////////////////////////////////////////////
    public SequencesPossibles arbreSequences(Sequence sequenceCourante, int profondeur, LinkedHashSet<Sequence> sequencesRestantes, Noeud[] noeudsRestants) {
        int limite = 0;
        int profondeurBis = profondeur + 1;
        SequencesPossibles bestPossibilites = new SequencesPossibles();
        Noeud[] noeudsRestantsBis = noeudsRestants.clone();

        //System.out.println("################### Sequence courante : ");
        //System.out.println(sequenceCourante);
        LinkedHashSet<Sequence> sequencesFilles = getSequencesFilles(sequenceCourante, sequencesRestantes, noeudsRestantsBis);
        /*System.out.println("Sequences filles : ");
        for (Sequence s : sequencesFilles) {
            System.out.println(s.toStringShort());
        }*/

        if (profondeurBis < this.profondeurMax && !(sequencesFilles.isEmpty()) ) {
            //System.out.println("APPEL RECURSIF");
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

        /*System.out.println("## profondeur : ");
        System.out.println(profondeur);
        */
        /*System.out.println("Best solution : ");
        System.out.println(sequenceCourante);*/

        return bestPossibilites;
    }
    /////////////////////////////////////////////////////////////////////////

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
                //System.out.println("Sequence fille valide ----------------");
                sequencesFilles.add(s);
            }
        }

        //System.out.println(sequencesRestantesBis);
        return sequencesFilles;
    }

    public SequencesPossibles getSequencesPossibles() {
        return sequencesPossibles;
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

    /**
     * Méthode chargée de vérifier si la Séquence passé en paramètre peut être choisies dans la solution, à partir de la liste d'ids de Noeuds déjà selectionnée, dans sequencesChoisies.
     * Si la séquence peut être ajoutée, elle est ajoutée à sequencesChoisies et ses ids de Noeuds sont ajoutés à la liste d'ids de Noeuds utilisés, dans sequencesChoisies.
     * @param sequence Sequence à ajouter.
     * @param sequencesChoisies Objet SequencesPossibles permettant de tester si les noeuds de sequence sont disponibles, et dans laquelle elle est ajoutée si possible.
     * @return true si sequence est ajoutée à sequencesChoisies.
     * @return false si sequence n'est pas sequencesChoisies.
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



    public SequencesPossibles selectionCombiSequence(){
        LinkedHashSet<SequencesPossibles> solutionsPossibles = new LinkedHashSet<>();
        int count = 0;
        ArrayList<Sequence> arrayCycle= new ArrayList<>(this.sequencesPossibles.getCycles());
        Collections.sort(arrayCycle);
        ArrayList<Sequence> arrayChaine= new ArrayList<>(this.sequencesPossibles.getChaines());
        Collections.sort(arrayChaine);

        SequencesPossibles seqPosCycleChaine = new SequencesPossibles();
        seqPosCycleChaine.getCycles().addAll(arrayCycle);
        seqPosCycleChaine.getChaines().addAll(arrayChaine);
       // seqPosCycleChaine.setCycles(this.sequencesPossibles.getCycles());
       // seqPosCycleChaine.setChaines(this.sequencesPossibles.getChaines());

        Iterator<Sequence> seq = seqPosCycleChaine.getCycles().iterator();
        while(seq.hasNext() && count<2 ) {
            count ++;
            Sequence s = seq.next();
            System.out.println("SEQUENCE "+ this.sequencesPossibles);
            SequencesPossibles sequencesPossibles = copy(this.sequencesPossibles);
            SequencesPossibles sequencesSolution = new SequencesPossibles();
            sequencesSolution.ajouterSequence(s);
            supprimerCombi(s,sequencesPossibles);
            trierSequence(sequencesPossibles, sequencesSolution,solutionsPossibles);
        }

        System.out.println("solutions possibles "+ solutionsPossibles);
        SequencesPossibles sequencesSolution = bestSolution(solutionsPossibles);
        return sequencesSolution;
    }




    public void trierSequence(SequencesPossibles seqPossibles,SequencesPossibles seqSolution, LinkedHashSet<SequencesPossibles> listeSolPossibles){
        if(seqPossibles.getChaines().size()== 0 && seqPossibles.getCycles().size() == 0){
            System.out.println("--- Ajout solution "+ seqSolution);
            listeSolPossibles.add(seqSolution);
        }
        else {
            LinkedHashSet<Sequence> chaines = seqPossibles.getChaines();
            Iterator<Sequence> seqCh = chaines.iterator();
            while(seqCh.hasNext()&& chaines.size()>0) {
                Sequence s = seqCh.next();
                seqSolution.ajouterSequence(s);
                SequencesPossibles seqRestantes = copy(seqPossibles);
                supprimerCombi(s, seqRestantes);
                trierSequence(seqRestantes, seqSolution, listeSolPossibles);
            }

            LinkedHashSet<Sequence> cycles = seqPossibles.getCycles();
            Iterator<Sequence> seqCy = cycles.iterator();
            while(seqCy.hasNext() && cycles.size()>0) {
                Sequence s = seqCy.next();
                seqSolution.ajouterSequence(s);
                SequencesPossibles seqRestantes = copy(seqPossibles);
                supprimerCombi(s, seqRestantes);
                trierSequence(seqRestantes, seqSolution, listeSolPossibles);
            }
            seqPossibles.setCycles(cycles);
        }
    }

    public SequencesPossibles bestSolution(LinkedHashSet<SequencesPossibles> listeSolPossibles){

        SequencesPossibles bestSolution = new SequencesPossibles();
        for(SequencesPossibles solPossible : listeSolPossibles){
            if(solPossible.calculBenefTotal()>bestSolution.calculBenefTotal()){
                bestSolution = solPossible;
            }
        }
        return bestSolution;
    }
    public SequencesPossibles supprimerCombi(Sequence seq,SequencesPossibles sequencesPossibles){
        for(Noeud n: seq.getListeNoeuds()){
            Iterator<Sequence> ich = sequencesPossibles.getChaines().iterator();
            while (ich.hasNext()) {
                Sequence s = ich.next();
                if(s.getListeNoeuds().contains(n)){
                    //System.out.println("Le noeud "+ n +" est dans la séquence "+ s);
                    ich.remove();
                }
            }
            Iterator<Sequence> i = sequencesPossibles.getCycles().iterator();
            while (i.hasNext()) {
                Sequence s = i.next();
                if(s.getListeNoeuds().contains(n)){
                    //System.out.println("Le noeud "+ n +" est dans la séquence "+ s);
                    i.remove();
                }
            }
        }
        return sequencesPossibles;
    }

    public SequencesPossibles copy(SequencesPossibles sequencesPossibles){
        SequencesPossibles sequencesPossibles1 = new SequencesPossibles();
        sequencesPossibles1.getCycles().addAll(sequencesPossibles.getCycles());
        sequencesPossibles1.getChaines().addAll(sequencesPossibles.getChaines());
        sequencesPossibles1.getNoeudsUtilises().addAll(sequencesPossibles.getNoeudsUtilises());
        sequencesPossibles1.setBenefTotal(sequencesPossibles.getBenefTotal());

        return sequencesPossibles1;
    }
    /**
     *
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
