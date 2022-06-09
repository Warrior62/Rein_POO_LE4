package com.rein.solveur;

import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.io.InstanceReader;
import com.rein.io.exception.ReaderException;
import com.rein.solution.Arbre;
import com.rein.solution.Selecteur;
import com.rein.solution.SequencesPossibles;
import com.rein.solution.Solution;
import com.rein.transplantation.Sequence;

import java.util.Iterator;
import java.util.LinkedHashSet;

public class ArbreSequences implements Solveur {

    @Override
    public String getNom() {
        return "Arbre Sequences";
    }

    @Override
    public Solution solve(Instance instance) {

        //Détection des Sequences
        Noeud n;
        if (instance.getNbAltruistes() > 0)
            n = instance.getTabAltruistes().get(0);
        else
            n = instance.getTabPaire().get(0);

        Arbre racine = new Arbre(n, instance, 6);
        SequencesPossibles sequencesUtilisables = racine.detectionChainesCycles();

        // Sélection des Sequences
        Selecteur selecteur = new Selecteur(sequencesUtilisables);
        Iterator it = sequencesUtilisables.getCycles().iterator();
        Sequence seqRacine = (Sequence) it.next();
        SequencesPossibles seqSelectionnees = selecteur.arbreBestSol(seqRacine, instance, 8, 3);

        //Génération de la Solution
        Solution s = seqSelectionnees.generationSolution(instance);
        System.out.println(s);
        return s;
    }

    public static void main(String[] args) {
        InstanceReader reader = null;
        try {
            reader = new InstanceReader("instances/KEP_p50_n3_k3_l4.txt");
            Instance i = reader.readInstance();
            ArbreSequences ra = new ArbreSequences();
            Solution sol = ra.solve(i);

            //System.out.println(sol);
        } catch (ReaderException e) {
            e.printStackTrace();
        }

    }
}
