package nonLinear_Fem;

import iceb.jnumerics.*;
import iceb . jnumerics .lse .*;
import inf.text.*;
import org.ejml.data.SingularMatrixException;
import org.ejml.simple.SimpleMatrix;
import org.ojalgo.array.Array1D;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import fem.*;

public class nonLinear_Structure {
	
	private ArrayList<Node> node = new ArrayList<Node>(); // Creating Array List
	private ArrayList<CrisfieldFormulation> element = new ArrayList<CrisfieldFormulation>(); // Creating Array List
	
	private IMatrix K_a;
	private double[] rExtGlobal;
	private double[] rIntGlobal;
	private double[] uGlobal;
	private IVector rExternal;
	private SimpleMatrix K , r , u;
	private double[] u_0;
	
	public Node addNode(double x1 , double x2 , double x3) {
		Node n = new Node(x1 , x2 , x3);
		node.add(n);
		return n;
	}
	
	public CrisfieldFormulation addElement(double e, double a, int n1, int n2){
		Node node1 = node.get(n1);
		Node node2 = node.get(n2);
		CrisfieldFormulation ele = new CrisfieldFormulation(e,a,node1,node2);
		element.add(ele);
		return ele;
	}
	
	public List<Node> getLON(){
		return node;
	}
	
	public int getNumberOfNodes(){
		return node.size();
	}
	
	public Node getNode(int id){
		return node.get(id);
	}
	
	public List<CrisfieldFormulation> getLOE(){
		return element;
	}
	
	public int getNumberOfElements(){
		return element.size();
	}
	
	public CrisfieldFormulation getElement(int id){
		return element.get(id);
	}
	
	private int enumerateDOFs() {
		int max = 0;
		int eq = 0;
		for(Node i : node) {
			eq = i.enumerateDOFs(eq); 
		}
		for(CrisfieldFormulation i : element) {
			i.enumerateDOFs();
		}
		for (Node i : node) {
			for (int j = 0 ; j < i.getDOFNumbers().length ; j++) {
				if (max < i.getDOFNumbers()[j]) {
					max = i.getDOFNumbers()[j];
				}
			}
		}	
		return max+1;
	}
	
	public void printStructure() {
		System.out.println("Listing Structure \n");
		System.out.println("Nodes");
		System.out.println("  idx" + ArrayFormat.fFormat("x1") + ArrayFormat.fFormat("x2") + ArrayFormat.fFormat("x3"));
		int count1 = 0;
		for(Node n : node) {
			System.out.print(ArrayFormat.format(count1));
			n.print();
			count1++;
		}
		System.out.println("Constraints");
		System.out.println("  node" + ArrayFormat.fFormat("u1") + ArrayFormat.fFormat("u2") + ArrayFormat.fFormat("u3"));
		count1 = 0;
		for(Node n : node) {
			if (n.getConstraint() != null) {
				System.out.print(ArrayFormat.format(count1));
				n.getConstraint().print();
			}
			count1++;
		}
		System.out.println("Forces");
		System.out.println("  node" + ArrayFormat.fFormat("r1") + ArrayFormat.fFormat("r2") + ArrayFormat.fFormat("r3"));
		count1 = 0;
		for(Node n : node) {
			if (n.getForce() != null) {
				System.out.print(ArrayFormat.format(count1));
				n.getForce().print();
			}
			count1++;
		}
		System.out.println("Elements");
		count1 = 0;
		System.out.println("  idx" + ArrayFormat.fFormat("E") + ArrayFormat.fFormat("A") + ArrayFormat.fFormat("length"));
		for(CrisfieldFormulation e : element) {
			System.out.print(ArrayFormat.format(count1));
			e.print();
			count1++;
		}
		
	}
	
	//@SuppressWarnings("unused")
	private void assembleExternalForces(double[] rGlobal){
		for (Node n : node) {
			if (n.getForce() != null) {
				for(int i : n.getDOFNumbers()) {
					if (i != -1) {
						rGlobal[i] = n.getForce().getComponent(i);
					}
				}
			}
		}
		//System.out.println(ArrayFormat.format(rGlobal));
	}
	
	//@SuppressWarnings("unused")
	private void assembleInternalForces(double[] fIntGlobal){
		for(CrisfieldFormulation e : element) {
			IVector f = e.getFint();
			for (int i = 0 ; i < e.getDOFNumbers().length ; i++) {
				if (e.getDOFNumbers()[i] != -1) {
					fIntGlobal[i] = f.get(i);
				}
			}
		}
	}
	
	//@SuppressWarnings("unused")
	private void assembleTangentMatrix(IMatrix kGlobal){
		for(CrisfieldFormulation e : element) {
			IMatrix temp = e.computeStiffnessMatrix();
			for(int i : e.getDOFNumbers()) {
				if (i != -1) {
					for (int j : e.getDOFNumbers()) {
						if (j != -1) {
							kGlobal.set(i, j, temp.get(i, j));
						}
					}
				}
			}
		}
	}
	
