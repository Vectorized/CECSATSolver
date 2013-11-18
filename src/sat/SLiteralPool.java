package sat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This is a class to get instances of SLiterals from. It manages the creation and retrieval of SLiteral instances.
 * 
 * Class Dependencies:
 * SClause, SLiteral, SATSolver
 * 
 * @author Kang Yue Sheng Benjamin
 *
 */
public class SLiteralPool {
	
	public HashMap<String, SLiteral> positiveLiteralsHashMap; //A HashMap Containing all the Positive SLiterals with their names as keys
	public HashMap<String, SLiteral> negativeLiteralsHashMap; //A HashMap Containing all the Negative SLiterals with their names as keys
	
	public ArrayList<SLiteral> literalSelectionList; //An ArrayList to store the Sliterals for optimized retrieval of unassigned SLiterals
	public int literalSelectionListOffset;
	
	/**
	 * Default constructor
	 */
	public SLiteralPool() {
		//Initialize internal data structures
		positiveLiteralsHashMap = new HashMap<String, SLiteral>();
		negativeLiteralsHashMap = new HashMap<String, SLiteral>();
		literalSelectionList = new ArrayList<SLiteral>();
		literalSelectionListOffset = 0;
	}
	
	/**
	 * A method to retrieve an instance of a Positive SLiteral represented by its name.
	 *
	 * @param s The name of the SLiteral
	 * @return A Positive SLiteral
	 */
	public SLiteral getPositiveLiteralWithString(String s) {
		if (positiveLiteralsHashMap.containsKey(s)) { //If the SLiteral has already been made
			SLiteral literal = positiveLiteralsHashMap.get(s);	
			return literal;
		} else { //Else if the SLiteral has not yet been made
			SLiteral positiveLiteral = new SLiteral( /*isNegative*/ false);
			SLiteral negativeLiteral = new SLiteral( /*isNegative*/ true);
			negativeLiteral.setInverseCounterpart(positiveLiteral);
			positiveLiteral.setInverseCounterpart(negativeLiteral);
			positiveLiteralsHashMap.put(s, positiveLiteral);
			negativeLiteralsHashMap.put(s, negativeLiteral);
			return positiveLiteral;
		}
	}
	
	/**
	 * A method to retrieve an instance of a Negative SLiteral represented by its name.
	 *
	 * @param s The name of the SLiteral
	 * @return A Negative SLiteral
	 */
	public SLiteral getNegativeLiteralWithString(String s){
		if (negativeLiteralsHashMap.containsKey(s)) { //If the SLiteral has already been made
			SLiteral literal = negativeLiteralsHashMap.get(s);
			return literal;
		} else { //Else if the SLiteral has not yet been made
			SLiteral negativeLiteral = new SLiteral( /*isNegative*/ true);
			SLiteral positiveLiteral = new SLiteral( /*isNegative*/ false);
			positiveLiteral.setInverseCounterpart(negativeLiteral);
			negativeLiteral.setInverseCounterpart(positiveLiteral);
			positiveLiteralsHashMap.put(s, positiveLiteral);
			negativeLiteralsHashMap.put(s, negativeLiteral);
			return negativeLiteral;
		}
	}
	
	/**
	 * Sorts literalSelectionList in descending order by the priority of each SLiteral.
	 * Replaces the SLiteral in the list with its positive or negative counterpart, depending on which has more priority.
	 * Assigns all unit literals.
	 */
	public void preProcess(){ 
		for (SLiteral literal:positiveLiteralsHashMap.values()) {
			SLiteral literalInverseCounterpart = literal.inverseCounterpart;
			
			if (literalInverseCounterpart.addCount==0) {  //Pure literal rule
				literal.setEvalutateToTrue(); 
			} 
			else if (literal.addCount==0) { //Pure literal rule
				literalInverseCounterpart.setEvalutateToTrue(); 
			}
			else if (literalInverseCounterpart.priority>literal.priority) {
				literalSelectionList.add(literalInverseCounterpart);
			} else {
				literalSelectionList.add(literal);
			}
		}
		Collections.sort(literalSelectionList); //Will be sorted in descending order due to the compareTo() function
	}
	
	/**
	 * Get an unassigned SLiteral based on its priority. Higher priority SLiterals are return first.
	 * Recently accessed SLiterals are returned first if they are unassigned.
	 * @return An unassigned SLiteral
	 */
	public SLiteral getUnassignedLiteral(){
		int literalSelectionListSize = this.literalSelectionList.size();
		int i=0;
		for (i=literalSelectionListOffset; i!=literalSelectionListSize; ++i) {
			SLiteral l = literalSelectionList.get(i);
			if (!l.assigned) {
				literalSelectionListOffset = i;
				return l;
			}
		}
		for (i=0; i!=literalSelectionListOffset; ++i) {
			SLiteral l = literalSelectionList.get(i);
			if (!l.assigned) {
				literalSelectionListOffset = i;
				return l;
			}
		}
		return null;
	}

	
	
}
