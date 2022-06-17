package com.rein.solveur;

import com.rein.instance.*;
import com.rein.interface_web.InterfaceWeb;
import com.rein.io.InstanceReader;
import com.rein.io.exception.ReaderException;
import com.rein.operateur.InsertionNoeud;
import com.rein.solution.*;
import com.rein.transplantation.Cycle;
import com.rein.transplantation.Sequence;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.ArrayUtils;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.sql.Array;
import java.util.*;

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
            if(instance.getTabPaire().size()==250){
                for (int i=0;i<10;i++){
                    Arbre arbre1 = new Arbre(instance.getTabNoeud()[i], instance,instance.getTailleMaxCycles(),0);
                    SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
                    sequencesUtilisables.getCycles().addAll(sequencesUtilisables1.getCycles());
                }
            }
            else {
                for (int i = 0; i < instance.getTabPaire().size() - 1; i++) {
                    Arbre arbre1 = new Arbre(instance.getTabNoeud()[i], instance,instance.getTailleMaxCycles(),0);
                    SequencesPossibles sequencesUtilisables1 = arbre1.detectionChainesCycles();
                    sequencesUtilisables.getCycles().addAll(sequencesUtilisables1.getCycles());
                }
            }
        }

        Selecteur selecteur = new Selecteur(sequencesUtilisables);
        SequencesPossibles sequencesSolution = selecteur.selectionParBenef(false);

        Noeud[] noeudsNonUtilises = sequencesSolution.getNoeudsNonUtilises(instance);

        //GENERATION SOLUTION
        Solution s = new Solution(instance);
        s = s.generationSolution(sequencesSolution,instance);

        //INSERTION NOEUDS NON UTILISES
        for(int i = 0; i < noeudsNonUtilises.length; i++){
            for(Sequence sequence : s.getListeSequences()){
                if(sequence.getTailleMaxSequence() >= sequence.getListeNoeuds().size() + 1){
                    for(int j = 0; j < sequence.getListeNoeuds().size(); j++){
                        if(!sequence.getListeNoeuds().contains(noeudsNonUtilises[i])){
                            // si le noeud avant a un bénéf avec noeudsNonUtilises[i]
                            Noeud noeudPrec = sequence.getPrec(j);
                            if(noeudPrec.getListeEchanges().containsKey(noeudsNonUtilises[i])){
                                Noeud noeudCurrent = sequence.getCurrent(j);
                                // si le noeud après a un bénéf avec noeudsNonUtilises[i]
                                if(noeudCurrent.getListeEchanges().containsKey(noeudsNonUtilises[i])){
                                    if(sequence instanceof Chaine){
                                        // ne peut pas être en premier dans la chaîne
                                        // on insère noeudsNonUtilises[i] entre noeudPrec et NoeudNext
                                        if(j > 0 && (noeudsNonUtilises[i] instanceof Paire)){
                                            InsertionNoeud insertionNoeud = new InsertionNoeud(sequence, noeudsNonUtilises[i], j);
                                        }
                                    }
                                    if(sequence instanceof Cycle){
                                        // si ajout en dernier du cycle,
                                        // vérifier s'il y a un échange avec le premier du cycle
                                        InsertionNoeud insertionNoeud = new InsertionNoeud(sequence, noeudsNonUtilises[i], j);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        try{
            new InterfaceWeb(s).createHtmlFile();
        } catch (Exception e) {
            System.err.println("interface web erreur");
        }
        return s;
    }


    public static void main(String[] args) {
        InstanceReader reader = null;
        try {
            //reader = new InstanceReader("instances/KEP_p50_n6_k3_l13.txt");
            reader = new InstanceReader("instances/KEP_p50_n6_k3_l4.txt");
            Instance i = reader.readInstance();
            RechercheArbre ra = new RechercheArbre();
            Solution sol = ra.solve(i);
            System.out.println(sol.check());
            System.out.println("sol = " + sol);
        } catch (ReaderException e) {
            e.printStackTrace();
        }

    }
}
