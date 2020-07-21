package fem;

import iceb.jnumerics.*;

import inf.text.*;

import java.util.*;


public class Structure {
	
	private ArrayList<Node> node = new ArrayList<Node>(); // Creating Array List
	private ArrayList<Element> element = new ArrayList<Element>(); // Creating Array List
	
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
	
	public int NumberOfElements() {
		return element.size();
	}
	
	public Element getElement(int id) {
		return element.get(id);
	}
	
	public void printStructure() {
		System.out.println("Listing Structure \n");
		System.out.println("Nodes");
		System.out.println("idx \t " + "  x1 \t \t" + " x2 \t \t" + "x3");
		int count1 = 0;
		for(Node n : node) {
			System.out.print(count1);
			n.print();
			count1++;
		}
		System.out.println("Constraints");
		System.out.println("node \t " + "  u1 \t \t" + " u2 \t \t" + "u3");
		count1 = 0;
		for(Node n : node) {
			
			if (n.getConstraint() != null) {
				System.out.print(count1);
				n.getConstraint().print();
			}
			count1++;
		}
		System.out.println("Forces");
		System.out.println("node \t " + "  r1 \t \t" + " r2 \t \t" + "r3");
		count1 = 0;
		for(Node n : node) {
			if (n.getForce() != null) {
				System.out.print(count1);
				n.getForce().print();
			}
			count1++;
		}
		System.out.println("Elements");
		count1 = 0;
		System.out.println("idx \t " + "  E \t \t" + " A \t  " + "\tlength");
		for(Element e : element) {
			System.out.print(count1);
			e.print();
			count1++;
		}
	}
	
	//public void solve() {}
	
	//private int enumerateDOFs() {}
	
	//private void assembleLoadVector(double[] rGlobal) {}
	
	//private void assembleStiffnessMatrix(IMatrix kGlobal) {}
	
	//private void selectDisplacements(double[] uGlobal) {}
	
	//public void printResults() {}

}
