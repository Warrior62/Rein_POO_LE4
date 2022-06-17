package com.rein.solveur;

import com.rein.instance.Instance;
import com.rein.solution.Solution;

import java.io.IOException;

public interface Solveur {
    public String getNom();
    public Solution solve(Instance instance) throws IOException;
}
