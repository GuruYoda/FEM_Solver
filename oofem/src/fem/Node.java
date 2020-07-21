package fem;

import iceb.jnumerics.*;

import inf.text.*;

public class Node {
	
	private int[] dofNumbers = new int[3];
	private double[] node = new double[3];
	private Constraint c;
	private Force f;
	private Vector3D v;
	private Vector3D v1;
	
	public Node(double x1 , double x2 , double x3) {
		this.node[0] = x1;
		this.node[1] = x2;
		this.node[2] = x3;
			
	}
	
	public void setConstraint(Constraint c) {
		this.c = c;
	}
	
	public Constraint getConstraint() {
		return this.c;
	}
	
	public void setForce(Force f) {
		this.f = f;
	} 
	
	public Force getForce() {
		return this.f;
	}
	
	public int enumerateDOFs(int start) {
		if (this.c == null) {
			for (int i = 0 ; i < dofNumbers.length ; i++) {
				
					dofNumbers[i] = start;
					start++;
					}
			}
		
		else {
			
			for (int i = 0 ; i < dofNumbers.length ; i++) {
				if (c.isFree(i)) {
					dofNumbers[i] = start;
					start++;
				}
				else {
					dofNumbers[i] = -1;
				}
			}
			
		}
		
		return start;
	}
	
	public int[] getDOFNumbers() {
		return this.dofNumbers;
	}
	
	public Vector3D getPosition() {
		v = new Vector3D(this.node);
		return v;
	}
	
	public void setDisplacement(double[] u) {
		v1 = new Vector3D(u);
	}
	
	public Vector3D getDisplacement() {
		return this.v.add(this.v1);
	}
	
	public void print() {
		System.out.println(ArrayFormat.format(this.node));
	}
	

}
