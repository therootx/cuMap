package com.ahmet.showmustgoon;

import android.support.v7.app.AppCompatActivity;


public class AccessPoints extends AppCompatActivity {
    static String accessPoints[][] = new String[36][4];
    static double accessP[][]=new double[36][2];

    public double[][] AP(){
        for (int i = 0; i < accessPoints.length; i++) {
            for (int j = 1; j < accessPoints[i].length; j++) {
                accessP[i][j-1]=Double.parseDouble(accessPoints[i][j]);
            }
        }
        return accessP;
    }
    public String [][] getAccessPoints(){
        return accessPoints;
    }
}
