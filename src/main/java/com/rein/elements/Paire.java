package com.rein.elements;

import java.util.List;

public class Paire extends Noeud {

    List<Echange> echanges; //Liste de 2 échanges (échange IN, échange OUT)
    Situation situation;
    
    public Paire(int id) {
        super(id);
    }
    
    
}
