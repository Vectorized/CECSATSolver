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



/**
 * This is the class that takes in a CNF (Conjunctive Normal Form) represented in an instance of Formula, 
 * attempt to search for a possible solution and return one if there is.
 * 
 * Some Terminology:
 * BCP 			- Boolean Constraint Propagation: Identifying Unit Clauses and setting them to evaluate to TRUE
 * subClauses	- An Array of some the original Clauses. 
 * 
 * Class Dependencies:
 * SClause, SLiteral, SLiteralPool
 * 
 * @author Kang Yue Sheng Benjamin
 *
 */
public class SATSolver {

	public SLiteralPool sLiteralPool;  //An SLiteralPool to manage the creation and retrieval of SLiterals
	public ArrayList<SClause> clauses; //An ArrayList of SClauses
	public ArrayDeque<SLiteral> assignedLiterals; //A stack to keep track of which SLiterals have been assigned for backtracking
	
	
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
					SLiteral literalToAdd = solver.sLiteralPool.getPositiveLiteralWithString(variable);
					newClause.addLiteral(literalToAdd);
				} else {
					SLiteral literalToAdd = solver.sLiteralPool.getNegativeLiteralWithString(variable);
					newClause.addLiteral(literalToAdd);
				}
			}
			int newClauseSize = newClause.literals.size();
			for (int i=0; i!=newClauseSize; ++i) {
				newClause.literals.get(i).priority -= newClauseSize;
			}
			solver.clauses.add(newClause);
		}
		
		//Prioritize the literals
		solver.sLiteralPool.preProcess();
		
		//Starts the solving process
		boolean solvable = solver.attemptSolving(solver.clauses);
        
		//If a solution is found, it starts repackaging it in an Environment instance before returning it
		if (solvable==false) {
        	return null;
        } else {
        	Environment environment = new Environment(); 
        	HashMap<String, SLiteral> literalsHashMap = solver.sLiteralPool.positiveLiteralsHashMap;
        	for (Map.Entry<String, SLiteral> entry : literalsHashMap.entrySet()) {
        		SLiteral l = entry.getValue();
        		if (l.assigned) { 
        			environment = environment.put(new Variable(entry.getKey()), l.value ? Bool.TRUE:Bool.FALSE); 
        		}
        	}
        	return environment;
        }
    }
	
    /**
     * A private constructor. The SAT Solver is only to be used by its solve method.
     */
	private SATSolver(int numberOfClauses){
		//Initialize internal data structures.
		this.sLiteralPool 			= new SLiteralPool();
		this.clauses 				= new ArrayList	<SClause>(numberOfClauses);
		this.assignedLiterals 		= new ArrayDeque<SLiteral>();
	}

	/**
	 * This is the main recursive method of the DPLL algorithm
	 * @return TRUE if the problem has a solution, else FALSE. Leaves sLiteralPool containing the answer if TRUE.
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
    
    /**
     * Returns an ArrayList of SClause that are yet to be determined to be TRUE
     * @param subClauses
     * @return
     */
    private ArrayList<SClause> notTrueSubClauses(ArrayList<SClause> subClauses) {
    	int subClausesSize = subClauses.size();
    	ArrayList<SClause> newSubClauses = new ArrayList<SClause>(subClausesSize>>3);
    	for (int i=0; i!=subClausesSize; ++i) {
    		SClause c = subClauses.get(i);
    		if (!c.isTrue()) { newSubClauses.add(c); }
    	}
    	return newSubClauses;
    }
    
    /**
     * This is the Boolean Constraint Propagation method.
     * It can assign SLiterals to TRUE or FALSE by iterating through subClauses for "unit clauses". 
     * It will push any newly assigned SLiterals into assignedLiterals
     * Thus, care must be taken to unassign any SLiteral that has been assigned by it if necessary, such as backtracking.
     * @param subClauses
     * @return TRUE if no conflict has been found, otherwise FALSE.
     */
    private boolean BCP(ArrayList<SClause> subClauses) {    	
    	boolean finished = false;
    	int subClausesSize = subClauses.size();
    	while (!finished) {
    		finished = true;
        	for (int i=0; i!=subClausesSize; ++i) {
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

    /**
     * A method used by BCP() to reset all SClauses marked as ignorableByBCP.
     * Ignorable clauses are either already TRUE, or set to be TRUE by unit clause detection. 
     * @param subClauses
     */
    private void resetIgnoreByBCP(ArrayList<SClause> subClauses) {
    	int subClausesSize = subClauses.size();
    	for (int i=0; i!=subClausesSize; ++i) {
    		SClause c = subClauses.get(i);
    		c.ignoreByBCP = false;
    	}
    }
    
    /**
     * A method to read from the assignedLiterals stack and unassign all literals that have been added after SLiteral s
     * @param s
     */
    private void undoAssignmentsToLiteral(SLiteral s) {
    	while (assignedLiterals.peekLast()!=s) {
    		if (assignedLiterals.peekLast()==null) { return; }
    		SLiteral lastLiteral = assignedLiterals.removeLast();
    		lastLiteral.setUnassigned();
    	}
    }
    
    /**
     * Check if the subClauses consists entirely of SClauses that evaluate to TRUE
     * @param subClauses
     * @return TRUE if solved, otherwise FALSE
     */
    private boolean isSolved(ArrayList<SClause> subClauses){
    	int subClausesSize = subClauses.size();
    	for (int i=0; i!=subClausesSize; ++i) {
    		SClause c = subClauses.get(i);
    		if (!c.isTrue()) return false; 
    	}
    	return true; 
    }

}
