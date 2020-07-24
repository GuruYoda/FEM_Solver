package fem;

import iceb.jnumerics.*;

import inf.text.*;

public class Element {
	
	private double area, eModulus;
	private int[] dofNumbers = new int[6];
	private Node n1 , n2;
	private IMatrix Transformation;
	
	public Element(double e, double a, Node n1 , Node n2) {
		this.eModulus = e;
		this.area = a;
		this.n1 = n1;
		this.n2 = n2;
	}
	
	public IMatrix computeStiffnessMatrix() {
		//Computing Direction Cosines (Done in getE1 method)
		//double c1 = (this.n2.getPosition().toArray()[0] - this.n1.getPosition().toArray()[0])/getLength();
		//double c2 = (this.n2.getPosition().toArray()[1] - this.n1.getPosition().toArray()[1])/getLength();
		//double c3 = (this.n2.getPosition().toArray()[2] - this.n1.getPosition().toArray()[2])/getLength();
		
		IMatrix T = new Array2DMatrix(2,6);
		IMatrix Ke = new Array2DMatrix(2,2);
		IMatrix Kg = new Array2DMatrix(6,6);
		IMatrix tmp = new Array2DMatrix(6,2);
		
		//Adding the direction cosines in the transformation matrix
		T.addRow(0, 0, getE1());
		T.addRow(1, 3, getE1());
		this.Transformation = T;
		
		//Computation of Local Stiffness matrix
		double con = (this.eModulus * this.area / getLength());
		Ke.add(0, 0, con*1);
		Ke.add(0, 1, con*-1);
		Ke.add(1, 0, con*-1);
		Ke.add(1, 1, con*1);
		
		//double[][] K = new double[6][6];
		// Conventional way tp form a stiffness matrix
//		double[][] K = {{con*(c1*c1) , con*(c1*c2) , con*(c1*c3) , con*(-c1*c1) , con*-(c1*c2) , con*-(c1*c3)},
//			            {con*(c1*c2) , con*(c2*c2) , con*(c2*c3) , con*-(c1*c2) , con*-(c2*c2) , con*-(c2*c3)},
//			            {con*(c1*c3) , con*(c2*c3) , con*(c3*c3) , con*-(c3*c1) , con*-(c3*c2) , con*-(c3*c3)},
//			            {con*-(c1*c1) , con*-(c1*c2) , con*-(c1*c3) , con*(c1*c1) , con*(c1*c2) , con*(c1*c3)},
//			            {con*-(c1*c2) , con*-(c2*c2) , con*-(c2*c3) , con*(c1*c2) , con*(c2*c2) , con*(c2*c3)},
//			            {con*-(c1*c3) , con*-(c2*c3) , con*-(c3*c3) , con*(c3*c1) , con*(c3*c2) , con*(c3*c3)}};
//		
	
		//Computation of global stiffness matrix from Local stiffness matrix
		BLAM.multiply(1.0 , BLAM.TRANSPOSE , T, BLAM.NO_TRANSPOSE , Ke, 0.0 , tmp);
		BLAM.multiply(1.0 , BLAM.NO_TRANSPOSE , tmp , BLAM.NO_TRANSPOSE , T, 0.0 , Kg);
		
		//The final transposed stiffness matrix 
		//System.out.println(ArrayFormat.fFormat(Kg.toString()));
		return Kg;
		
	}
	
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
	
	public double computeForce() {
		IMatrix disp = new Array2DMatrix(6, 1);
		IMatrix tmp = new Array2DMatrix(2,1);
		for(int i = 0 ; i < 3 ; i++) {
			disp.add(i, 0, this.n1.getDisplacement().get(i));
		}
		for(int i = 3 ; i < 6 ; i++) {
			disp.add(i, 0, this.n2.getDisplacement().get(i-3));
		}

		BLAM.multiply(1.0 , BLAM.NO_TRANSPOSE , this.Transformation, BLAM.NO_TRANSPOSE , disp, 0.0 , tmp);
		
		return (this.area*this.eModulus/getLength()) * (tmp.get(1, 0) - tmp.get(0, 0));
	}
	
	public Vector3D getE1() {
		//Computing Direction Cosines
		double[] c = new double[3];
		for(int i = 0 ; i < c.length ; i++) {
			c[i] = (this.n2.getPosition().toArray()[i] - this.n1.getPosition().toArray()[i])/getLength();
		}
		Vector3D e1 = new Vector3D(c);
		return e1;
	}
	
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
