package com.rein.solveur;

import com.rein.instance.Instance;
import com.rein.operateur.InsertionNoeud;
import com.rein.operateur.OperateurLocal;
import com.rein.operateur.TypeOperateurLocal;
import com.rein.solution.Solution;

public class RechercheLocale implements Solveur {

    private Solveur solveur;

    public RechercheLocale() {
        //this.solveur = new ;
    }

    @Override
    public String getNom() {
        return this.solveur.getNom() + " : RechercheLocale";
    }

    @Override
    public Solution solve(Instance instance) {
        Solution s = this.solveur.solve(instance);
        boolean improve = true;
        while(improve){
            improve = false;
            OperateurLocal best = s.getMeilleurOperateurLocal(TypeOperateurLocal.INTER_DEPLACEMENT);
            if(best.isMouvementAmeliorant()){
                s.doMouvementRechercheLocale(best);
                improve = true;
            }
        }
        return s;
    }
}
