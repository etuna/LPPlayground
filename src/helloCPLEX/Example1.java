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

            IloCplex cplex  = new IloCplex();
            System.out.println("Modelling...");

            IloNumVar x = cplex.numVar(0, Double.MAX_VALUE,"x");
            IloNumVar y = cplex.numVar(0, Double.MAX_VALUE,"y");

            IloLinearNumExpr objective = cplex.linearNumExpr();
            objective.addTerm(1, x);



            cplex.addMinimize(objective);
            //cplex.addMaximize(objective);


            //Constraint
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
