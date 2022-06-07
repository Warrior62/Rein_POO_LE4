package com.rein.solveur;

import com.rein.instance.Altruiste;
import com.rein.instance.Echange;
import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.io.InstanceReader;
import com.rein.io.exception.ReaderException;
import com.rein.solution.*;
import com.rein.transplantation.Cycle;
import com.rein.transplantation.Sequence;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.ArrayUtils;

import java.sql.Array;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;

public class RechercheArbre implements Solveur {


    @Override
    public String getNom() {
        return "Recherche Arbre";
    }

    @Override
    public Solution solve(Instance instance) {
        Solution s = new Solution(instance);

        Arbre arbre = new Arbre(instance.getTabNoeud()[0], instance);
        SequencesPossibles sequencesUtilisables = arbre.detectionChainesCycles();

        Selecteur selecteur = new Selecteur(sequencesUtilisables);
        SequencesPossibles sequencesSolution = selecteur.selectionRandom_v1();
        SequencesPossibles seqSol2 = selecteur.selectionPlusGrosBenef();

        //lancer la fonction de recherche arbre
        LinkedHashSet<Sequence> tabCycle = sequencesSolution.getCycles();
        LinkedHashSet<Sequence> tabChaine = sequencesSolution.getChaines();
      for (Sequence seq : tabCycle){
          s.ajouterSequence(seq);
      }
      for (Sequence seq : tabChaine){
          s.ajouterSequence(seq);
      }

        System.out.println(s);
        return s;
    }

    public static void main(String[] args) {
        InstanceReader reader = null;
        try {
            reader = new InstanceReader("instances/KEP_p50_n3_k3_l7.txt");
            Instance i = reader.readInstance();
            RechercheArbre ra = new RechercheArbre();
            Solution sol = new Solution(i);
            ra.solve(i);
        } catch (ReaderException e) {
            e.printStackTrace();
        }

    }
}
