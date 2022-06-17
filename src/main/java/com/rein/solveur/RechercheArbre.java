package com.rein.solveur;

import com.rein.instance.Altruiste;
import com.rein.instance.Echange;
import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.io.InstanceReader;
import com.rein.io.exception.ReaderException;
import com.rein.solution.*;

public class RechercheArbre implements Solveur {


    @Override
    public String getNom() {
        return "Recherche Arbre";
    }

    @Override
    public Solution solve(Instance instance) {

        Noeud n;
        //Détection des Sequences
        if (instance.getNbAltruistes() > 0) {
            n = instance.getTabAltruistes().get(0);
        }else {
            n = instance.getTabPaires().get(0);
        }

        Arbre racine = new Arbre(n, instance, 6, 5);
        //SequencesPossibles sequencesUtilisables = racine.detectionChainesCycles();

        //System.out.println(sequencesUtilisables);

        // Sélection des Sequences
        //Selecteur selecteur = new Selecteur(sequencesUtilisables);
        //Iterator it = sequencesUtilisables.getCycles().iterator();
        //Sequence seqRacine = (Sequence) it.next();
        //SequencesPossibles seqSelectionnees = selecteur.arbreBestSol(seqRacine, instance, 7, 2);

        //Génération de la Solution
        //Solution s = seqSelectionnees.generationSolution(instance);
        //Solution s = new Solution(instance);
        Solution s = new Solution(instance);
        return s;
    }

    public static void main(String[] args) {
        InstanceReader reader = null;
        try {
            reader = new InstanceReader("instances/KEP_p50_n3_k5_l17.txt");
            Instance i = reader.readInstance();
            RechercheArbre ra = new RechercheArbre();
            Solution sol = ra.solve(i);

            //System.out.println(sol);
        } catch (ReaderException e) {
            e.printStackTrace();
        }

    }
}
