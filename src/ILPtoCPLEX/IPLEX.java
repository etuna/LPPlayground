package ILPtoCPLEX;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.cplex.IloCplex;

public class IPLEX {

    public int replicaGenerator(){

        return 1;
    }

    public void ILP() throws IloException {
        //Cplex object, derived from IloAlgorithm, capable of solving optimization problems
        IloCplex cplex = new IloCplex();



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
         * */
        IloLinearNumExpr objective = cplex.linearNumExpr(); //Linear of ILP LPSOLVE


    }


}
