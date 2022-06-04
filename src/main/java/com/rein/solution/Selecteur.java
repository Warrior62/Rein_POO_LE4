package com.rein.solution;

import com.rein.instance.Noeud;
import com.rein.transplantation.Sequence;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Selecteur {


    private final SequencesPossibles sequencesPossibles;

    public Selecteur(SequencesPossibles sequencesPossibles) {
        this.sequencesPossibles = sequencesPossibles;
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


    public LinkedHashSet<Sequence> getSequencesParBenefice() {
        for(Sequence cyclePossible : this.sequencesPossibles.getCycles()){

        }
        for(Sequence chainePossible : this.sequencesPossibles.getChaines()){
            System.out.println(chainePossible);
        }
        return null;
    }

    /*public LinkedHashSet<Sequence> getSequencesParSociabilite() {

    }

    public LinkedHashSet<Sequence> getSequencesAleatoirement() {

    }

    public LinkedHashSet<Sequence> getSequencesParRacineAleatoire() {

    }

    public LinkedHashSet<Sequence> getSequencesParScoreRegret() {

    }

    public LinkedHashSet<Sequence> getSequencesParSimplex() {

    }*/
}
