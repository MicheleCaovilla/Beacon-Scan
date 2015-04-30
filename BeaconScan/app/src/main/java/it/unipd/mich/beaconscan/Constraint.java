package it.unipd.mich.beaconscan;

public class Constraint implements Constraints {
    /*	double round2(double num, int precision)
     {
         double rnum;
         int tnum;

         rnum = num*Math.pow(10,precision);
         tnum = (int)(rnum < 0 ? rnum-0.5 : rnum + 0.5);
         rnum = tnum/Math.pow(10,precision);

         return rnum;
     }*/
    public void getConstrainedValues(double x[], int n) {
        // round to 2 decimal places

        if (x[2] < 0) {
            x[2] = -x[2]; //if z negative, turn to positive :) lol
        }
  /*
   int i;
   for (i=0; i<n; i++) {
     x[i] = round2(x[i],2);
   }*/
    }
}