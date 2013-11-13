package sat;

import immutable.ImList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


import java.util.Map;

import sat.env.Bool;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;


public class SATSolver {

	public SLiteralPool sLiteralPool;
	public ArrayList<SClause> clauses;
	public ArrayDeque<SLiteral> assignedLiterals;
	
	/**
	 * Takes in a Formula representing a Boolean Satisfiability problem represented in CNF format.
	 * It attempts to search for a possible solution to the problem.
	 * If no solution is found, null is returned.
	 * 
	 * @param formula A Formula representing an Boolean Satisfiability problem represented in CNF format.
	 * @return An Environment containing a possible solution to Formula. 
	 */
    public static Environment solve(Formula formula) {

    	SATSolver solver = new SATSolver(formula.getSize());
    	
    	//Converting the Formula to internal data structures optimized for more efficiency
		ImList<Clause> clauseList = formula.getClauses();
		for (Clause c:clauseList) {
			Iterator<Literal> literalIterator = c.iterator();
			
			SClause newClause = new SClause();
			while (literalIterator.hasNext()) {
				Literal literal = literalIterator.next();
				String variable = literal.getVariable().toString();
				if (literal instanceof PosLiteral) {
					newClause.addLiteral(solver.sLiteralPool.getPositiveLiteralWithString(variable));
				} else {
					newClause.addLiteral(solver.sLiteralPool.getNegativeLiteralWithString(variable));
				}
			}
			solver.clauses.add(newClause);
		}
		
		//Prioritize the literals
		solver.sLiteralPool.sortLiteralSelectionList();
		
		//Starts the solving process
		Boolean solvable = solver.attemptSolving(solver.clauses);
        
		//If a solution is found, it starts repackaging it in an Environment instance before returning it
		if (solvable==false) {
        	return null;
        } else {
        	Environment environment = new Environment(); 
        	HashMap<String, SLiteral> literalsHashMap = solver.sLiteralPool.positiveLiteralsHashMap;
        	for (Map.Entry<String, SLiteral> entry : literalsHashMap.entrySet()) {
        		String s = entry.getKey();
        		SLiteral l = entry.getValue();
        		if (l.assigned) { 
        			environment = environment.put(new Variable(s), l.value ? Bool.TRUE:Bool.FALSE); 
        		}
        	}
        	return environment;
        }
    }
	
    /**
     * A private constructor. The SAT Solver is only to be used by its solve method.
     */
	private SATSolver(int numberOfClauses){
		this.sLiteralPool = new SLiteralPool();
		this.clauses = new ArrayList<SClause>(numberOfClauses);
		this.assignedLiterals = new ArrayDeque<SLiteral>();
	}

	/**
	 * This is the main recursive method of the DPLL algorithm
	 * @return TRUE if the problem has a solution else FALSE. Leaves sLiteralPool containing the answer if TRUE.
	 */
    private boolean attemptSolving(ArrayList<SClause> subClauses){
    	if (isSolved(subClauses)) { return true; } 	//If solved, end search
    	
    	SLiteral lastLiteral = assignedLiterals.peekLast();
    	if (BCP(subClauses)==false) { //i.e the previous assignment caused a chain reaction of unit clauses that resulted in false
    		undoAssignmentsToLiteral(lastLiteral);
    		return false; 
    	}
    	ArrayList<SClause> subSubClauses = notTrueSubClauses(subClauses);
    	
    	SLiteral unassignedLiteral = sLiteralPool.getUnassignedLiteral();
    	if (unassignedLiteral==null) { 
    		return true; 
    	} 
    	unassignedLiteral.setEvalutateToTrue();
    	assignedLiterals.add(unassignedLiteral);
    	if (attemptSolving(subSubClauses)) {
    		return true;
    	}
    	unassignedLiteral.invertValue();
    	if (attemptSolving(subSubClauses)) {
    		return true;
    	}
    	undoAssignmentsToLiteral(lastLiteral);
    	
    	return false;
    }

    private ArrayList<SClause> notTrueSubClauses(ArrayList<SClause> subClauses) {
    	int subClausesSize = subClauses.size();
    	ArrayList<SClause> newSubClauses = new ArrayList<SClause>(subClausesSize>>3);
    	for (int i=0; i<subClausesSize; ++i) {
    		SClause c = subClauses.get(i);
    		if (!c.isTrue()) { newSubClauses.add(c); }
    	}
    	return newSubClauses;
    }
    
    private boolean BCP(ArrayList<SClause> subClauses) {    	
    	boolean finished = false;
    	int subClausesSize = subClauses.size();
    	while (!finished) {
    		finished = true;
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
    		if (assignedLiterals.peekLast()==null) { return; }
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
