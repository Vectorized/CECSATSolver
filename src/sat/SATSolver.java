package sat;

import java.util.ArrayDeque;
import java.util.ArrayList;



public class SATSolver {

	public SLiteralPool sLiteralPool;
	public ArrayList<SClause> clauses;
	public ArrayDeque<SLiteral> assignedLiterals;
	
	/*
    public static Environment solve(Formula formula) {

        throw new RuntimeException("not yet implemented.");
    }*/
	
	public static void main(String[] args) {

		SATSolver solver = new SATSolver();
		SClause c1 = new SClause();
		c1.addLiteral(solver.sLiteralPool.getPositiveLiteralWithString("a"));

		
		SClause c2 = new SClause();
		c2.addLiteral(solver.sLiteralPool.getNegativeLiteralWithString("a"));
		
		SClause c3 = new SClause();
		c3.addLiteral(solver.sLiteralPool.getPositiveLiteralWithString("b"));
		
		SClause c4 = new SClause();
		c4.addLiteral(solver.sLiteralPool.getNegativeLiteralWithString("b"));

		for (int i=0; i<10000; ++i) {
			c1.addLiteral(solver.sLiteralPool.getNegativeLiteralWithString(Integer.toString(i)));
			c2.addLiteral(solver.sLiteralPool.getNegativeLiteralWithString(Integer.toString(i+1)));
			c3.addLiteral(solver.sLiteralPool.getNegativeLiteralWithString(Integer.toString(i+2)));
			c4.addLiteral(solver.sLiteralPool.getNegativeLiteralWithString(Integer.toString(i+3)));
		}
		
		solver.clauses.add(c1);
		solver.clauses.add(c2);
		solver.clauses.add(c3);
		solver.clauses.add(c4);
		
		long started = System.nanoTime();
		System.out.println(solver.attemptSolving(solver.clauses));
		long timeTaken = System.nanoTime()-started;
		System.out.println(timeTaken);
		System.out.println("BYES");
		
		//solver.sLiteralPool.printLiterals();
		
	}
	
	public SATSolver(){
		this.sLiteralPool = new SLiteralPool();
		this.clauses = new ArrayList<SClause>();
		this.assignedLiterals = new ArrayDeque<SLiteral>();
	}

    private boolean attemptSolving(ArrayList<SClause> subClauses){
    	//System.out.println("YOYO");
    	if (isSolved(subClauses)) { return true; } 	
    	
    	SLiteral lastLiteral = assignedLiterals.peekLast();
    	if (BCP(subClauses)==false) { //i.e the previous assignment caused a chain reaction of unit clauses that resulted in false
    		undoAssignmentsToLiteral(lastLiteral);
    		return false;
    	}
    	
    	
    	SLiteral unassignedLiteral = sLiteralPool.getUnassignedLiteral();
    	if (unassignedLiteral==null) return true;
    	unassignedLiteral.setEvalutateToTrue();
    	ArrayList<SClause> subSubClauses = undeterminedSubClauses(subClauses);
    	if (attemptSolving(subSubClauses)) {
    		return true;
    	}
    	unassignedLiteral.invertValue();
    	if (attemptSolving(subSubClauses)) {
    		return true;
    	}
    	undoAssignmentsToLiteral(lastLiteral);
    	assignedLiterals.removeLast().setUnassigned();
    	
    	return false;
    }

    private ArrayList<SClause> undeterminedSubClauses(ArrayList<SClause> subClauses) {
    	ArrayList<SClause> newSubClauses = new ArrayList<SClause>();
    	int subClausesSize = subClauses.size();
    	for (int i=0; i<subClausesSize; ++i) {
    		SClause c = subClauses.get(i);
    		if (c.isUndetermined()) { newSubClauses.add(c); }
    	}
    	return newSubClauses;
    }
    
    
    private boolean BCP(ArrayList<SClause> subClauses) {    	
    	boolean finished = false;
    	while (!finished) {
    		finished = true;
        	int subClausesSize = subClauses.size();
        	for (int i=0; i<subClausesSize; ++i) {
        		SClause c = subClauses.get(i);
        		
        		if (!c.ignoreByBCP){ //If clause has not yet been marked by BCP as ignorable
	    			SLiteral unitLiteral = c.unassignedLiteralIfUnitClause();
	    			if (unitLiteral!=null) { 
	    				finished = false;
	    				c.ignoreByBCP = true;
	    				unitLiteral.setEvalutateToTrue();
	    				assignedLiterals.addLast(unitLiteral);
	    			} else if (c.isTrue()) {
	    				c.ignoreByBCP = true;
	    			} else if (c.isFalse()) { //Some unit clause caused a false to pop out... this means a dead end!
	    				resetIgnoreByBCP(subClauses);
	    				return false;
	    			}
        		}
        		
    		}
    	}
    	resetIgnoreByBCP(subClauses);
    	return true;
    }

    private void resetIgnoreByBCP(ArrayList<SClause> subClauses) {
    	int subClausesSize = subClauses.size();
    	for (int i=0; i<subClausesSize; ++i) {
    		SClause c = subClauses.get(i);
    		c.ignoreByBCP = false;
    	}
    }
    
    private void undoAssignmentsToLiteral(SLiteral s) {
    	while (assignedLiterals.peekLast()!=s) {
    		assignedLiterals.removeLast().setUnassigned();
    	}
    }
    private boolean isSolved(ArrayList<SClause> subClauses){
    	int subClausesSize = subClauses.size();
    	for (int i=0; i<subClausesSize; ++i) {
    		SClause c = subClauses.get(i);
    		if (!c.isTrue()) return false; //isTrue returns true if any of the literal inside is assigned and true.
    	}
    	return true; //True for empty subClauses too!
    }

}
