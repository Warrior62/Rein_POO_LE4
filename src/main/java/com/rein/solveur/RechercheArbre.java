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
            if(instance.getTabPaires().size()>200){
                for (int i=0;i<10;i++){
                    Arbre arbre1 = new Arbre(instance.getTabNoeud()[i], instance,5,0);
                    SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
                    sequencesUtilisables.getCycles().addAll(sequencesUtilisables1.getCycles());
                }
            }
            else if (instance.getTabPaires().size()<=200 && instance.getTabPaires().size()>50){
                for (int i = 0; i < instance.getTabPaires().size() - 1; i++) {
                    Arbre arbre1 = new Arbre(instance.getTabNoeud()[i], instance,4,0);
                    SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
                    sequencesUtilisables.getCycles().addAll(sequencesUtilisables1.getCycles());
                }
            }
            else {
                for (int i=0;i<instance.getTabPaires().size() - 1;i++){
                    Arbre arbre1 = new Arbre(instance.getTabNoeud()[i], instance,4,0);
                    SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
                    sequencesUtilisables.getCycles().addAll(sequencesUtilisables1.getCycles());
                }
            }
        }

        Selecteur selecteur = new Selecteur(sequencesUtilisables);
        SequencesPossibles sequencesSolution = selecteur.selectionParBenef(false);

        Noeud[] noeudsNonUtilises = sequencesSolution.getNoeudsNonUtilises(instance);
        SequencesPossibles sequencesRestantes = new SequencesPossibles();
        Instance instanceRestante = new Instance("instanceRestante",
                instance.getNbPaires(), instance.getNbAltruistes(), instance.getTailleMaxCycles(),
                instance.getTailleMaxChaines(), noeudsNonUtilises);

        for (int i = 0; i < instanceRestante.getTabPaires().size() - 1; i++) {
            Arbre arbre1 = new Arbre(instanceRestante.getTabNoeud()[i],instanceRestante,4,0);
            SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
            sequencesRestantes.getCycles().addAll(sequencesUtilisables1.getCycles());
        }

        Selecteur selecteur2 = new Selecteur(sequencesRestantes);
        SequencesPossibles sequencesSolution2 = selecteur2.selectionParBenef(false);
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
