package com.rein.instance;

import java.util.HashMap;
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

    /**
     * Clone un objet de type Map
     * @param original à partir duquel il faut obtenir un clone
     * @return la copie de l'objet Map
     */
    public Map<Noeud, Integer> clone(Map<Noeud, Integer> original){
        Map<Noeud, Integer> copy = new HashMap<>();
        copy.putAll(original);
        return copy;
    }

    /**
     * Récupère le N meilleur bénéfice
     * @param numero N
     * @return le noeud avec le meilleur N bénéfice
     */
    public Noeud getNoeudMeilleurNBenefice(int numero){
        Map<Noeud, Integer> listeEchangesTest = clone(listeEchanges);
        Noeud noeud = null;

        if(numero <= listeEchanges.size()) {
            for (int j = 0; j < numero; j++) {
                Map.Entry<Noeud, Integer> entry = listeEchangesTest.entrySet().stream()
                        .max((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue())).get();
                listeEchangesTest.remove(entry.getKey());
                noeud = entry.getKey();
            }
        }
        return noeud;
    }

    /**
     * Récupère le meilleur bénéfice d'un échange avec
     * le noeud this
     * @return le noeud avec lequel le bénéfice est le meilleur
     */
    public Noeud getMeilleurBenefice(){
        Map.Entry<Noeud, Integer> entry = listeEchanges.entrySet().stream()
                .max((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue())).get();
        //System.out.println("L'entree ayant la valeur la plus elevee est (key=" + entry.getKey() + ", value=" + entry.getValue() + ")");
        return entry.getKey();
    }

    public boolean isPossible(Noeud n){
        boolean possible = this.listeEchanges.containsKey(n);
        return possible;
    }

    // Fonction pour savoir si le noeud est présent dans le tableau des paires pas encore insérées dans la solution
    public boolean isPresent(Noeud[] tab){
        for(int i=0;i<tab.length;i++){
            if(tab[i].getId()==this.getId()){
                return true;
            }
        }
        return false;
    }
    //Fonction qui renvoie la place du noeud dans le tableau de recherche
    public int recherchePlace(Noeud[] tab){
        for(int i=0;i<tab.length;i++){
            if(tab[i].getId()==this.getId()){
                return i;
            }
        }
        return -1;
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
