package ILPtoCPLEX;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;

import java.util.Random;

import static ILPtoCPLEX.IPLEX.objective;

public class Test {


    public static void main(String[] args) throws IloException {


        int size = 40;
        int MNR = 5;
        int[][] L = new int[size][size];
        Random rand = new Random();

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
        double[] resY = new double[size];
        double[][] resX = new double[size][size];
        double resObjective;

        resObjective = result.getValue(objective);
        resY = result.getValues(iplex.Y);
        for (int i = 0; i < size; i++) {
            resX[i] = result.getValues(iplex.X[i]);
        }

        System.out.println("RESULTS OF CPLEX SOLUTION------");
        for (int i = 0; i < resY.length; i++) {
            System.out.println("Y[" + i + "] :" + resY[i]);
            for (int j = 0; j < resX[i].length; j++) {
                System.out.println("X[" + i + "][" + j + "] :" + resX[i][j]);
            }
            System.out.println("Objective : " + resObjective + "\n");
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
}
