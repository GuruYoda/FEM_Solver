package fem;

import iceb.jnumerics.*;
import iceb . jnumerics .lse .*;
import inf.text.*;
import java.util.Scanner;


import java.util.*;


public class Structure {
	
	private ArrayList<Node> node = new ArrayList<Node>(); // Creating Array List
	private ArrayList<Element> element = new ArrayList<Element>(); // Creating Array List
	private IMatrix K_a;
	private double[] rGlobal;
	private double[] uGlobal;
	
	public Node addNode(double x1 , double x2 , double x3) {
		Node n = new Node(x1 , x2 , x3);
		node.add(n);
		return n;
	}
	
	public Element addElement(double e , double a , int id1 , int id2) {
		Element e1 = new Element(e , a , getNode(id1) , getNode(id2));
		element.add(e1);
		return e1;
	}
	
	public int getNumberOfNodes() {
		return node.size();
	}
	
	public Node getNode(int id) {
		return node.get(id);
	}
	
	public int getNumberOfElements() {
		return element.size();
	}
	
	public Element getElement(int id) {
		return element.get(id);
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
		for(Element e : element) {
			System.out.print(ArrayFormat.format(count1));
			e.print();
			count1++;
		}
		
	}
	
	/*public void solve() {
		
		// size of our matrix
		int neq = enumerateDOFs();
		
		// create the solver object
		ILSESolver solver = new GeneralMatrixLSESolver();
		
		// info object for coefficient matrix
		QuadraticMatrixInfo aInfo = solver.getAInfo();
		
		// get coefficient matrix
		this.K_a = new Array2DMatrix(neq, neq);
		assembleStiffnessMatrix(K_a);
		IMatrix A = solver.getA();
		
		// right hand side
		this.rGlobal = new double[neq];
		assembleLoadVector(rGlobal);
		double[] b = rGlobal;
		
		// initialize solver
		aInfo.setSize(neq);
		solver.initialize();
		
		// set entries of matrix and right hand side
		
		for (int i = 0; i < neq ; i++) {
			for (int j = 0 ; j < neq ; j++) {
				A.set(i, j, K_a.get(i, j)); 
			}
		}
		
		// print
		System .out . println (" Solving K x = r");
		System .out . println (" Matrix K");
		System .out . println ( MatrixFormat.format(A));
		System .out . println (" Vector r");
		System .out . println ( ArrayFormat . format (b));
		// after calling solve , b contains the solution
		try {
			solver.solve (b);
		} 
		catch ( SolveFailedException e) {
			System .out . println ("Solve failed : " + e.getMessage ());
		}
		// print result
		System .out . println (" Solution x");
		System .out . println (ArrayFormat.format (b));
	}*/
	
	public void solve() {
		// size of our matrix
		int neq = enumerateDOFs();
		
		// get coefficient matrix
		this.K_a = new Array2DMatrix(neq, neq);
		assembleStiffnessMatrix(K_a);
		double[][] K = new double[neq][neq];
		for (int i = 0 ; i < neq ; i++) {
			for(int j = 0 ; j < neq ; j++) {
				K[i][j] = K_a.get(i, j);
			}
		}
		
		if(determinantOfMatrix(K, neq) != 0) {
		// Print matrix
		/*for (double[] x : K)
		{
		   for (double y : x)
		   {
		        System.out.print(y + " ");
		   }
		   System.out.println();
		}*/
		
		// right hand side
		this.rGlobal = new double[neq];
		assembleLoadVector(rGlobal);
		double[] r = new double[neq];
		r = rGlobal;
		
		//Print load vector
		/*for(double i : r) {
			System.out.println(ArrayFormat.format(i));
		}*/
		
		
		/** GaussianElimination **/
		for (int k = 0; k < neq; k++) 
        {
            /** find pivot row **/
            int max = k;
            for (int i = k + 1; i < neq; i++) 
                if (Math.abs(K[i][k]) > Math.abs(K[max][k])) 
                    max = i;
 
            /** swap row in A matrix **/    
            double[] temp = K[k]; 
            K[k] = K[max]; 
            K[max] = temp;
 
            /** swap corresponding values in constants matrix **/
            double t = r[k]; 
            r[k] = r[max]; 
            r[max] = t;
 
            /** pivot within A and B **/
            for (int i = k + 1; i < neq; i++) 
            {
                double factor = K[i][k] / K[k][k];
                r[i] -= factor * r[k];
                for (int j = k; j < neq; j++) 
                    K[i][j] -= factor * K[k][j];
            }
        }
		
		/** back substitution **/
		double[] x = new double[neq];
        for (int i = neq - 1; i >= 0; i--) 
        {
            double sum = 0.0;
            for (int j = i + 1; j < neq; j++) 
                sum += K[i][j] * x[j];
            x[i] = (r[i] - sum) / K[i][i];
        }
        // Print Displacement
        /*for(double i : x) {
			System.out.println(ArrayFormat.format(i));
		}*/
        
        this.uGlobal = x;
        
        //System .out . println (" Solving K x = r");
        // print result
        //System .out . println (" Solution x");
        //System .out . println ( ArrayFormat . format (x));
        selectDisplacements(uGlobal);
		}
		
		else {
			System.out.println("***The K Matrix is SINGULAR***");
		}
	}
	
