package sat;

import static org.junit.Assert.*;

import org.junit.Test;

import sat.env.Environment;
import sat.formula.Formula;

/**
 * A testing suite for the SATSolver
 * 
 * @author Kang Yue Sheng Benjamin
 *
 */
public class SATSolverTest {
	
	public static void main(String[] args) {
		
		//Uncomment if not running as JUnit
		/*
		SATSolverTest test = new SATSolverTest();
		test.testSATSolver1();
		test.testSATSolver2();
		test.testSATSolver3();
		test.testSATSolver4();
		test.testSATSolver5();
		test.testSATSolver6();
		test.testSATSolver7();
		test.testSATSolver8();
		test.testSATSolver9();
		test.testSATSolver10();
		test.testSATSolver11();
		test.testSATSolver12();
		test.testSATSolver13();
		test.testSATSolver14();
		test.testSATSolver15();
		*/
		
		//Benchmark solving a file
		/*
		SATSolverTest test2 = new SATSolverTest();
		test2.benchmarkSolve("test_cases/a_or_b_yes.cnf");
		*/
		SATSolverTest test2 = new SATSolverTest();
		test2.benchmarkSolve("2d-demo/sat1.cnf");
		test2.benchmarkSolve("2d-demo/sat2.cnf");
		test2.benchmarkSolve("2d-demo/sat3Large.cnf");
		test2.benchmarkSolve("2d-demo/unsat1.cnf");
		test2.benchmarkSolve("2d-demo/unsat2.cnf");
		test2.benchmarkSolve("2d-demo/unsat3Large.cnf");

	}
	
	/**
	 * A method to test the solving of a CNF file
	 * If the CNF can be solved, a possible result is written into BoolAssignment.txt
	 * @param fileName
	 */
	public void benchmarkSolve(String fileName) {
        Formula formula = CNFParser.formulaFromFile(fileName, false);
        System.out.println("SAT solver starts.");
        
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		EnvironmentProcessor.printWhetherSatisfiableOrNot(e);
		EnvironmentProcessor.writeEnvironmentIntoFile(e, "BoolAssignment.txt");
	}
	
	public void assertUnsatisfiable(Environment e){
		assertEquals(null, e);
	}
	public void assertSatisfiable(Environment e){
		assertNotEquals(null, e);
	}
	
    @Test
    public void testSATSolver1(){
        // Empty: Satisfiable
    	String fileName = "test_cases/empty_yes.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 1 SAT solver starts: Empty <Satisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertSatisfiable(e);
    	
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
	
    @Test
    public void testSATSolver2(){
        // (a or b): Satisfiable
    	String fileName = "test_cases/a_or_b_yes.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 2 SAT solver starts: (a or b) <Satisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertSatisfiable(e);
    	
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
     
    @Test
    public void testSATSolver3(){
        // (~a) and (a): Not Satisfiable
    	String fileName = "test_cases/not_a_and_a_no.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 3 SAT solver starts: (~a) and (a) <Unsatisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertUnsatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
    @Test
    public void testSATSolver4(){
        // (~a or b) and (a or b):  Satisfiable
    	String fileName = "test_cases/simple_boolean_yes.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 4 SAT solver starts: (~a or b) and (a or b) <Satisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertSatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
    @Test
    public void testSATSolver5(){
        // Sample Unsatisfiable Circuit (c11):  Unsatisfiable
    	String fileName = "test_cases/test1_no.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 5 SAT solver starts: Sample Unsatisfiable Circuit (c11) <Unsatisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertUnsatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
    @Test
    public void testSATSolver6(){
        // 6000 clauses unsatisfiable:  Unsatisfiable
    	String fileName = "test_cases/6000_clauses_no.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 6 SAT solver starts: 6000 clauses unsatisfiable <Unsatisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertUnsatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
    
    @Test
    public void testSATSolver7(){
        // Zebra Problem:  Unsatisfiable
    	String fileName = "test_cases/zebra_yes.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 7 SAT solver starts: Zebra Problem <Satisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertSatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
    @Test
    public void testSATSolver8(){
        // Pigeon Hole 6 Problem:  Unsatisfiable
    	String fileName = "test_cases/pigeon_hole_6_no.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 8 SAT solver starts: Pigeon Hole 6 Problem <Unsatisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertUnsatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
    @Test
    public void testSATSolver9(){
        // Pigeon Hole 9 Problem:  Unsatisfiable
    	String fileName = "test_cases/pigeon_hole_9_no.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 9 SAT solver starts: Pigeon Hole 9 Problem <Unsatisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertUnsatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
    @Test
    public void testSATSolver10(){
        // Sample A.I Problem 1:  Satisfiable
    	String fileName = "test_cases/1_yes.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 10 SAT solver starts: Sample A.I Problem 1 <Satisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertSatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
    @Test
    public void testSATSolver11(){
        // Sample A.I Problem 2:  Satisfiable
    	String fileName = "test_cases/2_yes.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 11 SAT solver starts: Sample A.I Problem 2 <Satisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertSatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
    @Test
    public void testSATSolver12(){
        // Sample A.I Problem 3:  Satisfiable
    	String fileName = "test_cases/3_yes.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 12 SAT solver starts: Sample A.I Problem 3 <Satisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertSatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
    @Test
    public void testSATSolver13(){
        // Sample A.I Problem 4:  Satisfiable
    	String fileName = "test_cases/4_yes.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 13 SAT solver starts: Sample A.I Problem 4 <Satisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertSatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
    @Test
    public void testSATSolver14(){
        // Sample A.I Problem 5:  Satisfiable
    	String fileName = "test_cases/5_yes.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 14 SAT solver starts: Sample A.I Problem 5 <Satisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertSatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
    
    @Test
    public void testSATSolver15(){
        // 16 Queens Problem:  Satisfiable
    	String fileName = "test_cases/16queens_yes.cnf";
    	Formula formula = CNFParser.formulaFromFile(fileName, false);
    	System.out.println("Test 15 SAT solver starts: 16 Queens Problem <Satisfiable>  File: " + fileName);
    	System.out.println("Clauses: " + formula.getSize());
    	
		long started = System.nanoTime();
		Environment e = SATSolver.solve(formula);
		
		long timeTaken = System.nanoTime()-started;
		
		System.out.println("Time:" + timeTaken/1000000.0 + "ms");
		System.out.println(e);
		
		assertSatisfiable(e);
         
    	System.out.println("\n\n-----------------------------------------------------------\n\n");
    }
    
}
