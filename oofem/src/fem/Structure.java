package fem;

import iceb.jnumerics.*;
import iceb . jnumerics .lse .*;
import inf.text.*;

import java.util.*;


public class Structure {
	
	private ArrayList<Node> node = new ArrayList<Node>(); // Creating Array List
	private ArrayList<Element> element = new ArrayList<Element>(); // Creating Array List
	private IMatrix K_a;
	private double[] rGlobal;
	
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
	
	public void solve() {
		
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

	
	//private void selectDisplacements(double[] uGlobal) {}
	
	//public void printResults() {}

}
