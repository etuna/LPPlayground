package helloCPLEX;

/**
 * @author Esat Tunahan Tuna, etuna@ku.edu.tr
 * KOC UNIVERSITY DISNET
 */
        import ilog.concert.*;
        import ilog.cplex.*;

public class Example2 {

    public static void main(String[] args) {
        model();
    }

    public static void model() {

        // Main Variables---------------------------------
        int n = 4; // number of cargoes
        int m = 3; // number of compartments
        double[] p = {310, 380, 350, 285}; // profit of each cargo
        double[] v = {480, 650, 580, 390}; // volume per weight of each cargo
        double[] a = {18, 15, 23, 12}; // weight of each cargo
        double[] c = {10, 16, 8}; // weight capacity of each compartment
        double[] V = {6800, 8700, 5300}; // volume capacity of each compartment
        //------------------------------------------


        try {
            // Define the model, cplex object
            IloCplex cplex = new IloCplex();



            //VARIABLES------------------------------------------------------------
            // IloNumVarArray
            IloNumVar[][] x = new IloNumVar[n][];
            for(int i = 0; i < n; i++) {
                //x[i]s are num var arrays in the range of 0 to max. Length : m (= # of compartments)
                x[i] = cplex.numVarArray(m, 0, Double.MAX_VALUE);
            }

            // y is a IloNumVar in the range of 0 to max
            IloNumVar y = cplex.numVar(0, Double.MAX_VALUE);
            //----------------------------------------------------------------


            // EXPRESSIONS---------------------------------------------
            // How much weight is used in each compartment
            IloLinearNumExpr[] usedWeightCapacity = new IloLinearNumExpr[m];
            // How much volume is used in each compartment
            IloLinearNumExpr[] usedVolumeCapacity = new IloLinearNumExpr[m];
            for(int j = 0; j < m; j++) {
                usedWeightCapacity[j] = cplex.linearNumExpr();
                usedVolumeCapacity[j] = cplex.linearNumExpr();
                for(int i = 0; i < n; i++) { // make a summation
                    usedWeightCapacity[j].addTerm(1.0, x[i][j]);
                    usedVolumeCapacity[j].addTerm(v[i], x[i][j]);
                }
            }
            //----------------------------------------------------------------



            // OBJECTIVE------------------------------------------------------
            IloLinearNumExpr objective = cplex.linearNumExpr();
            for(int i = 0; i < n; i++) { // for each cargo
                for(int j = 0; j < m; j++) { // for each compartment
                    objective.addTerm(p[i], x[i][j]);
                }
            }

            // Define objective
            cplex.addMaximize(objective);
            //---------------------------------------------------------------



            // CONSTRAINTS---------------------------------------------------
            // (1) The total amount carried can't exceed the capacity constraint of each compartment
            for(int i = 0; i < n; i++) {
                cplex.addLe(cplex.sum(x[i]), a[i]);
            }
            // (2) The total amount carried cant't exceed the available amount
            for(int j = 0; j < m; j++) {
                cplex.addLe(usedWeightCapacity[j], c[j]);
                cplex.addLe(usedVolumeCapacity[j], V[j]);
                // Equality constraint --> balancing the airplane
                cplex.addEq(cplex.prod(1/c[j], usedWeightCapacity[j]), y);
            }

            // Suppress the output printout
            cplex.setParam(IloCplex.IntParam.SimDisplay, 0);
            //-------------------------------------------------------------



            // SOLUTION----------------------------------------------------
            if(cplex.solve()) {
                System.out.println("Obj fun = " + cplex.getObjValue());
                for(int i = 0; i < n; i++) {
                    for(int j = 0; j < m; j++) {
                        System.out.println("x[" + i + "," + j + "] = " + cplex.getValue(x[i][j]));
                    }
                }
                System.out.println("y = " + cplex.getValue(y));
            } else {
                System.out.println("Model not solved!");
            }
            //------------------------------------------------------------


            //END---------------------------------------------------------
            cplex.end();
            //------------------------------------------------------------
        } catch(IloException ex) {
            ex.printStackTrace();
        }
    }
}