package com.rein.solveur;

import com.rein.instance.Instance;
import com.rein.solution.Solution;

public interface Solveur {
    public String getNom();
    public Solution solve(Instance instance);
}
