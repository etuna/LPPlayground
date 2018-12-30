package Awake;
/**
 * @author Esat Tunahan Tuna, etuna@ku.edu.tr
 * KOC UNIVERSITY DISNET
 */
import net.sf.javailp.*;

import java.util.ArrayList;

public class AwakeILP {

    /**
     *     For random tests: make size 128
     *     Timeslots: 24
     *     rep degree 5
     *     Availability table: a 128 * 24 table filled randomly with doubles between 0 and 1
     */


    //Variables---------------------
    public static int workedProperly = 0; // For test purposes
    public static ArrayList<Integer> yequal1is;
    //------------------------------


    /**
     *
     * @param size
     * @param timeSlots
     * @param repDegree
     * @param availabilityTable
     * @returns Result
     */
    public static Result ReplicaOptimizer(int size, int timeSlots, int repDegree, double[][] availabilityTable)
    {
        SolverFactory factory = new SolverFactoryLpSolve(); // use lp_solve
        factory.setParameter(Solver.VERBOSE, 0);
        factory.setParameter(Solver.TIMEOUT, Integer.MAX_VALUE);

        yequal1is = new ArrayList<Integer>();

        /*A new LP problem*/
        Problem problem = new Problem();


        /**The objective
         * Maximize sigma(i) Ui
         *
         */
        Linear linear = new Linear();
        String objective = new String();
        for (int t = 0; t < timeSlots; t++)
        {
            objective = new String();
            objective = "U" + t;
            linear.add(1, objective); // Linear = sigma(i) Ui
        }
        problem.setObjective(linear, OptType.MAX); //objective function added to the problem for maximization


        /**
         * Part 1: for each t Ut = sigma(i) YiTit
         * Ut represents the availability per hour
         */
        for (int t = 0; t < timeSlots; t++)
        {
            linear = new Linear();
            linear.add(-1, "U" + t); //linear = -Ut

            for (int i = 0; i < size; i++)
            {
                    String cons1 = new String();
                    cons1 = "Y" + i;
                    double prob =  availabilityTable[i][t];
                    prob = Math.log(prob * 100);
                    if (prob < -1000 || prob == 0)
                        prob = Math.log(0.01);
                    linear.add(prob, cons1); //linear = sigma(i) YiTit
            }
            problem.add(linear, "=", 0); //Constraint : 0 = sigma(i) YiTit - Ut
        }


        /**
         * Part 2: sigma(i) Yi = repDegree
         */
        linear = new Linear();
        for (int i = 0; i < size; i++)
        {
                String cons2 = "Y" + i;
                linear.add(1, cons2); //Linear = sigma(i) Yi
        }
        problem.add(linear, "=", repDegree); //Constraint : sigma(i) Yi = repDegree added to the problem


        /**
         * Part 3 = Constraint on Yi
         * Yi >= 0 && Yi <= 1
         */
        for (int i = 0; i < size; i++)
        {
                linear = new Linear();
                String cons3 = "Y" + i;
                linear.add(-1, cons3); //Linear = -Yi
                problem.add(linear, "<=", 0); //Constraint added to the problem : -Yi<=0  =  Yi>=0

                linear = new Linear();
                String cons4 = "Y" + i;
                linear.add(1, cons4); //Linear = Yi
                problem.add(linear, "<=", 1); //Constraint added to the problem : Yi<=1
}


        /**
        * Part 4: Set the type of Yi
        */
        for (int i = 0; i < size; i++)
        {
        String cons5 = "Y" + i;
        problem.setVarType(cons5, Integer.class); //Setting the type of Yi
        }


        /**
         * Solving the problem
         */
        Solver solver = factory.get(); // you should use this solver only once for one problem
        Result result = solver.solve(problem); //Result of the problem
        System.out.println(result.getObjective().toString()); //prints the result
        yequal1is = extractReplica(result.toString(),size); // Yi == 1 i values
        workedProperly = 1; //For test purposes, not important component
        return (result);
        }


    /**
     *
     * @param result
     * @param size
     * @return
     */
    public static ArrayList<Integer> extractReplica(String result, int size){
        /**
         * Looking for i values which Yi = 1
         */
            ArrayList<Integer> is = new ArrayList<Integer>();

            for (int i = 0; i < size; i++)
            {
                String target = "Y" + i + "=1";

                if (result.contains(target))
                {
                    is.add(i);
                }
            }
            return is;
        }
}
