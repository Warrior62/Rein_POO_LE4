package com.rein.instance;

import com.rein.solution.Arbre;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class Noeud implements Comparable {
    private final int id;
    private Map<Noeud, Integer> listeEchanges;
    private int scorePopularite;
    private int scoreBenefice;

    public Noeud(int id) {
        this.id = id;
        this.listeEchanges = new HashMap<>();
        this.scorePopularite = 0;
        this.scoreBenefice = 0;
    }

    /**
     * Constructeur par copie de Noeud
     * @param n Noeud à copier.
     * @return La copie du Noeud n.
     * */
    public Noeud(Noeud n) {
        this.id = n.getId();
        this.listeEchanges = n.getListeEchanges();
        this.scorePopularite = n.getScorePopularite();
        this.scoreBenefice = n.getScoreBenefice();
    }

    public int getId() {
        return id;
    }

    public Map<Noeud, Integer> getListeEchanges() { return listeEchanges; }

    public int getScoreBenefice() {
        return scoreBenefice;
    }

    public int getScorePopularite() {
        return scorePopularite;
    }

    public void setListeEchanges(Map<Noeud, Integer> listeEchanges) {
        this.listeEchanges = listeEchanges;
    }

    public void setScoreBenefice(int scoreBenefice) {
        this.scoreBenefice = scoreBenefice;
    }

    public void setScorePopularite(int scorePopularite) {
        this.scorePopularite = scorePopularite;
    }

    //FONCTION POUR CLONER UN TABLEAU MAP
    public Map<Noeud, Integer> clone(Map<Noeud, Integer> original){
        Map<Noeud, Integer> copy = new HashMap<>();
        copy.putAll(original);
        return copy;
    }

    public boolean isPresent(HashSet<Integer> set){
        if(set.contains(this.getId()))
            return true;
        return false;
    }


    //FONCTION POUR RECUPERER LE N MEILLEUR BENEFICE
    public Noeud MeilleurNBenefice(int numero){
        Map<Noeud, Integer> listeEchangesTest = clone(listeEchanges);
        Noeud noeud = null;

        if(numero <= listeEchanges.size()) {
            for (int j = 0; j < numero; j++) {
                Map.Entry<Noeud, Integer> entry = listeEchangesTest.entrySet().stream()
                        .max((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue())).get();

                //System.out.println("L'entree avec la valeur la plus elevee est (key=" + entry.getKey() + ", value=" + entry.getValue() + ")");
                listeEchangesTest.remove(entry.getKey());
                noeud = entry.getKey();
            }
        }
        return noeud;

    }

    //FONCTION POUR RECUPERER LE MEILLEUR BENEFICE
    public Noeud MeilleurBenefice(){
        Map.Entry<Noeud, Integer> entry = listeEchanges.entrySet().stream()
                .max((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue())).get();
        //System.out.println("L'entree ayant la valeur la plus elevee est (key=" + entry.getKey() + ", value=" + entry.getValue() + ")");
        return entry.getKey();
    }

    public boolean isPossible(Noeud n){
        boolean possible = this.listeEchanges.containsKey(n);
        return possible;
    }

    /**
     * Méthode renvoyant le bénéfice médical du Noeud courant vers le Noeud n.
     * Si le noeud est incompatible avec le noeud n, renvoie -1.
     * @param n Noeud vers lequel le benef medical est calculé.
     * @return int benef du Noeud courant vers le Noeud n.
     * */
    public int getBenefMedicalVers(Noeud n) {
        if (this.listeEchanges.get(n) == null)
            return -1;
        else {
            int benef = this.listeEchanges.get(n);
            //System.out.println(benef);
            return benef;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Noeud)) return false;
        Noeud noeud = (Noeud) o;
        return id == noeud.id && Objects.equals(listeEchanges, noeud.listeEchanges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String s = "Noeud{" +
                "id=" + id;
            s += "}";
        return s;
    }

    @Override
    public int compareTo(Object o) {
        Noeud n = (Noeud) o;
        return n.getScoreBenefice() - this.getScoreBenefice();
    }
}
