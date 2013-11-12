package sat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class SLiteralPool {
	public HashMap<String, SLiteral> positiveLiteralsHashMap;
	public HashMap<String, SLiteral> negativeLiteralsHashMap;
	public ArrayList<SLiteral> literalSelectionList;
	
	public SLiteralPool() {
		positiveLiteralsHashMap = new HashMap<String, SLiteral>();
		negativeLiteralsHashMap = new HashMap<String, SLiteral>();
		literalSelectionList = new ArrayList<SLiteral>();
	}
	
	public SLiteral getPositiveLiteralWithString(String s) {
		if (positiveLiteralsHashMap.containsKey(s)) {
			SLiteral literal = positiveLiteralsHashMap.get(s);
			literal.incrementPriority();
			return literal;
		} else {
			SLiteral positiveLiteral = new SLiteral(/*isNegative*/false);
			SLiteral negativeLiteral = new SLiteral(/*isNegative*/true);
			negativeLiteral.setInverseCounterpart(positiveLiteral);
			positiveLiteral.setInverseCounterpart(negativeLiteral);
			positiveLiteralsHashMap.put(s, positiveLiteral);
			negativeLiteralsHashMap.put(s, negativeLiteral);
			literalSelectionList.add(positiveLiteral);
			return positiveLiteral;
		}
	}
	
	public SLiteral getNegativeLiteralWithString(String s){
		if (negativeLiteralsHashMap.containsKey(s)) {
			SLiteral literal = negativeLiteralsHashMap.get(s);
			literal.incrementPriority();
			return literal;
		} else {
			SLiteral negativeLiteral = new SLiteral(/*isNegative*/true);
			SLiteral positiveLiteral = new SLiteral(/*isNegative*/false);
			positiveLiteral.setInverseCounterpart(negativeLiteral);
			negativeLiteral.setInverseCounterpart(positiveLiteral);
			positiveLiteralsHashMap.put(s, positiveLiteral);
			negativeLiteralsHashMap.put(s, negativeLiteral);
			literalSelectionList.add(positiveLiteral);
			return negativeLiteral;
		}
	}
	
	public void sortLiteralSelectionList(){
		for (int i=0; i<literalSelectionList.size(); ++i) {
			SLiteral literal = literalSelectionList.get(i);
			SLiteral literalInverseCounterpart = literal.inverseCounterpart;
			if (literalInverseCounterpart.priority>literal.priority) {
				literalSelectionList.set(i, literalInverseCounterpart);
			}
		}
		Collections.sort(literalSelectionList);
	}
	
	public SLiteral getUnassignedLiteral(){
		int literalSelectionListSize = this.literalSelectionList.size();
		for (int i=0; i<literalSelectionListSize; ++i) {
			SLiteral l = literalSelectionList.get(i);
			if (!l.assigned) return l;
		}
		return null;
	}
	
	public void printStrings() {
		for (String s: positiveLiteralsHashMap.keySet()) {
			System.out.println(s);
		}
	}
	
	public void printLiterals(){
		for (String s: positiveLiteralsHashMap.keySet()) {
			SLiteral l = positiveLiteralsHashMap.get(s);
			if (l.assigned) {
				System.out.println(s + ": "+ l.value);
			} else {
				System.out.println(s + ": Unassigned");
			}
		}
	}
	
	
}
