package sat;

import java.util.ArrayList;

/**
 * This class represents a clause - a OR statement of literals
 * @author User
 *
 */
public class SClause {
	public ArrayList <SLiteral> literals;
	public boolean ignoreByBCP;
	
	public SClause() {
		this.literals = new ArrayList<SLiteral>();
	}
	
	/**
	 * Adds a SLiteral to the SClause 
	 */
	public void addLiteral(SLiteral l) {
		this.literals.add(l);
	}
	
	/**
	 * This function returns a unassigned SLiteral if the clause is detected to be "unit".
	 * A unit clause has one unassigned SLiteral and all the other S:iterals are assigned to false.
	 * @return SLiteral a SLiteral that is unassigned in the unit clause
	 */
	public SLiteral unassignedLiteralIfUnitClause() {
		boolean unassignedDetected = false;
		int falseCount = 0;
		SLiteral unassignedLiteral = null;
		int literalSize = this.literals.size();
		
		for (int i=0; i<literalSize; ++i) {
			SLiteral l = this.literals.get(i);
			if (!l.assigned) {
				if (unassignedDetected) { return null; } //Fast return if 2 unassigned is detected
				unassignedLiteral = l;
				unassignedDetected = true;
			}
			else if (!l.value) { 
				++falseCount;
			}
		}
		if (falseCount==literalSize-1) {
			return unassignedLiteral; //unassignedLiteral will be null if there the last variable is assigned and true.
		}
		return null;
	}
	
	/**
	 * 
	 * @return a 
	 */
	public boolean isFalse(){ //Returns true if all the literals are assigned and false
		for (int i=0; i<this.literals.size(); ++i) {
			SLiteral l = this.literals.get(i);
			if (!l.assigned || 					//if there is an unassigned value, we can't say that it is false!
			 l.value) return false; 	//if there is a true value, we can't say that it is false too!!
		}
		return true;
	}
	public boolean isTrue(){ //Returns TRUE if any one of the literals is detected as assigned and true.
		for (int i=0; i<this.literals.size(); ++i) {
			SLiteral l = this.literals.get(i);
			if (l.assigned && l.value) { return true; }
		}
		return false;
	}

	
}
