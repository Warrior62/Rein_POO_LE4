package com.rein.solveur;
import com.rein.instance.Altruiste;
import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.instance.Paire;
import com.rein.interface_web.InterfaceWeb;
import com.rein.io.InstanceReader;
import com.rein.solution.Chaine;
import com.rein.solution.Solution;
import com.rein.transplantation.Cycle;
import com.rein.transplantation.Sequence;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.ArrayUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class StrategieBasique implements Solveur{
    private Solveur solveur;
    @Override
    public String getNom() {
        return this.solveur.getNom()+"StrategieBasique"; }
    @Override
    public Solution solve(Instance instance) {
        Solution s = new Solution(instance);
        Noeud[] tabTest = instance.getTabNoeud();
        // Insertion des altruistes dans une chaine
        if(instance.getNbAltruistes()>0){
            int i;
            for(i=1;i<=instance.getNbAltruistes();i++){
                Altruiste a = (Altruiste) tabTest[0];
                Chaine ch = new Chaine(instance.getTailleMaxChaines(), a);
                tabTest= ArrayUtils.remove(tabTest,0);
                s.getListeSequences().add(ch);
            }
        }
        System.out.println(s.getListeSequences());
        for(int i=0;i<tabTest.length;i++) {
            boolean coutEffectue= false; // variable qui indique si la cout a ete effectue
            boolean rechercheCycle = true; // recherche d'insertion de la paire dans un cycle
            int meilleurBenef =1; // recherche du N meilleur benefice, par defaut 1 soit le premier meilleur benef
            Paire pRecherche = (Paire) tabTest[0]; // premiere paire du tableau
            System.out.println("PAIRE SELECTIONNEE " + pRecherche);
            while(rechercheCycle){
                Paire paireBest = (Paire) pRecherche.MeilleurNBenefice(meilleurBenef);
                if(paireBest != null){
                    if(paireBest.isPossible(pRecherche) && isPresent(tabTest,paireBest)) {
                        System.out.println("Cycle possible entre "+ pRecherche+ "et"+paireBest );
                        Cycle cy = new Cycle(instance.getTailleMaxCycles());
                        cy.getListeNoeuds().add(pRecherche);
                        cy.getListeNoeuds().add(paireBest);
                        coutEffectue = true;
                        rechercheCycle = false;
                        s.getListeSequences().add(cy);
                        //suppression de la paire associée dans le tableau de recherche
                        // (on ne veut pas qu'elle recherche
                        // vu qu'elle est insérée dans le cycle avec notre paire de recherche)
                        // on supprime a la fin de la boucle for l'autre paire
                        int index = recherchePlace(tabTest,paireBest);
                        if (index>-1) {
                            tabTest = ArrayUtils.remove(tabTest, index);
                        }
                    }
                    else{
                        //System.out.println("pas possible car pas de bénéfice avec l'autre paire");
                    }
                    meilleurBenef +=1;
                }
                else{
                    //si la paire est null c'est qu'il n'y a plus de solutions de cycle
                    // donc on sort de la boucle de recherche
                    rechercheCycle = false;
                }
            }
            //recherche insertion de la paire dans une chaine si elle n'a pas ete inseree
            if (!coutEffectue && s.getListeSequences().size()>0){
                System.out.println("Recherche Insertion Chaîne");
                boolean rechercheChaine = true;
                for (Sequence seq : s.getListeSequences()){
                    if (seq instanceof Chaine && rechercheChaine && seq.getListeNoeuds().size()>0){
                        Noeud nLast = seq.getListeNoeuds().get(seq.getListeNoeuds().size()-1);
                        if (nLast.isPossible(pRecherche)){
                            seq.getListeNoeuds().add(pRecherche);
                            rechercheChaine = false;
                            coutEffectue = true;
                        }
                    }
                }
            }
            // on supprime la paire du tableau de recherche (même si elle n'a pas été insérée)
            tabTest= ArrayUtils.remove(tabTest,0);
            if (tabTest.length==0){
                System.out.println("tabTestVide");
                s.calculBenefice();
                return s;
            }
        }
        //calcul du bénéfice total
        s.calculBenefice();
        return s;
    }
    // Fonction pour savoir si le noeud est présent dans le tableau des paires pas encore insérées dans la solution
    public boolean isPresent(Noeud[] tab, Noeud n){
        for(int i=0;i<tab.length;i++){
            if(tab[i].getId()==n.getId()){
                return true;
            }
        }
        return false;
    }
    //Fonction qui renvoie la place du noeud dans le tableau de recherche
    public int recherchePlace(Noeud[] tab, Noeud n){
        for(int i=0;i<tab.length;i++){
            if(tab[i].getId()==n.getId()){
                return i;
            }
        }
        return -1;
    }


    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        // Création des options
        Options options = new Options();
        options.addOption(Option.builder("inst")
                .hasArg(true)
                .valueSeparator(' ')
                .desc("Nom du fichier d'instance")
                .build());
        options.addOption(Option.builder("dSol")
                .hasArg(true)
                .valueSeparator(' ')
                .desc("Répertoire des fichiers solutions")
                .build());
        try {
            // Lecture des arguments CLI
            CommandLine line = parser.parse(options, args);
            System.out.println(line.getOptionValue("inst"));
            System.out.println(line.getOptionValue("dSol"));
            InstanceReader reader;
            try {
                // Lecture du fichier d'instance
                reader = new InstanceReader("instancesInitiales/" + line.getOptionValue("inst"));
                Instance i = reader.readInstance();
                // Résolution de l'instance
                StrategieBasique sb = new StrategieBasique();
                Solution s1 = sb.solve(i);
                System.out.println(s1);
                try {
                    // Création du fichier de solution
                    String nomFicSol = line.getOptionValue("dSol") + "/" + line.getOptionValue("inst").split("\\.")[0] + "_sol.txt";
                    File ficSol = new File(nomFicSol);
                    ficSol.getParentFile().mkdirs();
                    // Ecriture du fichier de solution
                    FileWriter myWriter = new FileWriter(ficSol);
                    myWriter.write(s1.exportSol());
                    myWriter.close();
                    System.out.println("Fichier de solution créé : " + nomFicSol);
                } catch (IOException e) {
                    System.err.println("ERROR fichier solution");
                    e.printStackTrace();
                }
                //System.out.println("s1 : " + s1.toString() + "\n\tcheck : " + s1.check());
                System.out.println("Checker : " + s1.check());
                InterfaceWeb interfaceWeb = new InterfaceWeb(s1);
                interfaceWeb.createHtmlFile();
            } catch(Exception e){
                System.out.println(e.getMessage());
                System.err.println("ERROR Strategie basique");
            }
        } catch (ParseException exp) {
            System.err.println("Unexpected exception:" + exp.getMessage());
        }
    }
}
