package com.rein.operateur;

import com.rein.instance.Noeud;
import com.rein.transplantation.Sequence;

public abstract class OperateurLocal extends Operateur {
    protected Noeud noeudI, noeudJ;
    protected final int positionI, positionJ;

    public OperateurLocal() {
        super();
        this.positionI = -1;
        this.positionJ = -1;
    }

    public OperateurLocal(Sequence sequence, int positionI, int positionJ) {
        super(sequence);
        this.positionI = positionI;
        this.positionJ = positionJ;
        this.noeudI = sequence.getNoeud(positionI);
        this.noeudJ = sequence.getNoeud(positionJ);
    }

    public Noeud getNoeudI() {
        return noeudI;
    }

    public Noeud getNoeudJ() {
        return noeudJ;
    }

    public static OperateurLocal getOperateur(TypeOperateurLocal type){
        switch(type){
            case INTRA_DEPLACEMENT:
                return new IntraDeplacement();
            case INTRA_ECHANGE:
                //return new IntraEchange();
            case INTER_DEPLACEMENT:
                return new InterDeplacement();
            case INTER_ECHANGE:
                //return new InterEchange();
            default:
                return null;
        }
    }

    public static OperateurIntraSequence getOperateurIntra(TypeOperateurLocal type, Sequence sequence){
        switch(type){
            case INTRA_DEPLACEMENT:
                return new IntraDeplacement();
            case INTRA_ECHANGE:
                //return new IntraEchange();
            default:
                return null;
        }
    }

    public static OperateurInterSequence getOperateurInter(TypeOperateurLocal type, Sequence sequence, Sequence autreSequence, int positionI, int positionJ){
        switch(type){
            case INTER_DEPLACEMENT:
                return new InterDeplacement(sequence, autreSequence, positionI, positionJ);
            case INTER_ECHANGE:
                //return new InterEchange(sequence);
            default:
                return null;
        }
    }
}
