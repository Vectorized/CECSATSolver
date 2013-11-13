package sat;


import sat.env.Environment;
import sat.formula.Formula;


public class SATSolverTest {
	
	public static void main(String[] args) {
		
        Formula formula = CNFParser.formulaFromFile("cnf/038y.cnf", false);
        System.out.println("SAT solver starts.");
        
		long started = System.nanoTime();
        Environment e = SATSolver.solve(formula);
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		

	}
}
