package com.rein.solution;

import com.rein.instance.Echange;
import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.io.InstanceReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class Arbre {

    private Noeud noeudRacine;
    private ArrayList<Arbre> listeFils;
    static int cpt = 0;
    static final int PROFONDEUR_MAX = 10;

    public Arbre(Noeud noeudRacine) {
        this.noeudRacine = noeudRacine;
        this.listeFils = new ArrayList<>();
    }

    public Arbre(Noeud noeudRacine, ArrayList<Arbre> listeFils) {
        this(noeudRacine);
        this.listeFils = listeFils;
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

    public static void creerArbre(Arbre arbre, HashSet<Integer> setId){
        cpt += 1;
        if(cpt < PROFONDEUR_MAX){
            Arbre a = new Arbre(arbre);
            a.remplirListeFils();
            for(Arbre fils : a.getListeFils())
                System.out.println("\tracine=" + a.noeudRacine.getId() + " -> " + fils.noeudRacine.getId());
            System.out.println("\n");
            if(!a.noeudRacine.isPresent(setId)){
                for(Arbre fils : a.getListeFils()){
                    setId.add(fils.noeudRacine.getId());
                    creerArbre(fils, setId);
                }
            }
            /*for(Arbre fils : a.getListeFils())
                creerArbre(fils);*/
        }
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
            this.listeFils.add(a);
        }
    }

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
            InstanceReader reader = new InstanceReader("instancesInitiales/KEP_p9_n1_k3_l3.txt");
            Instance i = reader.readInstance();
            Arbre a = new Arbre(i.getTabNoeud()[0]);
            HashSet<Integer> setId = new HashSet<>();
            creerArbre(a, setId);
            System.out.println(a);
            for(Arbre fils : a.getListeFils())
                System.out.println("\t" + fils);
        } catch(Exception e){
            System.err.println(e);
        }
    }
}
