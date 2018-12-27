package Awake;
/**
 * @author Esat Tunahan Tuna, etuna@ku.edu.tr
 * KOC UNIVERSITY DISNET
 */

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLQNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.util.ArrayList;

public class AwakeCPLEX {

    /**
     *     For random tests: make size 128
     *     Timeslots: 24
     *     rep degree 5
     *     Availability table: a 128 * 24 table filled randomly with doubles between 0 and 1
     */

    //Variables-----------------------
    public static int workedProperly = 0; //For test purposes
    static IloLQNumExpr objective;
    public static ArrayList<Integer> AwakeCplexResults = new ArrayList<Integer>(); //Results - not used
    public static IloCplex AwakeCPLEXResult; //Result - not used
    //--------------------------------

    public AwakeCPLEX(){

    }

    public static IloCplex AwakeLEX(int size, int timeSlots, int repDegree, double[][] availabilityTable) throws IloException {

        //Cplex object, derived from IloAlgorithm, capable of solving optimization problems
        IloCplex cplex = new IloCplex();

        //Objective function
        objective = cplex.lqNumExpr(); //Linear of ILP LPSOLVE

        //U and Y arrays
        IloNumVar[] U = new IloNumVar[timeSlots];
        IloIntVar[] Y = new IloIntVar[size];

        //Linear Expression - helper
        IloLQNumExpr linear;

        /*The objective
         * maximize sigma(i) Ui
         */
        U = cplex.numVarArray(timeSlots,0, Double.MAX_VALUE);
        Y = cplex.intVarArray(size,0,Integer.MAX_VALUE);

        for(int i=0; i<timeSlots; i++){
            objective.addTerm(1, U[i]);
        }
        cplex.addMaximize(objective);


        /*
         * Part 1: for each t Ut = sigma(i) YiTit
         * Ut represents the availability per hour
         */
        for(int t= 0 ; t<timeSlots;t++){
            linear = cplex.lqNumExpr();
            for(int i= 0; i<size; i++){
                double prob =  availabilityTable[i][t];
                prob = Math.log(prob * 100);
                if (prob < -1000 || prob == 0)
                    prob = Math.log(0.01);

                linear.addTerm(Y[i],prob);
            }
            cplex.addEq(U[t],linear);
        }


        /*
         * Part 2: sigma(i) Yi = MNR
         */
        linear = cplex.lqNumExpr();
        for(int i= 0;i<size;i++){
            linear.addTerm(1,Y[i]);
        }
        cplex.addEq(repDegree, linear);


        /*
         * Part 3 = Constraint on Yi
         * Yi >= 0 && Yi <= 1
         */
        for(int i=0; i<size;i++){
            cplex.addGe(Y[i],0);
            cplex.addLe(Y[i],1);
        }

        workedProperly = 1;
        return cplex;
    }

}
