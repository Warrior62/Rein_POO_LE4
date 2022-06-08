package com.rein.solveur;

import com.rein.instance.Instance;
import com.rein.solution.Solution;

public class BestSolution implements Solveur{
    @Override
    public String getNom() {
        return "Best Solution";
    }

    @Override
    public Solution solve(Instance instance) {

        StrategieBasique sb = new StrategieBasique();
        Solution s1 = sb.solve(instance);

        StrategieBasique2 sb2 = new StrategieBasique2();
        Solution s2 = sb2.solve(instance);

        RechercheArbre ra = new RechercheArbre();
        Solution s3 = ra.solve(instance);

        if( s3.getBenefMedicalTotal()>=s2.getBenefMedicalTotal() && s3.getBenefMedicalTotal()>= s1.getBenefMedicalTotal()){
            return s3;
        }
        else if(s2.getBenefMedicalTotal()>=s1.getBenefMedicalTotal()){
            return s2;
        }
        return s1;
    }
}
