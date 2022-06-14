package com.rein.solveur;

import com.rein.instance.Altruiste;
import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.interface_web.InterfaceWeb;
import com.rein.io.InstanceReader;
import com.rein.io.exception.ReaderException;
import com.rein.solution.Arbre;
import com.rein.solution.Selecteur;
import com.rein.solution.SequencesPossibles;
import com.rein.solution.Solution;
import com.rein.transplantation.Sequence;

import java.io.IOException;
import java.util.*;

public class ArbreSequences implements Solveur {

    @Override
    public String getNom() {
        return "Arbre Sequences";
    }

    @Override
    public Solution solve(Instance instance) {
        System.out.println("Arbre sequences DEBUT");
        int path=0;
        //System.out.println("111 : " + instance.getNom());
        // --> Init <-- //
        Arbre a = new Arbre(instance);
        /*int profondeurMaxArbreDetection=0;
        int profondeurMaxArbreSelection=0;
        int largeurMaxArbreSelection=0;*/
        HashMap<String, Integer> parametres;

        // --> Paramétrage des arbres <-- //
        parametres = setUpParameters(instance.getNom());
        // --> Détection des séquences <-- //
        SequencesPossibles sequencesDetectees = a.detectionChainesCycles(5, parametres.get("profondeurMaxArbreDetection"));
        System.out.println("Sequeces Détectées : ");
        System.out.println(sequencesDetectees);
        Selecteur selecteur = new Selecteur(sequencesDetectees);
        SequencesPossibles sequencesChoisies, sol;
        sequencesChoisies = new SequencesPossibles();
        Iterator it = selecteur.getSequencesPossibles().getCycles().iterator();
        while (it.hasNext()) {
            Sequence s = (Sequence) it.next();
            sol = selecteur.arbreBestSol(s, instance, parametres.get("profondeurMaxArbreSelection"), parametres.get("largeurMaxArbreSelection"));
            if (sol.getBenefTotal() > sequencesChoisies.getBenefTotal())
                sequencesChoisies = sol;
            System.out.println("_________________________________________________________________");
        }

        System.out.println("//////////////////");
        System.out.println(sequencesChoisies);
        System.out.println("//////////////////");

        System.out.println(sequencesChoisies.toString());
        // --> Génération solution <-- //
        Solution s = sequencesChoisies.generationSolution(instance);
        //System.out.println(s);
        System.out.println("Arbre sequences FIN : " + path);

        return s;
    }


