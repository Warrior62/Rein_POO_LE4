package com.rein.instance;

import com.rein.transplantation.Sequence;

import java.util.List;
import java.util.Map;

public class Paire extends Noeud {

    public Paire(int id) {
        super(id);
    }


    @Override
    public String toString() {
        return "Paire{id=" + this.getId()+"}";
    }
}
