package ILPtoCPLEX;
/**
 * @author Esat Tunahan Tuna, etuna@ku.edu.tr
 * KOC UNIVERSITY DISNET
 */

import ilog.concert.IloException;
import ilog.concert.IloLQNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.util.ArrayList;

/**
 * IPLEX
 */
public class IPLEX {


    /***
     *
     * ### IloNumVar : An instance of this class represents a numeric variable in a model. A numeric variable may be either an integer variable or a floating-point variable; that is, a numeric variable has a type, a value of the nested enumeration IloNumVar::Type.
     *                 By default, its type is Float. It also has a lower and upper bound. A numeric variable cannot assume values less than its lower bound, nor greater than its upper bound.
     *
     * ### IloLQNumExpr : A general expression featuring both linear and quadratic terms. (Subclass of IloLinearNumExpr)
     *
     * ### IloCplex : IloCplex derives from the class IloAlgorithm. Use it to solve Mathematical Programming models, such as: LP, QP, QCP etc
     *
     * ### cplex.numVar : NumVar(Double, Double), it has several signs. Creates and returns a numeric variable with specified bounds. (Inherited from CplexModeler.)
     *
     *
     *
     *
     * #####################
     * ####  IMPORTANT  ####
     * #####################
     *
     *
     * X[i][j] = cplex.numVar(0, Double.MAX_VALUE);
     * X[i] = cplex.numVarArray(1,0, Double.MAX_VALUE);
     *
     * NumVarArray() : Creates and returns an array of numeric variables representing an array of columns with specified lower and upper bounds as well as type.
     * NumVar()      : NumVar(Double, Double), it has several signs. Creates and returns a numeric variable with specified bounds. (Inherited from CplexModeler.)
     *
     *
     * ###########################
     * Do we need NumVarArray() ?
     * ###########################
     *
     * public virtual INumVar[] NumVarArray(
     * 	int n,
     * 	double lb,
     * 	double ub)
     *
     *
     * public interface INumVar : INumExpr, IAddable, ICopyable
     *
     *
     * --------------------------
     *  SAME THINGS BELOW
     *
     *  []X = IloNumVar[size]
     *  X[i] = cplex.numVarArray(size,0, Double.MAX_VALUE);
     *
     *  and
     *
     *  [][] X = IloNumVar[size][size]
     *  X[i][j] = cplex.numVar(0, Double.MAX_VALUE);
     *
     * --------------------------
     * #####################
     * #####################
     *
     * */


    //Variables---------------------------------------------
    static IloNumVar[][] X;
    static IloNumVar[] Y;
    static IloLQNumExpr objective;

    public static ArrayList<Integer> iplexResults = new ArrayList<Integer>();
    public static IloCplex IPLEXResult;
    double[] resY;
    double[][] resX;
    //------------------------------------------------------

    /**
     * IPLEX Constructor
     */
    public IPLEX() {
    }


    /**
     *
     * @param L
     * @param size
     * @param MNR
     * @return
     * @throws IloException
     */
    public static IloCplex replicaGenerator(int[][] L, int size, int MNR) throws IloException {
        IloCplex cpx = ILP(L, size, MNR);
        return cpx;
    }


    /**
     *
     * @param cplex
     * @param size
     * @throws IloException
     */
    public static void generateXY(IloCplex cplex, int size) throws IloException {
        X = new IloNumVar[size][size];
        Y = new IloNumVar[size];
        for (int i = 0; i < size; i++) {

            for(int j=0; j<size;j++) {
                X[i] = cplex.numVarArray(size,0, Double.MAX_VALUE);
                //OR
                //X[i][j] = cplex.numVar(0, Double.MAX_VALUE);
            }


        }

        for (int i = 0; i < size; i++) {
            Y = cplex.numVarArray(size,0,Double.MAX_VALUE);
            //Y[i] = cplex.numVar(0, Double.MAX_VALUE);

        }

    }



    //----------------------------------------------
    /**
     * Constructing a Problem:
     * Minimize: Sigma(i)Sigma(j) LijXij
     * Subject to:
     * for each i,j Yi>= Xij
     * Sigma(i)Xij = 1
     * Sigma(j)Xij >= Yi
     * Sigma(i)Yi <= MNR
     */
    //----------------------------------------------

    /**
     *
     * @param L
     * @param size
     * @param MNR
     * @return
     * @throws IloException
     */
    public static IloCplex ILP(int[][] L, int size, int MNR) throws IloException {

        //Cplex object, derived from IloAlgorithm, capable of solving optimization problems
        IloCplex cplex = new IloCplex();

        //Generate the X and Y Arrays
        generateXY(cplex, size);

        //Objective function
        objective = cplex.lqNumExpr(); //Linear of ILP LPSOLVE

        //Linear Expression - helper
        IloLQNumExpr linear;

        /**
         * Part 1: Minimize: Sigma(i)Sigma(j) LijXij
         */
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                objective.addTerm(L[i][j], X[i][j]);  //Obj : LijXij
            }
        }
        cplex.addMinimize(objective);

        /**
         * Part 2: for each i,j Yi>= Xij
         */
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cplex.addGe(Y[i], X[i][j]);   //Yi Greater than or Equal to Xij
            }
        }

        /**
         * Part 3: Sigma(i)Xij = 1
         */
        for (int j = 0; j < size; j++) {
            linear = cplex.lqNumExpr();
            for (int i = 0; i < size; i++) {
                linear.addTerm(1, X[i][j]);  //E Xij s
            }
            cplex.addEq(linear, 1);
        }

        /**
         * Part 4: Sigma(j)Xij >= Yi
         */
        for (int i = 0; i < size; i++) {
            linear = cplex.lqNumExpr();
            for (int j = 0; j < size; j++) {
                linear.addTerm(1, X[i][j]);
            }
            cplex.addGe(linear, Y[i]);
        }

        /**
         * Part 5: Sigma(i)Yi <= MNR
         */
        linear = cplex.lqNumExpr();
        for (int i = 0; i < size; i++) {
            linear.addTerm(1, Y[i]);
        }
        cplex.addLe(linear, MNR);

        //Return the cplex object
        IPLEXResult = cplex;
        return cplex;
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


    /**
     *
     * @param xs
     * @return
     */
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

    /**
     *
     *
     *
     * System.out.println("X i values------");
     *         System.out.println(getXIndexForMin(getXTotals(resX)));
     *
     * @param xTotals
     * @return
     */
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


    /**
     *
     * @param xTotals
     * @return
     */
    public static double getXForMin(double[] xTotals){
        double min  = 999;
        int ind = -1;
        for(int i =0; i<xTotals.length;i++)
        {
            if(xTotals[i]<min){
                min = xTotals[i];

            }
        }
        return min;
    }

}
