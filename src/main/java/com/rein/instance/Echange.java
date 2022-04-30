package com.rein.instance;

import com.rein.transplantation.Sequence;

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
        //System.out.println(this.donneur.getListeEchanges());
    }

    public int getBenefMedical() {
        return benefMedical;
    }

    private void setBenefMedical(int benefMedical) {
        this.benefMedical = benefMedical;
    }

    public Noeud getDonneur() {
        return donneur;
    }

    private void setDonneur(Noeud donneur) {
        this.donneur = donneur;
    }

    public Noeud getReceveur() {
        return receveur;
    }

    private void setReceveur(Noeud receveur) {
        this.receveur = receveur;
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
