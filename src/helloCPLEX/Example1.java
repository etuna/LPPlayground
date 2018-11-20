package helloCPLEX;

import ilog.concert.*;
import ilog.cplex.*;


public class Example1 {

    public static void main(String []args){
        model();
        System.out.println("test");
    }

    public static void model() {

        try {
            //TODO
            /*
            * What does I L O stand for?
            *
            * */

            //Cplex object, derived from IloAlgorithm, capable of solving optimization problems
            IloCplex cplex  = new IloCplex();
            System.out.println("Modelling...");

            /*
            * An instance of this class represents a numeric variable in a model.
            * A numeric variable may be either an integer variable or a floating-point variable; that is, a numeric variable has a type, a value of the nested enumeration IloNumVar::Type.
             * By default, its type is Float. It also has a lower and upper bound. A numeric variable cannot assume values less than its lower bound, nor greater than its upper bound.
             *
             * By default, the numeric variable ranges from 0.0 (zero) to the symbolic constant IloInfinity, but we can specify other upper and lower bounds ourselves.
            * */
            IloNumVar x = cplex.numVar(0, Double.MAX_VALUE,"x");
            IloNumVar y = cplex.numVar(0, Double.MAX_VALUE,"y");


            /*
            * This is the interface for scalar product expressions for numerical variables of any type. Objects of type IloLinearNumExpr represent linear expressions of the form
            * It extends IloNumExpr
            * This helps us create our objective function
            * */
            IloLinearNumExpr objective = cplex.linearNumExpr();
            objective.addTerm(1, x);



            cplex.addMinimize(objective);
            //cplex.addMaximize(objective);


            //Constraints
            cplex.addGe(x,0);
            cplex.addLe(x,1);

            //Solve
            cplex.solve();

            System.out.println("x is : "+cplex.getValue(x));



        }catch(IloException e){
            System.out.println("Error occured");
        }
    }

}
