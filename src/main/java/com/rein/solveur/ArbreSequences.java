package com.rein.solveur;

import com.rein.instance.Instance;
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

    /**
     * Méthode d'implémentation de la classe abstraite Solveur.
     * Renvoie le nom de la méthode de résolution utilisée.
     * @return La chaine conteant le nom de la méthode de solveur
     * */
    @Override
    public String getNom() {
        return "Arbre Sequences";
    }

    /**
     * Méthode de résolution d'instances de la classe ArbreSequences.
     * Fait appel aux méthodes métier detectionChainesCycles() de la classe Arbre et arbreBestSol() de la classe Selecteur,
     * respectivement pour détecter et selectionner les séquences.
     * @param instanceInitiale Instance à résoudre.
     * @return un objet Solution, contenant les cycles et chaines qui constituent la solution.
     * */
    @Override
    public Solution solve(Instance instanceInitiale) {
        Instance instance = new Instance(instanceInitiale); //Copie de l'instance, car elle est dégradée au fil de l'exécution du solveur
        System.out.println("Arbre sequences  : " + instance.getNom());

        HashMap<String, Integer> parametres; // Paramètres des dimensions des arbres (détection + selection)
        // --> Paramétrage des arbres <-- //
        parametres = setUpParameters(instance);
        SequencesPossibles sequencesDetectees, sequencesChoisies; //Objets récupérant les sequences détectées et selectionnées
        Selecteur selecteur;
        Sequence seq;
        Arbre a;
        Iterator it;
        Solution s = new Solution(instance);


        // --> 1er passage <-- //
        // -- Init
        System.out.println("~~~~~~~~~~~~ 1ER PASSAGE");
        a = new Arbre(instance);
        // detection des sequences
        sequencesDetectees = a.detectionChainesCycles(parametres.get("largeurMaxArbreDetection"), parametres.get("profondeurMaxArbreDetection"));
        System.out.println("<===>");
        // selection des sequences
        selecteur = new Selecteur(sequencesDetectees);
        sequencesChoisies = new SequencesPossibles();

        //Le selecteur est positionné sur la 1ère Chaine ou le 1er cycle en fonction des séquences détectées précédemment
        if (instance.getNbAltruistes() > 0)
            it = selecteur.getSequencesPossibles().getChaines().iterator();
        else
            it = selecteur.getSequencesPossibles().getCycles().iterator();
        seq = (Sequence) it.next();
        //Lancement de la sélection des séquences
        sequencesChoisies = selecteur.arbreBestSol(seq, instance, parametres.get("profondeurMaxArbreSelection"), parametres.get("largeurMaxArbreSelection"));
        //Ajout des séquences à la solution
        s.ajouterSequencesSelectionnees(sequencesChoisies);
        System.out.println("_________________________________________________________________");


        for (int i=0 ; i<7 ; i++) {
            System.out.println("~~~~~~~~~~~~ PASSAGE N°" + (i+2));
            //Retrait des altruistes et paires utilisés de l'instance
            instance.getTabAltruistes().removeAll(sequencesChoisies.getAltruistesUtilises());
            instance.getTabPaires().removeAll(sequencesChoisies.getPairesUtilisees());
            //Paramétrage des nombre d'altruistes et paires restants
            instance.setNbAltruistes(instance.getTabAltruistes().size());
            instance.setNbPaires(instance.getTabPaires().size());
            //Réinitialisattion des sequences detectées
            sequencesDetectees.setChaines(new LinkedHashSet<Sequence>());
            sequencesDetectees.setCycles(new LinkedHashSet<Sequence>());
            // Nouvelle sélection
            a = new Arbre(instance);
            //Nouvelle détection des séquences possibles restantes
            sequencesDetectees = a.detectionChainesCycles(parametres.get("largeurMaxArbreDetection"), parametres.get("profondeurMaxArbreDetection"));
            System.out.println("<===>");

            selecteur = new Selecteur(sequencesDetectees);
            sequencesChoisies = new SequencesPossibles();
            //Le selecteur est positionné sur la 1ère Chaine ou le 1er cycle en fonction des séquences détectées précédemment
            //Si rien n'a été détecté, retourne la solution courante
            if (instance.getNbAltruistes() > 0 && !sequencesDetectees.getChaines().isEmpty())
                it = selecteur.getSequencesPossibles().getChaines().iterator();
            else if (instance.getNbPaires() > 0 && !sequencesDetectees.getChaines().isEmpty())
                it = selecteur.getSequencesPossibles().getCycles().iterator();
            else
                return s;
            seq = (Sequence) it.next();
            //Lancement de la sélection des séquences
            sequencesChoisies = selecteur.arbreBestSol(seq, instance, parametres.get("profondeurMaxArbreSelection"), parametres.get("largeurMaxArbreSelection"));

            //Ajout des séquences à la solution
            s.ajouterSequencesSelectionnees(sequencesChoisies);
            System.out.println("_________________________________________________________________");
        }

        return s;
    }

    /**
     * Méthode statique de paramétrage des arbres pour la détection et extraction de séquences.
     * Pour différentes tranches de taille d'instances, setup la profondeur et largeur de l'arbre de
     * détection des séquences, puis la profondeur et largeur de l'arbre de sélection des séquences.
     * @param i Instance à résoudre.
     * @return La HashMap contenant les paramètres 'profondeurMaxArbreSelection', 'largeurMaxArbreDetection',
     *          'profondeurMaxArbreDetection' et 'largeurMaxArbreSelection', avec leurs valeurs respectives.
     * */
    private static HashMap setUpParameters(Instance i) {
        //System.out.println("aaah : " + nomInstance);
        HashMap<String, Integer> parametres = new HashMap<>();

        if (i.getNbPaires() > 0 && i.getNbPaires() < 25) {              //]0 et 25[
            parametres.put("profondeurMaxArbreSelection", 40);
            parametres.put("largeurMaxArbreDetection", 10);
            parametres.put("profondeurMaxArbreDetection", 30);
            parametres.put("largeurMaxArbreSelection", 30);
        }else if (i.getNbPaires() >= 25 && i.getNbPaires() <= 50) {     //[25 et 50]
            parametres.put("profondeurMaxArbreSelection", 15);
            parametres.put("largeurMaxArbreDetection", 12);
            parametres.put("profondeurMaxArbreDetection", 15);
            parametres.put("largeurMaxArbreSelection", 15);
        }else if (i.getNbPaires() > 50 && i.getNbPaires() <= 100) {     //]50 100]
            parametres.put("profondeurMaxArbreSelection", 9);
            parametres.put("largeurMaxArbreDetection", 10);
            parametres.put("profondeurMaxArbreDetection", 4);
            parametres.put("largeurMaxArbreSelection", 4);
        }else if (i.getNbPaires() > 100 && i.getNbPaires() <= 250) {    //]100 et 250]
            if (i.getNbAltruistes() >= 28) {
                parametres.put("profondeurMaxArbreSelection", 7);
                parametres.put("largeurMaxArbreDetection", 2);
                parametres.put("profondeurMaxArbreDetection", 10);
                parametres.put("largeurMaxArbreSelection", 2);
            }else {
                parametres.put("profondeurMaxArbreSelection", 7);
                parametres.put("largeurMaxArbreDetection", 10);
                parametres.put("profondeurMaxArbreDetection", 4);
                parametres.put("largeurMaxArbreSelection", 4);
            }
        }else if (i.getNbPaires() > 250) {                              // plus de 250
            parametres.put("profondeurMaxArbreSelection", 7);
            parametres.put("largeurMaxArbreDetection", 10);
            parametres.put("profondeurMaxArbreDetection", 4);
            parametres.put("largeurMaxArbreSelection", 4);
        }

        return parametres;
    }




    public static void main(String[] args) {
        InstanceReader reader = null;
        long startTime = System.nanoTime();
        try {
            reader = new InstanceReader("instances/KEP_p250_n28_k3_l4.txt");
            Instance i = reader.readInstance();
            ArbreSequences ra = new ArbreSequences();
            System.out.println("================= SEQUENCE "+ i.getNom() +" =================");

            Solution sol = ra.solve(i);
            //new InterfaceWeb(sol).createHtmlFile();
            System.out.println(sol);
            System.out.println("Checker : " + sol.check());

            long endTime = System.nanoTime();
            System.out.println("Tps : " + (endTime - startTime)/1000000 + " ms");  //divide by 1000000 to get milliseconds.
        } catch (ReaderException e) {
            e.printStackTrace();
        }

    }
}
