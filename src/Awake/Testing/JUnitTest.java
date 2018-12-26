package Awake.Testing;

import Awake.AwakeCPLEX;
import Awake.AwakeILP;
import ILPtoCPLEX.IPLEX;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import net.sf.javailp.Result;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class JUnitTest {


    //For random tests: make size 128
    //Timeslots: 24
    //rep degree 5
    //Availability table: a 128 * 24 table filled randomly with doubles between 0 and 1


    //Variables---------------------------------------------
    static int size = 128;
    static int timeSlots = 24;
    static int repDegree = 5;
    static double[][] availabilityTable = new double[size][timeSlots];
    static Random rand = new Random();
    public static IPLEX iplex;
    public static IloCplex iplexResult;
    public static AwakeILP awakeILP;
    public static AwakeCPLEX awakeCPLEX;
    public static IloCplex cplex;
    public static Result ILPResult;
    //------------------------------------------------------


    /**
     * Executed once before the test begins
     */
    @BeforeClass
    public static void onceExecutedBeforeAll() throws IloException {

        //Populate availability table with random integers
        double rangeMin = 0;
        double rangeMax = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < timeSlots; j++) {
                double randomValue = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
                availabilityTable[i][j] = randomValue;
            }
        }

        //ILP solution
        awakeILP = new AwakeILP();
        ILPResult = awakeILP.ReplicaOptimizer(size,timeSlots,repDegree,availabilityTable);

        //CPLEX solution
        cplex = awakeCPLEX.AwakeLEX(size,timeSlots, repDegree,availabilityTable);
        cplex.solve();

    }

    /**
     * Awake ILP Test : prints out objective and asserts works properly
     */
    @Test
    public void AwakeILPTest(){
        System.out.println("Awake ILP objective------------------------------------------------");
        System.out.println(ILPResult.getObjective().doubleValue());
        System.out.println("-------------------------------------------------------------------\n\n");
        assertTrue(awakeILP.workedProperly == 1);
    }


    /**
     *  public static IloCplex AwakeLEX(int size, int timeSlots, int repDegree, double[][] availabilityTable)
     */
    @Test
    public void AwakeCPLEXTest() throws IloException {

        System.out.println("Awake CPLEX objective------------------------------------------------");
        System.out.println(cplex.getObjValue());
        System.out.println("-------------------------------------------------------------------\n\n");
        assertTrue(awakeCPLEX.workedProperly == 1);

    }

    /**
     *
     * @throws IloException
     */
    @Test
    public void assertCPLEXLPObjectiveEqual() throws IloException {
        assertEquals(ILPResult.getObjective().doubleValue(),cplex.getObjValue(),1);
    }
}
