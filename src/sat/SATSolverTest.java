package sat;

import static org.junit.Assert.*;

import org.junit.Test;

import sat.env.*;
import sat.formula.*;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    public static void main(String[] args) {
        // TODO: read in and parse a .cnf file
        // TODO: construct Formula from cnf data
        Formula formula = new Formula();
        System.out.println("SAT solver starts.");
        long timeStart = System.nanoTime();
        Environment e = SATSolver.solve(formula);
        long timeEnd = System.nanoTime();
        long timeTaken = timeEnd - timeStart;
        System.out.println("Time: " + timeTaken/1000000.0 + "ms");   
    }
    
	// TODO: put your test cases for SATSolver.solve here
	
    @Test
    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
    	
    }
    
    
    @Test
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
    	
    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    
    
}