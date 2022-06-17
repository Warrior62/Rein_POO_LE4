package com.rein.instance;


public class Echange {

    private int benefMedical;
    private Noeud donneur;
    private Noeud receveur;
    private boolean isRealise;

    public Echange(int benefMedical, Noeud donneur, Noeud receveur) {
        this.benefMedical = benefMedical;
        this.donneur = donneur;
        this.receveur = receveur;
        this.isRealise = false;
        this.donneur.getListeEchanges().put(this.receveur, this.benefMedical);
    }

    public int getBenefMedical() {
        return benefMedical;
    }

    public Noeud getDonneur() {
        return donneur;
    }

    public Noeud getReceveur() {
        return receveur;
    }

    @Override
    public String toString() {
        return "\n Echange{" +
                "benefMedical=" + benefMedical +
                ", \t donneur=" + donneur +
                ", \t receveur=" + receveur +
                ", \t isRealise=" + isRealise +
                '}';
    }


}
