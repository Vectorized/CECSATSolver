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
		this.assigned = this.inverseCounterpart.assigned = false;
	}
	
	public void setEvalutateToTrue(){
		this.value = 
		this.assigned = this.inverseCounterpart.assigned = true;
		this.inverseCounterpart.value = false;
	}
	public void invertValue(){
		this.inverseCounterpart.value = this.value;
		this.value = !this.value;
	}

	@Override
	public int compareTo(SLiteral o) {
		return o.priority-this.priority;
	}

	
}