	/************************************************
	  SOLUTION USING NEWTON METHOD (LOAD CONTROLLED) 
	*************************************************/
	/* The input arguments of this method are the initial guess displacement value of the initial portion of total load 
	 * (Lambda * load) and the increment in this load at each step called delta lambda. Also the number of iteration for 
	 * which the loop should run and the convergence criteria are to be defined */
	
	public void newtonMethod(double Ini_val, double lambda_init , int n_step, int iter, double tol) throws Exception{
		
		if(lambda_init == 1) {
			System.out.println();
			System.out.println("Entire load cannot be applied in one shot!");
			System.out.println("------------------------------------------");
			System.out.println("***********SOLUTION TERMINATED************");
			System.out.println("------------------------------------------");
			return;
		}
		
		// size of our matrix
		int neq = enumerateDOFs();
		
		// initializing the solution matrices
		SimpleMatrix a = new SimpleMatrix(neq, neq);
		SimpleMatrix b = new SimpleMatrix(neq, 1);
		
		
		// setting the initial displacements to the free dof's (Suitable initial guess important for convergence)
		for(Node n : node) {
			double[] d = {Ini_val , Ini_val , Ini_val};
			for(int i= 0 ; i < n.getDOFNumbers().length ; i++) {
				if(n.getDOFNumbers()[i] == -1) {
					d[i] = 0.0;
				}
			}
			n.setDisplacement(d);
		}
		
		// initializing the initial displacement vector to begin the iteration
		u_0 = new double[neq];
		double[] u_k = new double[neq];					// the array to update the displacements in each iteration of Newton Method
		for (Node n : node) {
			for(int i : n.getDOFNumbers()) {
				if (i != -1) {
					u_0[i] = Ini_val;
					u_k[i] = Ini_val;
				}
			}
		}
		
		this.K_a = new Array2DMatrix(neq, neq);			// initialize the stiffness matrix
		assembleTangentMatrix(K_a);
		
		this.rIntGlobal = new double[neq];				// initialize the internal force array 
		this.rExtGlobal = new double[neq];				// initialize the external force array
		
		double[] rExtGlobal1 = new double[neq];			// initialize the array to store the load increment 
		double[] rExtGlobal2 = new double[neq];			// initialize the array to store the load increment steps at each iteration
		
		double delta_lambda = 0.1;						// load increment 
		//double delta_lambda = lambda_init/n_step;
		
		// Computing external load vector (complete load)
		assembleExternalForces(rExtGlobal);
		
		for(int i = 0 ; i < neq ; i++) {				// compute the array to store the load increment
			rExtGlobal1[i] = rExtGlobal[i] * delta_lambda; 
		}
		
		System.out.println();
		System.out.println("*********************************    BEGINNING OF SOLUTION ITERATIONS   ******************************");
		System.out.println("------------------------------------------------------------------------------------------------------");
		
		while (lambda_init < 1) {
			System.out.println();
			System.out.println("Portion of load applied : " + lambda_init);

			for(int i = 0 ; i < neq ; i++) {			// compute the array to store the load increment steps at each iteration
				rExtGlobal2[i] = rExtGlobal2[i] + rExtGlobal1[i]; 
			}
			
			// Computing internal force vector
			assembleInternalForces(rIntGlobal);
			System.out.println("external force " + Arrays.toString(rExtGlobal2));
			System.out.println("internal force " + Arrays.toString(rIntGlobal));
			// Computing the residual load vector
			double[] res = new double[neq];
			for(int i = 0 ; i < neq ; i++) {
				res[i] = rExtGlobal2[i] - rIntGlobal[i];
			}
			
			System.out.println("res vect " + Arrays.toString(res));
			
			double err = 100;						// initialize the error to be updated in every loop after checking against convergence criteria
			int count = 0;
			while(err > tol && count < iter) {
			//while(count < iter) {
			//while(err > tol) {	
				// Computing coefficient matrix at current iteration
				assembleTangentMatrix(K_a);
				
				double[] del_u = new double[neq];	// array to store the displacement computed u = K\r
				
				// set entries of matrix and right hand side
				for (int i = 0; i < neq ; i++) {
					for (int j = 0 ; j < neq ; j++) {
						a.set(i, j, K_a.get(i, j));
						b.set(i, res[i]);
					}
				}
				this.K = a;
				this.r = b;
				try {
					SimpleMatrix x = a.solve(b);	// solving the linear equations K * u = r
					this.u = x;
	
					for(int i = 0 ; i < neq ; i++) {
						del_u[i] = x.get(i);
					}
				}
				
				catch (SingularMatrixException e) {
					throw new Exception("Singular matrix");
				}
				
				//double[] u_k = new double[neq];
				for(int i = 0 ; i < neq ; i++) {			// updating the displacements at current load step iteration
					u_k[i] = u_k[i] + del_u[i];
				}
				
				/*********** INEFFICIENT WAY (Writing a new method to update the displacements is better)***********/
				
				/*double[] u = u_k;
				double[] d1 = new double[node.size() * 3];
				
				int c = 0;
				for (Node n : node) {
					//double[] d = new double[node.size() * 3];
					System.out.println(Arrays.toString(d1));
					for(int i : n.getDOFNumbers()) {
						if(i != -1) {
							d1[c] = u[i];
						}
						System.out.println("i is " + i);
						System.out.println("c is " + c);
						c++;
					}
					
				}
				
				System.out.println("u is " + Arrays.toString(d1));
				
				for(Node n : node) {
					double[] d = new double[3];
					int cnt = 0;
					for(int i = 0 ; i < 3 ; i++) {
						d[i] = d1[cnt];
						cnt++;
					}
					for(int i= 0 ; i < n.getDOFNumbers().length ; i++) {
						if(n.getDOFNumbers()[i] == -1) {
							d[i] = 0.0;
						}
					}
					n.setDisplacement(d);
				}*/
				
				/*********** EFFICIENT WAY ***********/
				selectDisplacements(u_k);
				/*************************************/
				
				// computation of relative displacement base error for convergence check
				IVector du = new ArrayVector(neq);
				IVector uk_d = new ArrayVector(neq);
				
				double[] ud = new double[neq];				// initialize the denominator of the ratio
				for(int i = 0 ; i < neq ; i++) {			// computation of denominator 
					ud[i] = u_k[i] - u_0[i];				// (displacement at current step - displacement at previous converged step)
				}
				
				du.assignFrom(del_u);
				uk_d.assignFrom(ud);
				
				err = du.normTwo()/uk_d.normTwo();			// error (2nd norm used)
		
				count++;									// increment in counter
			}
			
			System.out.println("the error at convergence is " + err);
			System.out.println("number of iterations        " + count);
			
			for(int i = 0 ; i < neq ; i++) {
				u_0[i] = u_k[i];
			}
			System.out.println();
			System.out.println("------------------------------------------------------------------------------------------------------");
			System.out.println("Dispalcement after convergence is : ");
			System.out.println("u :" + Arrays.toString(u_0));
			System.out.println("------------------------------------------------------------------------------------------------------");
			
			/*********** EFFICIENT WAY ***********/
			selectDisplacements(u_0);
			/*************************************/
			
			lambda_init = lambda_init + delta_lambda;		// increment in load
		}
		
		selectDisplacements(u_k);
		printResults();
	}
	
