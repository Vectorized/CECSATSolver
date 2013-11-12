package sat;



public class SLiteral implements Comparable<SLiteral>{
	
	public SLiteral inverseCounterpart;
	public boolean value;
	public boolean assigned;
	public boolean isNegative; 
	public int priority;
	
	public SLiteral(boolean isNegative) {
		this.assigned = false;
		this.isNegative = isNegative;
		this.priority = 0;
		// TODO Auto-generated constructor stub
	}
	public void setInverseCounterpart(SLiteral inverseCounterpart){
		this.inverseCounterpart = inverseCounterpart;
	}
	public void setValue(boolean value) {
		this.value = value;
		this.inverseCounterpart.value = !value;
		this.inverseCounterpart.assigned = this.assigned = true;
	}

	public void setUnassigned() {
		this.assigned = false;
		this.inverseCounterpart.assigned = false;
	}
	public boolean evaluatesTo(){
		return this.value; // value XOR isNegative
	}
	public void setEvalutateToTrue(){
		/*if (this.isNegative) { System.out.println("NEG!!"); }
		else {
			System.out.println("POS!!"); 
		}*/
		this.assigned = this.inverseCounterpart.assigned = true;
		this.value = true;
		this.inverseCounterpart.value = false;
		
		System.out.println(this.value +" "+ this.inverseCounterpart.value);
	}
	public void invertValue(){
		this.inverseCounterpart.value = this.value;
		this.value = !this.value;
	}
	public void incrementPriority(){
		++this.priority;
	}
	@Override
	public int compareTo(SLiteral o) {
		return o.priority-this.priority;
	}

	
}
