package com.rein.instance;

import com.rein.transplantation.Sequence;

public class Echange {

    private int benefMedical;
    private Sequence sequence;
    private Noeud donneur;
    private Paire receveur;

    public int getBenefMedical() {
        return benefMedical;
    }

    public void setBenefMedical(int benefMedical) {
        this.benefMedical = benefMedical;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
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
}
