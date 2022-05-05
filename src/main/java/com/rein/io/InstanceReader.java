package com.rein.io;


import com.rein.instance.*;
import com.rein.io.exception.FileExistException;
import com.rein.io.exception.FormatFileException;
import com.rein.io.exception.OpenFileException;
import com.rein.io.exception.ReaderException;
import org.apache.commons.cli.*;
import java.io.*;



/**
 *
 * @author MartinFrmx
 * 1 avr. 2022
 * Project : rein-poo-le4
 * InstanceReader in io
 * Description : Instance reader class
 */

public class InstanceReader {
    /**
     * Le fichier contenant l'instance.
     */
    private File instanceFile;


    /**
     * Constructeur par donnee du chemin du fichier d'instance.
     * @param inputPath le chemin du fichier d'instance, qui doit se terminer 
     * par l'extension du fichier (.xml).
     * @throws ReaderException lorsque le fichier n'est pas au bon format ou 
     * ne peut pas etre ouvert.
     */
    public InstanceReader(String inputPath) throws ReaderException {
        if (inputPath == null) {
            throw new OpenFileException();
        }
        if (!inputPath.endsWith(".txt")) {
            throw new FormatFileException("txt", "txt");
        }
        String instanceName = inputPath;
        this.instanceFile = new File(instanceName);
    }



    /**
     * Methode principale pour lire le fichier d'instance.
     * @return l'instance lue
     * @throws ReaderException lorsque les donnees dans le fichier d'instance 
     * sont manquantes ou au mauvais format.
     */
    public Instance readInstance() throws ReaderException {
        try {
            String nom = this.instanceFile.getName();
            FileReader f = new FileReader(this.instanceFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(f);
            int nbPaires = Integer.parseInt(lire(br));
            int nbAltruistes = Integer.parseInt(lire(br));
            int tailleMaxCycles = Integer.parseInt(lire(br));
            int tailleMaxChaines = Integer.parseInt(lire(br));
            Instance instance = new Instance(nom, nbPaires, nbAltruistes, tailleMaxCycles, tailleMaxChaines, new Noeud[nbPaires+nbAltruistes]);

            int count = 0;
            while(count < (nbPaires+nbAltruistes)){
                if (count < instance.getNbAltruistes()) {
                    instance.addAltruiste(count);
                } else {
                    instance.addPaire(count);
                }
                count++;
            }
            count=0;
            while(count < (nbPaires+nbAltruistes)){
                String noeud = lireNoeud(br, instance, count);
                if(!"".equals(noeud))
                    count++;
            }
            br.close();
            f.close();
            return instance;
        } catch (FileNotFoundException ex) {
            throw new FileExistException(instanceFile.getName());
        } catch (IOException ex) {
            throw new ReaderException("IO exception", ex.getMessage());
        }
    }



    /**
     * Lecture des infos principales de l'instance.
     * @param br lecteur courant du fichier d'instance
     * @return le nom de l'instance
     * @throws IOException 
     */
    private String lire(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        while(!ligne.matches("^\\d+$")) {
            ligne = br.readLine();
        }
        ligne = ligne.trim();
        return ligne;
    }
    
    
    /**
     * Lecture des infos principales de l'instance.
     * @param br lecteur courant du fichier d'instance
     * @return le Noeud avec 
     * @throws IOException 
     */
    private String lireNoeud(BufferedReader br, Instance instance, int count) throws IOException {
        String ligne = br.readLine();
        while(!ligne.matches("^(-?\\d+\\t)+(-?\\d+)?$")) {
            ligne = br.readLine();
        }
        if(ligne!="") {
            //On ajoute les echanges
            int i = 0;
            String[] ligneNoeud = ligne.split("\t");
            for(String noeud : ligneNoeud) {
                int benefMedical = Integer.parseInt(noeud);
                if(benefMedical != -1) {
                    instance.getEchanges().add(new Echange(benefMedical,instance.getTabNoeud()[count], (Paire) instance.getTabNoeud()[i+instance.getNbAltruistes()]));
                }
                i++;
            }
            
        }
        return ligne;
    }


    /**
     * Test de lecture d'une instance.
     * @param args 
     */
    public static void main(String[] args) {
        try {
            CommandLineParser parser = new DefaultParser();
            // create the Options
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

                InstanceReader reader = new InstanceReader("instancesInitiales/" + line.getOptionValue("inst"));
                Instance i = reader.readInstance();
                System.out.print(i.toString());
            } catch (ParseException exp) {
                System.out.println("Unexpected exception:" + exp.getMessage());
            }
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
