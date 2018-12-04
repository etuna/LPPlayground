package ILPtoCPLEX;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;

import java.util.Random;

import static ILPtoCPLEX.IPLEX.objective;

public class Test {


    public static void main(String[] args) throws IloException {


        //Variables---------------------------------------------
        int size = 128;
        int MNR = 5;
        int[][] L = new int[size][size];
        Random rand = new Random();
        //------------------------------------------------------

        //Populate L with random integers
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                L[i][j] = rand.nextInt(20);
            }
        }


        /**##############################################################*/
        /**##############################################################
         *
         * IPLEX SOLUTION
         *
         */
        IPLEX iplex = new IPLEX();

        IloCplex result = iplex.replicaGenerator(L, size, MNR);
        result.solve();


        double[] resY = getIPLEXYVals(iplex,result,size);
        double[][] resX = getIPLEXXVals(iplex,result,size);

        System.out.println("RESULTS OF CPLEX SOLUTION------");
        for (int i = 0; i < resY.length; i++) {
            if(resY[i]==1) {
                System.out.println(i);
            }
        }

        System.out.println("---------------------------------------");
        /**##############################################################*/
        /**##############################################################*/




        /**##############################################################*/
        /**##############################################################
         *
         * LPSOLVE SOLUTION
         *
         */

        System.out.println("\nLPSOLVE SOLUTION------------------------------");

        ILP ilp = new ILP();
        ilp.replicaGenerator(ilp.process(L,size,MNR), size);


    }

    public static double[][] getIPLEXXVals(IPLEX iplex, IloCplex result, int size) throws IloException {
        double x[][] = new double[size][size];
        for (int i = 0; i < size; i++) {
            x[i] = result.getValues(iplex.X[i]);
        }

        return x;
    }

    public static double[] getIPLEXYVals(IPLEX iplex, IloCplex result, int size) throws IloException {
        double y[] = new double[size];
        y = result.getValues(iplex.Y);
        return y;
    }
}