    private static HashMap setUpParameters(String nomInstance) {
        //System.out.println("aaah : " + nomInstance);
        HashMap<String, Integer> parametres = new HashMap<>();

        switch (nomInstance) {
            case "KEP_p9_n0_k3_l0.txt":
                parametres.put("profondeurMaxArbreSelection", 20);
                parametres.put("profondeurMaxArbreDetection", 30);
                parametres.put("largeurMaxArbreSelection", 10);
            break;

            case "KEP_p9_n1_k3_l3.txt":
                parametres.put("profondeurMaxArbreSelection", 30);
                parametres.put("profondeurMaxArbreDetection", 30);
                parametres.put("largeurMaxArbreSelection", 30);
            break;

            case "KEP_p18_n0_k4_l0.txt":
                parametres.put("profondeurMaxArbreSelection", 30);
                parametres.put("profondeurMaxArbreDetection", 30);
                parametres.put("largeurMaxArbreSelection", 30);
            break;

            case "KEP_p18_n2_k4_l4.txt":
                parametres.put("profondeurMaxArbreSelection", 40);
                parametres.put("profondeurMaxArbreDetection", 30);
                parametres.put("largeurMaxArbreSelection", 30);
                break;

            case "KEP_p50_n0_k3_l0.txt":
                parametres.put("profondeurMaxArbreSelection", 100);
                parametres.put("profondeurMaxArbreDetection", 100);
                parametres.put("largeurMaxArbreSelection", 100);
                break;

            case "KEP_p50_n3_k3_l4.txt":
                parametres.put("profondeurMaxArbreSelection", 100);
                parametres.put("profondeurMaxArbreDetection", 100);
                parametres.put("largeurMaxArbreSelection", 100);
                break;

            case "KEP_p50_n3_k3_l7.txt":
                parametres.put("profondeurMaxArbreSelection", 100);
                parametres.put("profondeurMaxArbreDetection", 100);
                parametres.put("largeurMaxArbreSelection", 100);
                break;

            case "KEP_p50_n3_k3_l13.txt":
                parametres.put("profondeurMaxArbreSelection", 100);
                parametres.put("profondeurMaxArbreDetection", 100);
                parametres.put("largeurMaxArbreSelection", 100);
                break;

            case "KEP_p50_n3_k5_l17.txt":
                parametres.put("profondeurMaxArbreSelection", 100);
                parametres.put("profondeurMaxArbreDetection", 100);
                parametres.put("largeurMaxArbreSelection", 100);
                break;

            case "KEP_p50_n6_k3_l4.txt":
                parametres.put("profondeurMaxArbreSelection", 100);
                parametres.put("profondeurMaxArbreDetection", 100);
                parametres.put("largeurMaxArbreSelection", 100);
                break;

            case "KEP_p50_n6_k3_l7.txt":
                parametres.put("profondeurMaxArbreSelection", 100);
                parametres.put("profondeurMaxArbreDetection", 20);
                parametres.put("largeurMaxArbreSelection", 20);
                break;

            case "KEP_p50_n6_k3_l13.txt":
                parametres.put("profondeurMaxArbreSelection", 100);
                parametres.put("profondeurMaxArbreDetection", 20);
                parametres.put("largeurMaxArbreSelection", 20);
                break;

            case "KEP_p50_n6_k5_l17.txt":
                parametres.put("profondeurMaxArbreSelection", 100);
                parametres.put("profondeurMaxArbreDetection", 20);
                parametres.put("largeurMaxArbreSelection", 20);
                break;

            case "KEP_p50_n17_k3_l7.txt":
                parametres.put("profondeurMaxArbreSelection", 100);
                parametres.put("profondeurMaxArbreDetection", 40);
                parametres.put("largeurMaxArbreSelection", 40);
                break;

            case "KEP_p50_n17_k5_l17.txt":
                parametres.put("profondeurMaxArbreSelection", 100);
                parametres.put("profondeurMaxArbreDetection", 40);
                parametres.put("largeurMaxArbreSelection", 40);
                break;

            case "KEP_p100_n0_k3_l0.txt":
                parametres.put("profondeurMaxArbreSelection", 20);
                parametres.put("profondeurMaxArbreDetection", 10);
                parametres.put("largeurMaxArbreSelection", 5);
                break;

            case "KEP_p100_n5_k3_l4.txt":
                parametres.put("profondeurMaxArbreSelection", 20);
                parametres.put("profondeurMaxArbreDetection", 10);
                parametres.put("largeurMaxArbreSelection", 5);
                break;

            case "KEP_p100_n5_k3_l7.txt":
                parametres.put("profondeurMaxArbreSelection", 7);
                parametres.put("profondeurMaxArbreDetection", 5);
                parametres.put("largeurMaxArbreSelection", 5);
                break;

            case "KEP_p100_n5_k3_l13.txt":
                parametres.put("profondeurMaxArbreSelection", 7);
                parametres.put("profondeurMaxArbreDetection", 5);
                parametres.put("largeurMaxArbreSelection", 5);
                break;

            /*case "":
                profondeurMaxArbreDetection=0;
                profondeurMaxArbreSelection=0;
                largeurMaxArbreSelection=0;
            break;*/
        }
        return parametres;
    }

    public static void main(String[] args) {
        InstanceReader reader = null;
        try {
            reader = new InstanceReader("instances/KEP_p100_n5_k3_l13.txt");
            Instance i = reader.readInstance();
            ArbreSequences ra = new ArbreSequences();
            System.out.println("================= SEQUENCE "+ i.getNom() +" =================");

            Solution sol = ra.solve(i);
            new InterfaceWeb(sol).createHtmlFile();
            //System.out.println(sol);
            System.out.println("Checker : " + sol.check());
        } catch (ReaderException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
