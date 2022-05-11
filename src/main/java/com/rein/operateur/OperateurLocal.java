package com.rein.operateur;

import com.rein.transplantation.Sequence;

public abstract class OperateurLocal extends Operateur {

    public OperateurLocal() {
        super();
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

    public static OperateurInterSequence getOperateurInter(TypeOperateurLocal type, Sequence sequence, Sequence autreSequence){
        switch(type){
            case INTER_DEPLACEMENT:
                //return new InterDeplacement(sequence);
            case INTER_ECHANGE:
                //return new InterEchange(sequence);
            default:
                return null;
        }
    }
}
