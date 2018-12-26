package ILPtoCPLEX;


import net.sf.javailp.*;

import java.util.ArrayList;

public class ILP
{

    /**
     * The result has a set of variables in the form of Yi, and this function prints the i values where their Yi is
     * equal to 1.
     * @param R
     * @param size
     *
     * @return
     */
    //Variables
    public static ArrayList<Integer> ilpResults = new ArrayList<Integer>();
    public static Result ILPResult;
    public static double[] ILPXResults;

    public ILP(){
    }


    public int replicaGenerator(Result R, int size)
    {
        if (R == null)
        {
            System.out.println("replica optimizer 2, null result input");
            System.exit(0);
        }
        String result = new String();
        result = R.toString();
        boolean flag = false;


        ILPXResults = new double[size];
        for(int k=0 ; k<size; k++){
            ILPXResults[k] = 999;
        }

        String[] resArray = result.split(" ");
        for (int i = 0; i < size; i++)
        {
            String target = "Y" + i + "=1";

            if (result.contains(target))
            {
                ilpResults.add(i);
                getXTotals(resArray,i);
                System.out.println(i);
            }
        }

        if (!flag) return -1;

        return 0;
    }
    public Result process(int[][] L, int size, int MNR)
    {

        SolverFactory factory = new SolverFactoryLpSolve(); // use lp_solve
        factory.setParameter(Solver.VERBOSE, 0);
        factory.setParameter(Solver.TIMEOUT, Double.MAX_VALUE); // set timeout to 100 seconds

        //printNameIDDistanceTable(L , size, badCandidateIndices, dataRequesters);
        /**
         * Constructing a Problem:
         * Minimize: Sigma(i)Sigma(j) LijXij
         * Subject to:
         * for each i,j Yi>= Xij
         * Sigma(i)Xij = 1
         * Sigma(j)Xij >= Yi
         * Sigma(i)Yi <= MNR
         */

        Problem problem = new Problem();

        /**
         * Part 1: Minimize: Sigma(i)Sigma(j) LijXij
         */
        Linear linear = new Linear();
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                String var = "X" + i + "," + j; // X1,2 etc
                linear.add(L[i][j], var);
            }
        }
        problem.setObjective(linear, OptType.MIN);


        /**
         * Part 2: for each i,j Yi>= Xij
         */
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                //if ((repType == Simulator.system.PRIVATE_REPLICATION && j >= Simulator.system.getNOR())) continue;
                linear = new Linear();
                String var = "X" + i + "," + j;
                linear.add(1, var);
                var = "Y" + i;
                linear.add(-1, var);
                problem.add(linear, "<=", 0);
            }

        }

        /**
         * Part 3: Sigma(i)Xij = 1
         */
        for (int j = 0; j < size; j++)
        {

            //if ((repType == Simulator.system.PRIVATE_REPLICATION && j >= Simulator.system.getNOR())) continue;
            linear = new Linear();
            for (int i = 0; i < size; i++)
            {
                String var = "X" + i + "," + j;
                linear.add(1, var);
            }
            problem.add(linear, "=", 1);
        }


        /**
         * Part 4: Sigma(j)Xij >= Yi
         */
        for (int i = 0; i < size; i++)
        {
            linear = new Linear();
            for (int j = 0; j < size; j++)
            {
                //if ((repType == Simulator.system.PRIVATE_REPLICATION && j >= Simulator.system.getNOR())) continue;
                String var = "X" + i + "," + j;
                linear.add(-1, var);
            }

            String var = "Y" + i;
            linear.add(1, var);
            problem.add(linear, "<=", 0);
        }


        /**
         * Part 5: Sigma(i)Yi <= MNR
         */
        linear = new Linear();
        for (int i = 0; i < size; i++)
        {
            String var = "Y" + i;
            linear.add(1, var);
        }
        problem.add(linear, "=", MNR);


        /**
         * Part 6: Sigma(j)Xij >= Yi
         */
        for (int i = 0; i < size; i++)
        {

            for (int j = 0; j < size; j++)
            {
                //if (repType == Simulator.system.PRIVATE_REPLICATION && j >= Simulator.system.getNOR()) continue;
                linear = new Linear();
                String var = "X" + i + "," + j;
                linear.add(1, var);
                problem.add(linear, ">=", 0);
                linear = new Linear();
                linear.add(1, var);
                problem.add(linear, "<=", 1);

            }

            linear = new Linear();
            String var = "Y" + i;
            linear.add(1, var);
            problem.add(linear, "<=", 1);

            linear = new Linear();
            var = "Y" + i;
            linear.add(1, var);
            problem.add(linear, ">=", 0);
        }


        /**
         * Part 6: Set the type of Xij and Yi
         */
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                //if ((repType == Simulator.system.PRIVATE_REPLICATION && j > Simulator.system.getNOR())) continue;
                String var = "X" + i + "," + j;
                problem.setVarType(var, Double.class);
            }

            String var = "Y" + i;
            problem.setVarType(var, Double.class);
        }


        /**
         * Solving the problem
         */
        Solver solver = factory.get(); // you should use this solver only once for one problem
        //System.out.println(problem.toString());
        Result result = solver.solve(problem);



        //System.out.println(result.get("X30,30"));
        //System.out.println(result);

        //System.exit(0);

        ILPResult = result;
        return (result);
    }


    /**
     *
     * @param resArr
     * @param index
     */
    public static void getXTotals(String[] resArr,int index){
        double iTotal = 0;

        for(int i=0; i<resArr.length;i++){

            System.out.println(resArr[i].substring(0,3));
            System.out.println(resArr[i].substring(1,4));

            if(resArr[i].substring(0,3).equals("X"+index+",") || resArr[i].substring(1,4).equals("X"+index+",")){

                double val = Double.parseDouble(resArr[i].substring(resArr[i].indexOf("=")+1,resArr[i].length()-1));
                iTotal +=val;
            }
        }
        ILPXResults[index] = iTotal;
    }

    /**
     *
     * @return
     */
    public static double getMinXFromTotal(){
        double min = 999;
        for(int i=0; i<ILPXResults.length;i++){
            if(ILPXResults[i]<min){
                min = ILPXResults[i];
            }
        }
        return min;
    }

}
