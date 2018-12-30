package ILPtoCPLEX.Testing;
/**
 * @author Esat Tunahan Tuna, etuna@ku.edu.tr
 * KOC UNIVERSITY DISNET
 */
import ILPtoCPLEX.ILP;
import ILPtoCPLEX.IPLEX;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class JUnitTest {



    //Variables---------------------------------------------
    static int size = 128;
    static int MNR = 1;
    static int[][] L = new int[size][size];
    static Random rand = new Random();
    public static IPLEX iplex;
    public static IloCplex iplexResult;
    public static ILP ilp;
    //------------------------------------------------------



    @BeforeClass
    public static void onceExecutedBeforeAll() throws IloException {

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
        iplex = new IPLEX();

        iplexResult = iplex.replicaGenerator(L, size, MNR);
        iplexResult.solve();


        double[] resY = iplex.getIPLEXYVals(iplex, iplexResult, size);
        double[][] resX = iplex.getIPLEXXVals(iplex, iplexResult, size);

        //System.out.println("RESULTS OF CPLEX SOLUTION------");
        for (int i = 0; i < resY.length; i++) {
            if (resY[i] == 1) {
                IPLEX.iplexResults.add(i);
                System.out.println("Y :"+i);
            }
        }
        /**##############################################################*/
        /**##############################################################
         *
         * LPSOLVE SOLUTION
         *
         */
        ilp = new ILP();
        ilp.replicaGenerator(ilp.process(L, size, MNR), size);
    }
    /**
     *
     * @throws IloException
     */
    @Test
    public void objectiveTest() throws IloException {
        assertEquals(ILP.ILPResult.getObjective().doubleValue(), IPLEX.IPLEXResult.getObjValue(),1);
        //assertNull(ILP.ILPResult);
    }

    /**
     *
     * @throws IloException
     */
    @Test
    public void equalYTest() throws IloException {
        assertEquals(ILP.ilpResults, IPLEX.iplexResults);
    }

    /**
     *
     * @throws IloException
     */
    @Test
    public void equalXTestOnMin() throws IloException {
        double minXTotalIPLEX = IPLEX.getXForMin(IPLEX.getXTotals(IPLEX.getIPLEXXVals(iplex,iplexResult,size)));
        double minXTotalILP = ILP.getMinXFromTotal();
        System.out.println("EqualXTest\nILP:"+minXTotalILP+"\nCPLEX:"+minXTotalIPLEX);
        assertEquals(minXTotalILP, minXTotalIPLEX,1);
    }

    /**
     *
     * @throws IloException
     */
    public void solveAgain() throws IloException {
        iplexResult.solve();
        double[] resY = iplex.getIPLEXYVals(iplex, iplexResult, size);
        double[][] resX = iplex.getIPLEXXVals(iplex, iplexResult, size);

        //System.out.println("RESULTS OF CPLEX SOLUTION------");
        for (int i = 0; i < resY.length; i++) {
            if (resY[i] == 1) {
                IPLEX.iplexResults.add(i);
                System.out.println("Y :"+i);
            }
        }
        ilp.replicaGenerator(ilp.process(L, size, MNR), size);
    }

    /**
     * It tests the replicas , Yi = 1, i values
     * @throws IloException
     */
    @Test
    public void replicaTest() throws IloException {
        ArrayList<Integer> IPLEXis = iplex.extractReplica(iplex,iplexResult,size); //gets Yi = 1 , i values
        System.out.println(IPLEXis.toString());

        ArrayList<Integer> ILPis = ilp.ilpResults; //gets Yi = 1 , i values
        System.out.println(ILPis.toString());

        assertEquals(IPLEXis.toString(), ILPis.toString());
    }

}
