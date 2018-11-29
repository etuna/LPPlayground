package ILPtoCPLEX;

import ilog.concert.*;


import ilog.cplex.IloCplex;
import net.sf.javailp.Linear;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class IPLEX {


    IloNumVar[][] randomNumbers;
    static IloNumVar[][] X;
    static IloNumVar[] Y;
    static IloNumExpr objective;

    public IPLEX() {

    }


    public static IloCplex replicaGenerator(int[][] L, int size, int MNR) throws IloException {
        IloCplex cpx = ILP(L, size, MNR);
        return cpx;
    }

    public static void generateXY(IloCplex cplex, int size) throws IloException {
        X = new IloNumVar[size][size];
        Y = new IloNumVar[size];
        for (int i = 0; i < size; i++) {

            for(int j=0; j<size;j++) {
                //X[i] = cplex.numVarArray(1,0, Double.MAX_VALUE);

                // TODO
                // LOOK AT NUMVARARRAY!
                X[i][j] = cplex.numVar(0, Double.MAX_VALUE);
            }


        }

        for (int i = 0; i < size; i++) {
            Y[i] = cplex.numVar(0, Double.MAX_VALUE);

        }

    }


    public static IloCplex ILP(int[][] L, int size, int MNR) throws IloException {
        //Cplex object, derived from IloAlgorithm, capable of solving optimization problems
        IloCplex cplex = new IloCplex();

        generateXY(cplex, size);
        /**
         * Constructing a Problem:
         * Minimize: Sigma(i)Sigma(j) LijXij
         * Subject to:
         * for each i,j Yi>= Xij
         * Sigma(i)Xij = 1
         * Sigma(j)Xij >= Yi
         * Sigma(i)Yi <= MNR
         */


        /*
         * This is the interface for scalar product expressions for numerical variables of any type. Objects of type IloLinearNumExpr represent linear expressions of the form
         * It extends IloNumExpr
         * This helps us create our objective function
         */
        objective = cplex.linearNumExpr(); //Linear of ILP LPSOLVE


        /**
         * Part 1: Minimize: Sigma(i)Sigma(j) LijXij
         */




        IloNumExpr expr;
        IloLinearNumExpr linear;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //String var = "X" + i + "," + j; // X1,2 etc
                /*  Lij*Xij  */
               // expr = cplex.linearNumExpr();
                //expr = cplex.prod(L[i][j], X[i][j]);
                ((IloLinearNumExpr) objective).addTerm(L[i][j], X[i][j]);
            }
        }

        cplex.addMinimize(objective);


        /**
         * Part 2: for each i,j Yi>= Xij
         */

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                //linear = cplex.linearNumExpr();
                // String var = "X" + i + "," + j;
                //linear.addTerm(1, X[i][j]);
                //var = "Y" + i;
                //linear.addTerm(-1, Y[i]);

                cplex.addGe(Y[i], X[i][j]);
                //cplex.add(linear, "<=", 0);
            }

        }


        /**
         * Part 3: Sigma(i)Xij = 1
         */
        for (int j = 0; j < size; j++) {
            linear = cplex.linearNumExpr();
            for (int i = 0; i < size; i++) {
                //String var = "X" + i + "," + j;
                ((IloLinearNumExpr) linear).addTerm(1, (IloNumVar) X[i][j]);


            }
            cplex.addEq(linear, 1);
        }


        /**
         * Part 4: Sigma(j)Xij >= Yi
         */
        //IloNumVar total;
        for (int i = 0; i < size; i++) {
            //linear = new Linear();
            linear = cplex.linearNumExpr();
            for (int j = 0; j < size; j++) {
                //if ((repType == Simulator.system.PRIVATE_REPLICATION && j >= Simulator.system.getNOR())) continue;
                //String var = "X" + i + "," + j;
                ((IloLinearNumExpr) linear).addTerm(1, (IloNumVar) X[i][j]);
            }

            //String var = "Y" + i;
            //linear.add(1, var);
            //cplex.addGe(linear, Y[i]);
        }


        /**
         * Part 5: Sigma(i)Yi <= MNR
         */
        linear = cplex.linearNumExpr();
        for (int i = 0; i < size; i++) {
            //String var = "Y" + i;
            ((IloLinearNumExpr) linear).addTerm(1, (IloNumVar) Y[i]);
        }
        cplex.addLe(linear, MNR);


        /**
         * Part 6: Sigma(j)Xij >= Yi

        for (int i = 0; i < size; i++) {

            for (int j = 0; j < size; j++) {
                //if (repType == Simulator.system.PRIVATE_REPLICATION && j >= Simulator.system.getNOR()) continue;
                linear = cplex.linearNumExpr();
                //String var = "X" + i + "," + j;
                ((IloLinearNumExpr) linear).addTerm(1, (IloNumVar) X[i][j]);
                //problem.add(linear, ">=", 0);
                //linear = new Linear();
                // linear.add(1, var);
                //problem.add(linear, "<=", 1);

            }

            //linear = new Linear();
            // String var = "Y" + i;
            //linear.add(1, var);
            //problem.add(linear, "<=", 1);

            //linear = new Linear();
            // var = "Y" + i;
            //linear.add(1, var);
            //problem.add(linear, ">=", 0);
            cplex.addGe(linear, Y[i]);
        }
*/

        /**
         * Part 7: Set the type of Xij and Yi

         for (int i = 0; i < size; i++)
         {
         for (int j = 0; j < size; j++)
         {
         //if ((repType == Simulator.system.PRIVATE_REPLICATION && j > Simulator.system.getNOR())) continue;
         String var = "X" + i + "," + j;
         problem.setVarType(var, Integer.class);
         }

         String var = "Y" + i;
         problem.setVarType(var, Integer.class);
         } */


        /**
         * Solving the problem
         */
        //Solver solver = factory.get(); // you should use this solver only once for one problem
        //System.out.println(problem.toString());
        //Result result = solver.solve(problem);


        //System.out.println(result.get("X30,30"));
        //System.out.println(result);

        //System.exit(0);
        return cplex;
    }


}
