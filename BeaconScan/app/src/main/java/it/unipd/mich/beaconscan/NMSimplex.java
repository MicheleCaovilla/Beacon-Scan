package it.unipd.mich.beaconscan;

/**
 * nmsimplex.java
 *
 * @author Michael Hutt
 * @date 2011-03-15
 * www.mikehutt.com
 * <p/>
 * An implementation of the Nelder-Mead simplex method.
 * <p/>
 * Copyright (c) 1997-2011 <Michael F. Hutt>
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

interface Constraints {
    public void getConstrainedValues(double x[], int n);
}


interface Objfun {
    public abstract double evalObjfun(double x[]);
}


public class NMSimplex {
    static final int MAX_IT = 1000;      /* maximum number of iterations */
    static final double ALPHA = 1.0;       /* reflection coefficient */
    static final double BETA = 0.5;       /* contraction coefficient */
    static final double GAMMA = 2.0;       /* expansion coefficient */

    public NMSimplex(double start[], int n, double EPSILON, double scale, Objfun objf, Constraints c) {
        //c = new Constraint();
        //objf = new Objfunc();


        double v[][] = new double[n + 1][n];
        double f[] = new double[n + 1];
        double vr[] = new double[n];
        double ve[] = new double[n];
        double vc[] = new double[n];
        double vm[] = new double[n];
        double fr;      /* value of function at reflection point */
        double fe;      /* value of function at expansion point */
        double fc;      /* value of function at contraction point */
        double pn, qn;   /* values used to create initial simplex */
        double fsum, favg, s, cent;
        int vs;         /* vertex with smallest value */
        int vh;         /* vertex with next smallest value */
        int vg;         /* vertex with largest value */
        int i, j, m, row = 0;
        int k;   	      /* track the number of function evaluations */
        int itr;	      /* track the number of iterations */

    /* create the initial simplex */
    /* assume one of the vertices is 0,0 */

        System.out.format("Starting from : %f\n", start[0]);

        pn = scale * (Math.sqrt(n + 1) - 1 + n) / (n * Math.sqrt(2));
        qn = scale * (Math.sqrt(n + 1) - 1) / (n * Math.sqrt(2));

        for (i = 0; i < n; i++) {
            v[0][i] = start[i];
        }

        //System.out.format("pn:%f qn:%f\n",pn,qn);

        for (i = 1; i <= n; i++) {
            for (j = 0; j < n; j++) {
                if (i - 1 == j) {
                    v[i][j] = pn + start[j];
                } else {
                    v[i][j] = qn + start[j];
                }
            }
            c.getConstrainedValues(v[i], n);
        }

    /* find the initial function values */
        for (j = 0; j <= n; j++) {
            f[j] = objf.evalObjfun(v[j]);
        }

        k = n + 1;
    
    /* print out the initial values */
        System.out.println("Initial Values");
        for (j = 0; j <= n; j++) {
            for (i = 0; i < n; i++) {
                System.out.format("%f %f\n", v[j][i], f[j]);
            }
        }

  	/* begin the main loop of the minimization */
        for (itr = 1; itr <= MAX_IT; itr++) {
          /* find the index of the largest value */
            vg = 0;
            for (j = 0; j <= n; j++) {
                if (f[j] > f[vg]) {
                    vg = j;
                }
            }

  		/* find the index of the smallest value */
            vs = 0;
            for (j = 0; j <= n; j++) {
                if (f[j] < f[vs]) {
                    vs = j;
                }
            }

  		/* find the index of the second largest value */
            vh = vs;
            for (j = 0; j <= n; j++) {
                if (f[j] > f[vh] && f[j] < f[vg]) {
                    vh = j;
                }
            }

  		/* calculate the centroid */
            for (j = 0; j <= n - 1; j++) {
                cent = 0.0;
                for (m = 0; m <= n; m++) {
                    if (m != vg) {
                        cent += v[m][j];
                    }
                }
                vm[j] = cent / n;
            }

  		/* reflect vg to new vertex vr */
            for (j = 0; j <= n - 1; j++) {
              /*vr[j] = (1+ALPHA)*vm[j] - ALPHA*v[vg][j];*/
                vr[j] = vm[j] + ALPHA * (vm[j] - v[vg][j]);
            }

            c.getConstrainedValues(vr, n);
            fr = objf.evalObjfun(vr);
            k++;

            if (fr < f[vh] && fr >= f[vs]) {
                for (j = 0; j <= n - 1; j++) {
                    v[vg][j] = vr[j];
                }
                f[vg] = fr;
            }

  		/* investigate a step further in this direction */
            if (fr < f[vs]) {
                for (j = 0; j <= n - 1; j++) {
                  /*ve[j] = GAMMA*vr[j] + (1-GAMMA)*vm[j];*/
                    ve[j] = vm[j] + GAMMA * (vr[j] - vm[j]);
                }

                c.getConstrainedValues(ve, n);
                fe = objf.evalObjfun(ve);
                k++;

  			/* by making fe < fr as opposed to fe < f[vs], 			   
                 Rosenbrocks function takes 63 iterations as opposed
  			   to 64 when using double variables. */

                if (fe < fr) {
                    for (j = 0; j <= n - 1; j++) {
                        v[vg][j] = ve[j];
                    }
                    f[vg] = fe;
                } else {
                    for (j = 0; j <= n - 1; j++) {
                        v[vg][j] = vr[j];
                    }
                    f[vg] = fr;
                }
            }

  		/* check to see if a contraction is necessary */
            if (fr >= f[vh]) {
                if (fr < f[vg] && fr >= f[vh]) {
  				/* perform outside contraction */
                    for (j = 0; j <= n - 1; j++) {
  					/*vc[j] = BETA*v[vg][j] + (1-BETA)*vm[j];*/
                        vc[j] = vm[j] + BETA * (vr[j] - vm[j]);
                    }

                    c.getConstrainedValues(vc, n);
                    fc = objf.evalObjfun(vc);
                    k++;
                } else {
  				/* perform inside contraction */
                    for (j = 0; j <= n - 1; j++) {
  					/*vc[j] = BETA*v[vg][j] + (1-BETA)*vm[j];*/
                        vc[j] = vm[j] - BETA * (vm[j] - v[vg][j]);
                    }

                    c.getConstrainedValues(vc, n);
                    fc = objf.evalObjfun(vc);
                    k++;
                }


                if (fc < f[vg]) {
                    for (j = 0; j <= n - 1; j++) {
                        v[vg][j] = vc[j];
                    }
                    f[vg] = fc;
                }
  			/* at this point the contraction is not successful,
  			   we must halve the distance from vs to all the 
  			   vertices of the simplex and then continue.
  			   10/31/97 - modified to account for ALL vertices. 
  			*/
                else {
                    for (row = 0; row <= n; row++) {
                        if (row != vs) {
                            for (j = 0; j <= n - 1; j++) {
                                v[row][j] = v[vs][j] + (v[row][j] - v[vs][j]) / 2.0;
                            }
                        }
                    }

                    c.getConstrainedValues(v[vg], n);
                    f[vg] = objf.evalObjfun(v[vg]);
                    k++;

                    c.getConstrainedValues(v[vh], n);
                    f[vh] = objf.evalObjfun(v[vh]);
                    k++;
                }
            }

  		/* print out the value at each iteration */
            System.out.format("Iteration %d\n", itr);
            for (j = 0; j <= n; j++) {
                for (i = 0; i < n; i++) {
                    System.out.format("%f %f\n", v[j][i], f[j]);
                }
            }

  		/* test for convergence */
            fsum = 0.0;
            for (j = 0; j <= n; j++) {
                fsum += f[j];
            }
            favg = fsum / (n + 1);
            s = 0.0;
            for (j = 0; j <= n; j++) {
                s += Math.pow((f[j] - favg), 2.0) / (n);
            }
            s = Math.sqrt(s);
            if (s < EPSILON) break;
        }
  	/* end main loop of the minimization */

  	/* find the index of the smallest value */
        vs = 0;
        for (j = 0; j <= n; j++) {
            if (f[j] < f[vs]) {
                vs = j;
            }
        }

        System.out.format("The minimum was found at\n");
        for (j = 0; j < n; j++) {
            System.out.format("%e\n", v[vs][j]);
            start[j] = v[vs][j];
        }
        //min=objf.evalObjfun(v[vs]);
        k++;
        System.out.format("%d Function Evaluations\n", k);
        System.out.format("%d Iterations through program\n", itr);

        //return min;
    }

}