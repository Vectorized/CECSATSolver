package sat;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeMap;


public class SATSolverTest {
	
	public static void main(String[] args) {
		
		long started = System.nanoTime();
		System.out.println("YOS");
		
		/*
		TreeMap<Integer, Integer> testMap = new TreeMap<Integer, Integer>();
		int members = 6000;
		for (int i=0; i<members; ++i){
			testMap.put(i, i);
		}
		for (int j=0; j<10000;++j){
		for (int i=0; i<members; ++i){
			testMap.get(i);
		}
		}
		*/
		
		
		ArrayList<String> testList = new ArrayList<String>();
		int members = 600000;
		for (int i=0; i<members; ++i) {
			testList.add("Ewqeq");
			testList.remove(testList.size()-1);
		}/*
		for (int j=0; j<100000;++j){

			for (int k=0; k<testList.size();++k) {
				String string = testList.get(k);
			}
		}*/
		
		
		/*
		ArrayDeque<String> testDeque = new ArrayDeque<String>();
		int members = 6000;
		for (int i=0; i<members; ++i) {
			testDeque.add("Ewqeq");
		}
		for (int j=0; j<100000;++j){

			for (String s:testDeque) {
			}
		}*/


		long timeTaken = System.nanoTime()-started;
		System.out.println(timeTaken);
		System.out.println("BYES");

	}
	
	/*
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
    
    */
    
}