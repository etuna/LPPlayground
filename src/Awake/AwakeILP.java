package Awake;
/**
 * KOC UNIVERSITY DISNET
 */

import net.sf.javailp.*;

public class AwakeILP {

    /**
     *     For random tests: make size 128
     *     Timeslots: 24
     *     rep degree 5
     *     Availability table: a 128 * 24 table filled randomly with doubles between 0 and 1
     */


    //Variables---------------------
    public static int workedProperly = 0; // For test purposes
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

        /*A new LP problem*/
        Problem problem = new Problem();


        /*The objective
         * Maximize sigma(i) Ui
         *
         */
        Linear linear = new Linear();
        String objective = new String();
        for (int t = 0; t < timeSlots; t++)
        {
            objective = new String();
            objective = "U" + t;
            linear.add(1, objective);
        }
        problem.setObjective(linear, OptType.MAX);


        /*
         * Part 1: for each t Ut = sigma(i) YiTit
         * Ut represents the availability per hour
         */
        for (int t = 0; t < timeSlots; t++)
        {
            linear = new Linear();
            linear.add(-1, "U" + t);

            for (int i = 0; i < size; i++)
            {
                    String cons1 = new String();
                    cons1 = "Y" + i;
                    double prob =  availabilityTable[i][t];
                    prob = Math.log(prob * 100);
                    if (prob < -1000 || prob == 0)
                        prob = Math.log(0.01);
                    linear.add(prob, cons1);
            }
            problem.add(linear, "=", 0);
        }


        /*
         * Part 2: sigma(i) Yi = repDegree
         */
        linear = new Linear();
        for (int i = 0; i < size; i++)
        {
                String cons2 = "Y" + i;
                linear.add(1, cons2);
        }
        problem.add(linear, "=", repDegree);


        /*
         * Part 3 = Constraint on Yi
         * Yi >= 0 && Yi <= 1
         */
        for (int i = 0; i < size; i++)
        {
                linear = new Linear();
                String cons3 = "Y" + i;
                linear.add(-1, cons3);
                problem.add(linear, "<=", 0);

                linear = new Linear();
                String cons4 = "Y" + i;
                linear.add(1, cons4);
                problem.add(linear, "<=", 1);
}


        /*
        * Part 4: Set the type of Yi
        */
        for (int i = 0; i < size; i++)
        {
        String cons5 = "Y" + i;
        problem.setVarType(cons5, Integer.class);
        }


        /**
         * Solving the problem
         */
        Solver solver = factory.get(); // you should use this solver only once for one problem
        Result result = solver.solve(problem);
        System.out.println(result.getObjective().toString());
        workedProperly = 1;
        return (result);
        }
}
