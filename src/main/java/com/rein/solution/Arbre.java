package com.rein.solution;

import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.io.InstanceReader;

import java.util.*;

public class Arbre {

    private int id;
    private Noeud noeudRacine;
    private ArrayList<Arbre> listeFils;
    private int niveauProfondeur;
    static final int PROFONDEUR_MAX = 5;
    static ArrayList<HashSet<Integer>> listeChainesPossibles = new ArrayList<HashSet<Integer>>();
    static ArrayList<HashSet<Integer>> listeCyclesPossibles = new ArrayList<HashSet<Integer>>();

    // --------------------------------------------------
    // --------------------------------------------------

    public Arbre(Noeud noeudRacine) {
        this.id = noeudRacine.getId();
        this.noeudRacine = noeudRacine;
        this.listeFils = new ArrayList<>();
        this.niveauProfondeur = 0;
    }

    public Arbre(Noeud noeudRacine, ArrayList<Arbre> listeFils) {
        this(noeudRacine);
        this.listeFils = listeFils;
    }

    public int getId() {
        return id;
    }

    public Arbre(Arbre arbre) {
        this.noeudRacine = arbre.noeudRacine;
        this.listeFils = arbre.listeFils;
    }

    public Noeud getNoeudRacine() {
        return noeudRacine;
    }

    public ArrayList<Arbre> getListeFils() {
        return listeFils;
    }

    public void setNoeudRacine(Noeud noeudRacine) {
        this.noeudRacine = noeudRacine;
    }



    // -- Methode chargée d'entamer la création d'un arbre (1er level)
    // Fait ensuite appel de façon récursive à
    public static void recurrArbre(Arbre arbre, LinkedHashSet<Integer> listeId, int profondeur){
        //this.niveauProfondeur += 1;
        profondeur++;
        LinkedHashSet<Integer> listeIdBis = new LinkedHashSet<>(listeId);
        System.out.println("On est sur : " + arbre.getId());
        if (listeIdBis.add(arbre.getId())) {
            if(profondeur < PROFONDEUR_MAX){
                arbre.remplirListeFils();       //Récupération de ses fils
                for(Arbre fils : arbre.getListeFils()) {
                    //System.out.println("\tracine=" + a.noeudRacine.getId() + " -> " + fils.noeudRacine.getId());
                    //System.out.println("\n");"";
                    System.out.println("On descend sur : " + fils.getId());
                    recurrArbre(fils, listeIdBis, profondeur);
                    System.out.println("On remonte...");
                }
            }else {
                System.out.println("Detection d'une chaine !!! ---> " + listeIdBis);
                listeChainesPossibles.add(listeId);
            }
        }else {
            Iterator it = listeIdBis.iterator();
            System.out.println("Detection d'un cycle !!! ---> " + listeIdBis);
            int idCourant = 0;
            while (idCourant != arbre.getId()) {
                //System.out.println("aaaa");
                idCourant = (int) it.next();
            }
                listeIdBis.remove(idCourant);
            System.out.println("Detection d'un cycle !!! ---> " + listeIdBis);
            listeCyclesPossibles.add(listeId);
        }
    }

    // -- Méthode récursive de génération de l'arbre
    // appelée par creerArbre()
    public void genArbreRecursif(Arbre a) {

    }





    public int taille(Arbre a){
        if(a == null) return 0;
        int tailleFils = 0;
        for(Arbre fils : this.listeFils)
            tailleFils += taille(fils);
        return 1 + tailleFils;
    }

    public void remplirListeFils(){
        for(Map.Entry echange : this.noeudRacine.getListeEchanges().entrySet()){
            Noeud noeudFils = (Noeud) echange.getKey();
            Arbre a = new Arbre(noeudFils);
            a.niveauProfondeur = this.niveauProfondeur + 1;
            this.listeFils.add(a);
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------

    @Override
    public String toString() {
        String listeFils = "";
        for(Arbre fils : this.listeFils)
            listeFils += fils.getNoeudRacine().getId() + " ";

        return "\nArbre{" +
                "noeudRacine=" + noeudRacine.getId() +
                ", listeFils=[" + listeFils + "]}";
    }


    public static void main(String[] args) {
        try{
            InstanceReader reader = new InstanceReader("instancesInitiales/KEP_p100_n11_k5_l17.txt");
            Instance i = reader.readInstance();
            Arbre a = new Arbre(i.getTabNoeud()[0]);
            LinkedHashSet<Integer> listeId = new LinkedHashSet<Integer>();
            recurrArbre(a, listeId, 0);
            System.out.println(a);
            /*for(Arbre fils : a.getListeFils())
                System.out.println("\t" + fils);*/
            System.out.println(i.getEchanges());
            System.out.println("id : " + a.getId());
            System.out.println("On est bons");
            System.out.println("listeCyclesPossibles : ");
            System.out.println(listeCyclesPossibles);
            System.out.println("listeChainesPossibles : ");
            System.out.println(listeChainesPossibles);
        } catch(Exception e){
            System.err.println(e);
        }
    }
}