	//Modified method to update the displacements in each loop
	private void selectDisplacements(double[] uGlobal) {
		int count = 0;
		for(Node n : node) {
			double[] temp = new double[3];
			for (int i = 0 ; i < n.getDOFNumbers().length ; i++) {
				if(n.getDOFNumbers()[i] != -1) {
					temp[i] = temp[i] + uGlobal[count];
					//System.out.println(temp[i]);
					count++;
				}
			}
			n.setDisplacement(temp);
			//n.print();
		}
	}
	
	public void printResults() {
		
		// print
		System.out.println();
		System.out.println ("Solving system of linear equations : K x = r to determine x");
		System.out.println();
		System.out.println ("Matrix K");
		System.out.println (this.K);
		System.out.println ("Vector r");
		System.out.println (this.r);
				
		System.out.println();
		System.out.println("***************  Listing analysis results  ***************");
		//System .out . println (" Solving K x = r");
		System.out.println();
        // print result
        System.out.println ("Solution x in global system");
        System.out.println (Arrays.toString(u_0));
        System.out.println();
        System.out.println("Nodal Displacements");
		System.out.println("  node" + ArrayFormat.fFormat("u1") + ArrayFormat.fFormat("u2") + ArrayFormat.fFormat("u3"));
		int count1 = 0;
		for(Node n : node) {
			if (n.getDisplacement() != null) {
				System.out.print(ArrayFormat.format(count1));
				System.out.print(ArrayFormat.fFormat(n.getDisplacement().toString()));
			}
			count1++;
			System.out.println();
		}
		
		System.out.println();
		System.out.println("Elememt Forces");
		System.out.println("  elem" + ArrayFormat.fFormat("force"));
		count1 = 0;
		for(CrisfieldFormulation e : element) {
			System.out.print(ArrayFormat.format(count1));
			System.out.print(ArrayFormat.format(e.computeForce()));
			count1++;
			System.out.println();
		}
	}
}
