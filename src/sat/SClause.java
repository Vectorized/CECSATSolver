package sat;

import java.util.ArrayList;

public class SClause {
	public ArrayList <SLiteral> literals;
	public boolean ignoreByBCP;
	
	public SClause() {
		this.literals = new ArrayList<SLiteral>();
		
	}
	
	public void addLiteral(SLiteral l) {
		this.literals.add(l);
	}
	public SLiteral unassignedLiteralIfUnitClause() {
		int unassignedCount = 0;
		int falseCount = 0;
		SLiteral potentialUnitSLiteral = null;
		int literalSize = this.literals.size();
		for (int i=0; i<literalSize; ++i) {
			SLiteral l = this.literals.get(i);
			if (!l.assigned) {
				++unassignedCount;
				potentialUnitSLiteral = l;
			}
			else if (!l.evaluatesTo()) { 
				++falseCount;
			}
		}
		if (falseCount+unassignedCount==this.literals.size()) {
			return potentialUnitSLiteral;
		}
		return null;
	}
	
	public boolean isFalse(){ //Returns true iff the clause definitely evaluates to false
		int literalSize = this.literals.size();
		for (int i=0; i<literalSize; ++i) {
			SLiteral l = this.literals.get(i);
			if (!l.assigned || 					//if there is an undefinied value, we can't say that it is false!
			 l.evaluatesTo()) return false; 	//if there is a true value, we can't say that it is false too!!
		}
		return true;
	}
	public boolean isTrue(){
		int literalSize = this.literals.size();
		for (int i=0; i<literalSize; ++i) {
			SLiteral l = this.literals.get(i);
			if (l.assigned && l.evaluatesTo()) { return true; }
		}
		return false;
	}
	public boolean isUndetermined(){
		int falseCount = 0;
		int unassignedCount = 0;
		int literalSize = this.literals.size();
		for (int i=0; i<literalSize; ++i) {
			SLiteral l = this.literals.get(i);
			if (!l.assigned) { ++unassignedCount; }
			else if (!l.evaluatesTo()) { ++falseCount; }
		}
		return ( unassignedCount>0 && ((unassignedCount+falseCount)==this.literals.size()));
	}
	
}
