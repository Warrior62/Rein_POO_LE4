package com.rein.solveur;

import com.rein.instance.Altruiste;
import com.rein.instance.Instance;
import com.rein.io.InstanceReader;
import com.rein.io.exception.ReaderException;
import com.rein.solution.Arbre;
import com.rein.solution.Selecteur;
import com.rein.solution.SequencesPossibles;
import com.rein.solution.Solution;

public class MeilleureCombiSequence implements Solveur{

    @Override
    public String getNom() {
        return "Meilleure Combi Sequence";
    }

    @Override
    public Solution solve(Instance instance) {
        Arbre arbre = new Arbre(instance.getTabNoeud()[0], instance,6,0);
        SequencesPossibles sequencesUtilisables = arbre.detectionChainesCycles(); //6
        // Pour tous les altruistes
        if (instance.getNbAltruistes()>0) {
            for (int i = 0; i < instance.getNbAltruistes() - 1; i++) {
                if (instance.getTabNoeud()[i] instanceof Altruiste) {
                    int profondeur2 = 0;
                    if (i < 2) {
                        profondeur2 =6;
                    } else {
                        profondeur2 =4;
                    }
                    Arbre arbre1 = new Arbre(instance.getTabNoeud()[i], instance,profondeur2,0);
                    SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
                    sequencesUtilisables.getCycles().addAll(sequencesUtilisables1.getCycles());
                    sequencesUtilisables.getChaines().addAll(sequencesUtilisables1.getChaines());
                }
            }
        }
        else{
            if(instance.getTabPaire().size()==250){
                for (int i=0;i<10;i++){
                    Arbre arbre1 = new Arbre(instance.getTabNoeud()[i], instance,instance.getTailleMaxCycles(),0);
                    SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
                    sequencesUtilisables.getCycles().addAll(sequencesUtilisables1.getCycles());
                }
            }
            else {
                for (int i = 0; i < instance.getTabPaire().size() - 1; i++) {
                    Arbre arbre1 = new Arbre(instance.getTabNoeud()[i], instance,4,0);
                    SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
                    sequencesUtilisables.getCycles().addAll(sequencesUtilisables1.getCycles());
                }
            }
        }

        Selecteur selecteur = new Selecteur(sequencesUtilisables);
        SequencesPossibles sequencesSolution = selecteur.selectionCombiSequence();
        System.out.println("sequences possibles "+ sequencesSolution);
        Solution s = new Solution(instance);
        s = s.generationSolution(sequencesSolution,instance);
        return s;
    }

    public static void main(String[] args) {
        InstanceReader reader = null;
        try {
            reader = new InstanceReader("instances/KEP_p100_n0_k3_l0.txt");
            Instance i = reader.readInstance();
            MeilleureCombiSequence mcs = new MeilleureCombiSequence();
            Solution sol = new Solution(i);
            mcs.solve(i);
        } catch (ReaderException e) {
            e.printStackTrace();
        }
    }
}
