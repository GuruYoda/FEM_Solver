package fem;

import iceb.jnumerics.*;

import inf.text.*;

public class Element {
	
	private double area, eModulus;
	private int[] dofNumbers = new int[6];
	private Node n1 , n2;
	
	public Element(double e, double a, Node n1 , Node n2) {
		this.eModulus = e;
		this.area = a;
		this.n1 = n1;
		this.n2 = n2;
	}
	
	//public IMatrix computeStiffnessMatrix() {}
	
	public void enumerateDOFs() {
		int count = 0;
		for(int i: n1.getDOFNumbers()) {
			dofNumbers[count] = i;
			count++;
		}
		for(int i: n2.getDOFNumbers()) {
			dofNumbers[count] = i;
			count++;
		}
	}
	
	public int[] getDOFNumbers() {
		return dofNumbers;
	}
	
	//public double computeForce() {}
	
	//public Vector3D getE1() {}
	
	public double getLength() {
		double[] a1 = this.n1.getPosition().toArray();
		double[] a2 = this.n2.getPosition().toArray();
		double l = Math.sqrt(Math.pow(a2[0] - a1[0], 2) + Math.pow(a2[1] - a1[1], 2) + Math.pow(a2[2] - a1[2], 2));
		return l;
	}
	
	public Node getNode1() {
		return this.n1;
	}
	
	public Node getNode2() {
		return this.n2;
	}
	
	public double getArea() {
		return this.area;
	}
	
	public double getEModulus() {
		return this.eModulus;
	}
	
	public void print() {
		System.out.println(ArrayFormat.format(getEModulus()) + ArrayFormat.format(getArea()) + ArrayFormat.format(getLength()));
	}
	
}
