package helloCPLEX;

/**
 * @author Esat Tunahan Tuna, etuna@ku.edu.tr
 * KOC UNIVERSITY DISNET
 */
import ilog.concert.IloException;
import ilog.concert.IloLQNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class Example1 {

    public static void main(String []args) throws IloException {
        model();
        System.out.println("test");
    }

    public static void model() throws IloException {

        try {
            //TODO
            /*
            * What does I L O stand for?
            * I L(Linear) O(Optimization) ??
            *
            */
            System.out.println("Modelling...");


            //Cplex object, derived from IloAlgorithm, capable of solving optimization problems
            IloCplex cplex  = new IloCplex();


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
            */
            IloLQNumExpr objective = cplex.lqNumExpr();



            /*
            *Under the Interface IloLinearIntExpr
            *void addTerm(int coef, IloIntVar var)
            *
            *Adds the new term <<  coef * var  >> to a scalar product.
            *
            */
            objective.addTerm(1, x);



            /*
            * IObjective AddMinimize(INumExpr expr)
            * Creates and returns an objective to minimize the expression and adds it to the invoking model.
            * Creates and returns a named objective function and adds it to the invoking model.
            * */
            cplex.addMinimize(objective);
            //cplex.addMaximize(objective);


            //Constraints
            /*
            * public IloRange addGe(IloNumExpr e, double v)
            * Creates and returns a range representing the constraint that the specified numeric expression must be greater than or equal to the specified value.
            *
            */
            cplex.addGe(x,0);

            /*
            * public IloRange addLe(IloNumExpr e, double v)
            * Creates and returns a range forcing the specified numeric expression to be less than than or equal to the specified value.
            *
            * */
            cplex.addLe(x,1);

            //Solve
            /*
            * This method returns an IloBool value, where IloTrue indicates that cplex successfully found a feasible (yet not necessarily optimal) solution, and IloFalse specifies that no solution was found.
            * More precise information about the outcome of the last call to the method IloCplex::solve can be obtained by calling cplex.getStatus()
            *
            * */


            cplex.solve();

            /*
            * If a solution has been found with the solve method, you access it and then query it using a variety of methods. The objective function can be accessed by the call getValue(var)
            * */
            System.out.println("x is : "+cplex.getValue(x));
            System.out.println("CPLEX SOLVE:"+cplex.solve());


        }catch(IloException e){
            System.out.println("Error occured");
        }
    }

}
