package ILPtoCPLEX;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import net.sf.javailp.Result;

import java.util.ArrayList;
import java.util.Random;


public class Main {

    public static ArrayList<Integer> iplexResults = new ArrayList<Integer>();
    public static ArrayList<Integer> ilpResults = new ArrayList<Integer>();
    public static Result ILPResult;
    public static IloCplex IPLEXResult;

    /**
     *
     * @param args
     * @throws IloException
     */
    public static void mains(String[] args) throws IloException {


        //Variables---------------------------------------------
        int size = 20;
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


        double[] resY = getIPLEXYVals(iplex, result, size);
        double[][] resX = getIPLEXXVals(iplex, result, size);

        System.out.println("RESULTS OF CPLEX SOLUTION------");
        for (int i = 0; i < resY.length; i++) {
            // System.out.println(resY[i]);
            if (resY[i] == 1) {
                iplexResults.add(i);
                System.out.println("Y :"+i);
            }
        }
        System.out.println("X i values------");
        System.out.println(getXIndexForMin(getXTotals(resX)));


        //Double obj = result.getObjValue();
        //System.out.println(obj);

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
        ilp.replicaGenerator(ilp.process(L, size, MNR), size);

        //System.out.println(ILPResult.getObjective().doubleValue());
        System.out.println("------------------------------------------------");

    }


    /**
     *
     * @param iplex
     * @param result
     * @param size
     * @return
     * @throws IloException
     */
    public static double[][] getIPLEXXVals(IPLEX iplex, IloCplex result, int size) throws IloException {
        double x[][] = new double[size][size];
        for (int i = 0; i < size; i++) {
            x[i] = result.getValues(iplex.X[i]);
        }

        return x;
    }

    /**
     *
     * @param iplex
     * @param result
     * @param size
     * @return
     * @throws IloException
     */
    public static double[] getIPLEXYVals(IPLEX iplex, IloCplex result, int size) throws IloException {
        double y[] = new double[size];
        y = result.getValues(iplex.Y);
        return y;
    }

    public static double[] getXTotals(double[][] xs){
        double total = 0;
        double[] xTotals = new double[xs.length];
        for(int i=0; i<xs.length; i++){
            total = 0;
            for(int j=0; j<xs[i].length;j++){
                total += xs[i][j];
            }
            xTotals[i] = total;
        }
        return xTotals;
    }

    public static int getXIndexForMin(double[] xTotals){
        double min  = 999;
        int ind = -1;
        for(int i =0; i<xTotals.length;i++)
        {
            if(xTotals[i]<min){
                min = xTotals[i];
                ind = i;
            }
        }
        return ind;
    }
}
