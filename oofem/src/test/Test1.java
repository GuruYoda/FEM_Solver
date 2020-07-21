package test;

import fem.Constraint ;
import fem.Element ;
import fem.Force ;
import fem.Node ;

public class Test1 {
	
	public static void main ( String [] args ) {
		Constraint c1 = new Constraint (true , false , true );
		Force f1 = new Force (1.2 , -4, 0);
		Node n1 = new Node (1, 0, 0);
		Node n2 = new Node (0, 1, 0);
		Element e1 = new Element (2.1e8 , 0.2 , n1 , n2 );
		// set force and constraint
		n1. setConstraint (c1);
		n2. setForce (f1);
		// print
		System.out.println("Constraint (u1 , u2 , u3)");
		n1. getConstraint().print();
		System.out.println("Force (r1 , r2 , r3)");
		n2. getForce (). print ();
		System.out.println("Nodes (x1 , x2 , x3)");
		n1.print();
		n2.print();
		System.out.println(" Element (E, A, L)");
		e1.print();
		}

}
