package twoD_fem;

import java.util.ArrayList;

import iceb.jnumerics.*;

import inf.text.*;

import fem.*;

public class Element2D {
	
	private int ng1 = 4; // 4 Gauss Points specified
	private double E , mue; 
	private double xi , eta;
	private Node n1 , n2 , n3 , n4;
	private ArrayList<Node> node = new ArrayList<Node>(4); // Creating Array List
	private double[][] MatConst;
	
	public Element2D(Node n1 , Node n2 , Node n3 , Node n4 , double E , double mue) {
//		this.eta = eta;
//		this.xi = xi;
		this.E = E;
		this.mue = mue;
		//this.ng1 = ng1;
		this.n1 = n1;
		this.n2 = n2;
		this.n3 = n3;
		this.n4 = n4;
		//this.node = nodes;
	}
	
	public ArrayList<Node> ElementNodes(Node n1 , Node n2 , Node n3 , Node n4) {
		node.add(n1);
		node.add(n2);
		node.add(n3);
		node.add(n4);
		
		return node;
		}
	
	//Plain Strain Condition
	public double[][] MatConstPlainStrain() {
		double[][] MatCont = new double[3][3];
		double con = (this.E / ((1-this.mue)*(1-2*this.mue)));
		MatCont[0][0] = con * (1-this.mue);
		MatCont[1][1] = con * (1-this.mue);
		MatCont[2][2] = con * (1-2*this.mue)/2;
		MatCont[0][1] = MatCont[1][0] = con * this.mue;
		MatCont[0][2] = MatCont[1][2] = MatCont[2][0] = MatCont[2][1] = 0;
		this.MatConst = MatCont;
		return MatCont;
		
	}
	
	
	//Plain Stress Condition
	public double[][] MatConstPlainStress() {
		double[][] MatCont = new double[3][3];
		double con = (this.E / ((1-this.mue)*(1-this.mue * this.mue)));
		MatCont[0][0] = con;
		MatCont[1][1] = con;
		MatCont[2][2] = con * (1-this.mue);
		MatCont[0][1] = MatCont[1][0] = con * this.mue;
		MatCont[0][2] = MatCont[1][2] = MatCont[2][0] = MatCont[2][1] = 0;
		this.MatConst = MatCont;
		return MatCont;
		
	}
	
	public double[][] ShapeFunction (double xi , double eta) {
		// Shape Functions for Q4 element
		double[] shapeQ4 = new double[4];
		shapeQ4[0]=0.25*(1-xi)*(1-eta);
		shapeQ4[1]=0.25*(1+xi)*(1-eta);
		shapeQ4[2]=0.25*(1+xi)*(1+eta);
		shapeQ4[3]=0.25*(1-xi)*(1+eta);
		
		// Derivatives of Shape Function wrt xi
		double[] dhdrQ4 = new double[4];
		dhdrQ4[0]=-0.25*(1-eta);
		dhdrQ4[1]=0.25*(1-eta);
	    dhdrQ4[2]=0.25*(1+eta);
		dhdrQ4[3]=-0.25*(1+eta);
		
		// Derivatives of Shape Function wrt eta
		double[] dhdsQ4 = new double[4];
		dhdsQ4[0]=-0.25*(1-xi);
		dhdsQ4[1]=-0.25*(1+xi);
		dhdsQ4[2]=0.25*(1+xi);
		dhdsQ4[3]=0.25*(1-xi);
		
		// 2D array containing 1st row of the shapefunctions , 2nd row with derivatives wrt xi, 3rd row with derivatives wrt eta
		double[][] shapeFunc = new double[3][4];
		
		for (int j = 0 ; j < shapeQ4.length ; j++) {
			shapeFunc[0][j] = shapeQ4[j];
			shapeFunc[1][j] = dhdrQ4[j];
			shapeFunc[2][j] = dhdsQ4[j];
		}
		
		return shapeFunc;
	}
	
	private double[][] NMatrix(){
		double[][] NMat = new double[2][this.ng1];
		for(int i = 0 ; i < node.size() ; i++) {
			NMat[0][i] = ShapeFunction(this.eta, this.mue)[1][i];
			NMat[1][i] = ShapeFunction(this.eta, this.mue)[2][i];
		}	
		return NMat;
	}
	
	private double[][] Jacobian() {
		double[][] jacobian = new double[2][2];
		for(int i = 0 ; i < node.size() ; i++) {
			jacobian[0][0] = jacobian[0][0] + ShapeFunction(this.xi , this.eta)[1][i] * node.get(i).getPosition().getX1();
			jacobian[0][1] = jacobian[0][1] + ShapeFunction(this.xi , this.eta)[1][i] * node.get(i).getPosition().getX2();
			jacobian[1][0] = jacobian[1][0] + ShapeFunction(this.xi , this.eta)[2][i] * node.get(i).getPosition().getX1();
			jacobian[1][1] = jacobian[1][1] + ShapeFunction(this.xi , this.eta)[2][i] * node.get(i).getPosition().getX2();
		}
		
		return jacobian;
	}
	
	private double DeterminantJacobian() {
		return Jacobian()[0][0] * Jacobian()[1][1] - Jacobian()[1][0] * Jacobian()[0][1];
	}
	
	private double[][] InvJacobian() {
		double[][] Inv = new double[2][2];
		if (DeterminantJacobian() != 0) {
			Inv[0][0] = (1/DeterminantJacobian()) * Jacobian()[1][1];
			Inv[1][1] = (1/DeterminantJacobian()) * Jacobian()[0][0];
			Inv[1][0] = -(1/DeterminantJacobian()) * Jacobian()[1][0];
			Inv[0][1] = -(1/DeterminantJacobian()) * Jacobian()[0][1];
			
			return Inv;
		}
		else {
			System.out.println("** The Jacobian is not invertible **");
			return null;
		}
	}
	
	//Method to determine the values of gauss point
	private double[][] GaussQuadrature (int ng1) {
		double[] Gausspoint = new double[ng1];
//		double[] GaussWeight = new double[ng1];
		Gausspoint[0] = -(1/Math.sqrt(3));
		Gausspoint[1] = -Gausspoint[0];
//		GaussWeight[0] = 1;
//		GaussWeight[1] = GaussWeight[0];
		double[][] GQ = new double[5][2];
		GQ[0][0] = GQ[0][1] = GQ[1][1] = GQ[3][0] = Gausspoint[0];
		GQ[1][0] = GQ[2][0] = GQ[2][1] = GQ[3][1] = Gausspoint[1];
		GQ[4][0] = GQ[4][1] = 1;
		return GQ;
	} 
	
	public IMatrix BMatrixPlainStrain() {
		
		//Computation of Stiffness matrix for GP
		for(int i = 0 ; i < ng1 ; i++) {
			this.xi = GaussQuadrature(ng1)[i][0];
			this.eta = GaussQuadrature(ng1)[0][i];
			
			IMatrix N = new Array2DMatrix(NMatrix());
			IMatrix Jinv = new Array2DMatrix(InvJacobian());
			IMatrix C = new Array2DMatrix(MatConstPlainStrain());
		}
		
		
		return null;
	}

}
