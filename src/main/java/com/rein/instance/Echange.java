package com.rein.instance;

import com.rein.transplantation.Sequence;

public class Echange {

    private int benefMedical;
    private Noeud donneur;
    private Paire receveur;
    private boolean isRealise;

    public Echange(int benefMedical, Noeud donneur, Paire receveur) {
        this.benefMedical = benefMedical;
        this.donneur = donneur;
        this.receveur = receveur;
        this.sequence = null;
        this.isRealise = false;
    }

    public int getBenefMedical() {
        return benefMedical;
    }

    public void setBenefMedical(int benefMedical) {
        this.benefMedical = benefMedical;
    }

    public Noeud getDonneur() {
        return donneur;
    }

    public void setDonneur(Noeud donneur) {
        this.donneur = donneur;
    }

    public Paire getReceveur() {
        return receveur;
    }

    public void setReceveur(Paire receveur) {
        this.receveur = receveur;
    }

    @Override
    public String toString() {
        return "Echange{" +
                "benefMedical=" + benefMedical +
                ", sequence=" + sequence +
                ", donneur=" + donneur +
                ", receveur=" + receveur +
                ", isRealise=" + isRealise +
                '}';
    }
}
