package nonLinear_Fem;

import iceb.jnumerics.*;
import fem.*;

public class CrisfieldFormulation {
	
	private double eModulus,area;
	Node node1, node2;
	private int[] dofNumbers = new int[6];
	private double[] displacement = new double[6];
	private double S11;
	
	public CrisfieldFormulation(double e, double a, Node n1, Node n2) {
		// TODO Auto-generated constructor stub
		this.eModulus = e;
		this.area = a;
		this.node1 = n1;
		this.node2 = n2;
	} 
	
	public void enumerateDOFs() {
		int count = 0;
		for(int i: node1.getDOFNumbers()) {
			dofNumbers[count] = i;
			count++;
		}
		for(int i: node2.getDOFNumbers()) {
			dofNumbers[count] = i;
			count++;
		}
	}
	
	public int[] getDOFNumbers() {
		return dofNumbers;
	}
	
	public IVectorRO vect_Xe(){
		IVector X = new ArrayVector(6);
		for (int i = 0; i<3; i++){
			X.set(i, node1.getPosition().get(i));
			X.set(i+3, node2.getPosition().get(i));
		}
		return X;
	}
	
	public IVectorRO vect_ue(){
		for (int i = 0; i<6; i++){
			if (dofNumbers[i] == -1){
				this.displacement[i] = 0;
			}
		}
		
		IVector u = new ArrayVector(6);
		
		for (int i = 0; i<3; i++){
			u.set(i, this.displacement[i]);
			u.set(i+3, this.displacement[i+3]);
		}
		return u;
	}
	
	public double computeS11(IVectorRO u){
		IVectorRO X = this.vect_Xe();
		
		double L_square = Math.pow(this.getLength(), 2);
		
		double l_square = Math.pow(X.get(3)+u.get(3)-(X.get(0) + u.get(0)), 2)
				+Math.pow(X.get(4)+u.get(4)-(X.get(1) + u.get(1)), 2)
				+Math.pow(X.get(5)+u.get(5)-(X.get(2) + u.get(2)), 2);
		
		double S11 = this.eModulus*(l_square - L_square)/(2*L_square);
		this.S11 = S11;
		return S11;
	}
	
	public IVectorRO computeInternalForce(IVectorRO u){
		IVectorRO X = this.vect_Xe();
		
		IMatrix A = this.getAMatrix();
		this.computeS11(u);
		
		IVectorRO delta_E11 = A.multiply(X.add(u));
		IVectorRO r = delta_E11.multiply(this.area*this.getEModulus()*S11);
		return r;
	}
	
	public IMatrix getAMatrix(){
		IMatrix I3 = new Array2DMatrix(3,3);
		for (int i = 0; i < 3; i++){
			I3.set(i, i, 1);
		}
		IMatrix A = new Array2DMatrix(6,6);
		A.addMatrix(0, 0, I3);
		A.addMatrix(3, 3, I3);
		A.addMatrix(0, 3, I3.multiply(-1));
		A.addMatrix(3, 0, I3.multiply(-1));
		return A;
	}
	
	public IMatrixRO computeTangentMatrix(IVectorRO u){
		//IMatrix A = this.getAMatrix();
		
		IVectorRO X = this.vect_Xe();
		
		this.computeS11(u);

		IMatrixRO k_geo = this.getAMatrix().multiply(this.S11*this.area/this.getLength());
		
		IMatrixRO k_mat = (this.getAMatrix().multiply(X.add(u))).dyadicProduct(this.getAMatrix().multiply(X.add(u)))
				.multiply(eModulus*area/Math.pow(this.getLength(),3));
		
		IMatrixRO k_t = k_geo.add(k_mat);
		return k_t;
	}
	
	public Node getNode1(){
		return node1;
	}
	
	public Node getNode2(){
		return node2;
	}
	
	public double getArea(){
		return area;
	}
	
	public double getEModulus(){
		return eModulus;
	}
	
	public double getLength(){
		double l = (node2.getPosition()).subtract(node1.getPosition()).normTwo();
		return l;
	}
	
}