	public double determinantOfMatrix(double mat[][], int n) 
    { 
        int D = 0; // Initialize result 
      
        // Base case : if matrix contains single 
        // element 
        if (n == 1) 
            return mat[0][0]; 
          
        // To store cofactors 
        double[][] temp = new double[n][n];  
          
        // To store sign multiplier 
        int sign = 1;  
      
        // Iterate for each element of first row 
        for (int f = 0; f < n; f++) 
        { 
            // Getting Cofactor of mat[0][f] 
            getCofactor(mat, temp, 0, f, n); 
            D += sign * mat[0][f]  
               * determinantOfMatrix(temp, n - 1); 
      
            // terms are to be added with  
            // alternate sign 
            sign = -sign; 
        } 
      
        return D; 
    }
	
	public void getCofactor(double mat[][],  
            double temp[][], int p, int q, int n) 
   { 
       int i = 0, j = 0; 
     
       // Looping for each element of  
       // the matrix 
       for (int row = 0; row < n; row++) 
       { 
           for (int col = 0; col < n; col++) 
           { 
                 
               // Copying into temporary matrix  
               // only those element which are  
               // not in given row and column 
               if (row != p && col != q) 
               { 
                   temp[i][j++] = mat[row][col]; 
     
                   // Row is filled, so increase  
                   // row index and reset col  
                   //index 
                   if (j == n - 1) 
                   { 
                       j = 0; 
                       i++; 
                   } 
               } 
           } 
       } 
   } 

	private int enumerateDOFs() {
		int max = 0;
		int eq = 0;
		for(Node i : node) {
			eq = i.enumerateDOFs(eq); 
		}
		for(Element i : element) {
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
	
	private void assembleLoadVector(double[] rGlobal) {
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
	
	private void assembleStiffnessMatrix(IMatrix kGlobal) {
		for (Element e : element) {
			IMatrix temp = e.computeStiffnessMatrix();
			for(int i : e.getDOFNumbers()) {
					if(i != -1) {
						for(int j : e.getDOFNumbers()) {
							if (j != -1) {
								kGlobal.add(i, j, temp.get(i, j));
							}
						}
					}
			}
		}
		//System.out.println(ArrayFormat.fFormat(kGlobal.toString()));
	}
	
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
		
		System.out.println();
		System.out.println("Listing analysis results");
		System .out . println (" Solving K x = r");
		System.out.println();
        // print result
        System .out . println (" Solution x in global system");
        System .out . println ( ArrayFormat . format (uGlobal));
        System.out.println();
        System.out.println("Displacements");
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
		
		System.out.println("Elememt Forces");
		System.out.println("  elem" + ArrayFormat.fFormat("force"));
		count1 = 0;
		for(Element e : element) {
			System.out.print(ArrayFormat.format(count1));
			System.out.print(ArrayFormat.format(e.computeForce()));
			count1++;
			System.out.println();
		}
	}

}
