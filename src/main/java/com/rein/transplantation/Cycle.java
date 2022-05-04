/**
 *
 * @author Mathis
 * Project : rein-poo-le4
 * Cycle in transplantation
 * Description : Cycle class
 */

package com.rein.transplantation;

import com.rein.instance.Altruiste;
import com.rein.instance.Noeud;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cycle extends Sequence {

    public Cycle(int tailleMax){

        this.setTailleMaxSequence(tailleMax);

    }

    //--------------- Checker de cycle ---------------
    /**
     * Un cycle est valide si :
     * - Le dernier noeud donne au premier && Le premier reçoit du dernier.
     * - Ne contient pas d'altruiste.
     * - Chaque noeud n'est présent qu'une seule fois.
     * - Le benef medical de la séquence est correctement calculé.
     * - La taille max de la séquence est respectée
     * */
    public boolean check() {
        return (verifTailleMax() && verifNoAltruiste() && verifBenefMedical());
    }


    private boolean verifTailleMax() {
        if (this.getTailleMaxSequence() >= this.getListeNoeuds().size())
            return true;
        else
            return false;
    }

    private boolean verifBenefMedical() {
        List listeNoeuds = this.getListeNoeuds();
        int somme = 0, i;
        Noeud noeudCourant, noeudSuivant;
        //Pour chaque noeud de listeNoeuds, on calcule le benef médical vers le noeud suivant (attention à la fin on reboucle sur le premier noeud)
        for (i=0 ; i<(listeNoeuds.size()-1) ; i++) { //On s'arrête à l'indice 'taille-1', pour manuellement calculer le bénéfice vers le 1er noeud (boucle)
            noeudCourant = (Noeud) listeNoeuds.get(i);
            noeudSuivant = (Noeud) listeNoeuds.get(i+1);
            somme += noeudCourant.getBenefMedicalVers(noeudSuivant);
        }
        i++; //positionnement sur le dernier noeud
        noeudCourant = (Noeud) listeNoeuds.get(i);
        noeudSuivant = (Noeud) listeNoeuds.get(0);
        somme += noeudCourant.getBenefMedicalVers(noeudSuivant);

        return (somme == this.getBenefMedicalSequence());
    }

    private boolean verifNoAltruiste() {
        for (Noeud n: this.getListeNoeuds()) {
            if (n instanceof Altruiste)
                return false;
        }
        return true;
    }
    //-------------------------------------------------




}
