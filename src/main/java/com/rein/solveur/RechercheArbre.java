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

import javax.sound.midi.Soundbank;
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

        Arbre arbre = new Arbre(instance.getTabNoeud()[0], instance);
        arbre.setPROFONDEUR_MAX(6);

        SequencesPossibles sequencesUtilisables = arbre.detectionChainesCycles();

        // Pour tous les altruistes

        if (instance.getNbAltruistes()>0) {

            for (int i = 0; i < instance.getNbAltruistes() - 1; i++) {
                if (instance.getTabNoeud()[i] instanceof Altruiste) {
                    Arbre arbre1 = new Arbre(instance.getTabNoeud()[i], instance);
                    if (i < 2) {
                        arbre1.setPROFONDEUR_MAX(6);
                    } else {
                        arbre1.setPROFONDEUR_MAX(4);
                    }
                    SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
                    sequencesUtilisables.getCycles().addAll(sequencesUtilisables1.getCycles());
                    sequencesUtilisables.getChaines().addAll(sequencesUtilisables1.getChaines());
                }
            }
        }
        else{
            if(instance.getTabPaire().size()==250){
                for (int i=0;i<10;i++){
                    Arbre arbre1 = new Arbre(instance.getTabNoeud()[i], instance);
                    arbre1.setPROFONDEUR_MAX(instance.getTailleMaxCycles());
                    SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
                    sequencesUtilisables.getCycles().addAll(sequencesUtilisables1.getCycles());
                }
            }
            else {
                for (int i = 0; i < instance.getTabPaire().size() - 1; i++) {
                    Arbre arbre1 = new Arbre(instance.getTabNoeud()[i], instance);
                    arbre1.setPROFONDEUR_MAX(instance.getTailleMaxCycles());
                    SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
                    sequencesUtilisables.getCycles().addAll(sequencesUtilisables1.getCycles());
                }
            }
        }

        // Si la solution n'a pas d'altruiste

        Selecteur selecteur = new Selecteur(sequencesUtilisables);
        SequencesPossibles sequencesSolution = selecteur.selectionPlusGrosBenef();

            Noeud[] noeudsNonUtilises = sequencesSolution.getNoeudsNonUtilises(instance);
            Instance instanceRestante = new Instance("instanceRestante",
                    instance.getNbPaires(), instance.getNbAltruistes(), instance.getTailleMaxCycles(),
                    instance.getTailleMaxChaines(), noeudsNonUtilises);

            for (int i = 0; i < instanceRestante.getTabPaire().size() - 1; i++) {
                Arbre arbre1 = new Arbre(instanceRestante.getTabNoeud()[i], instanceRestante);
                arbre1.setPROFONDEUR_MAX(instance.getTailleMaxCycles());
                SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
                //sequencesUtilisables.getCycles().addAll(sequencesUtilisables1.getCycles());
            }

            Selecteur selecteur2 = new Selecteur(sequencesUtilisables);
            SequencesPossibles sequencesSolution2 = selecteur2.selectionPlusGrosBenef();
            sequencesSolution.getCycles().addAll(sequencesSolution2.getCycles());
            sequencesSolution.getChaines().addAll(sequencesSolution2.getChaines());


        //GENERATION SOLUTION
        Solution s = new Solution(instance);
        s = s.generationSolution(sequencesSolution,instance);

        System.out.println(s);
        return s;
    }


    public static void main(String[] args) {
        InstanceReader reader = null;
        try {
            reader = new InstanceReader("instances/KEP_p100_n0_k3_l0.txt");
            Instance i = reader.readInstance();
            RechercheArbre ra = new RechercheArbre();
            Solution sol = new Solution(i);
            ra.solve(i);
        } catch (ReaderException e) {
            e.printStackTrace();
        }

    }
}
