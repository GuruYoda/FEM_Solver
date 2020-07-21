package test;

import iceb.jnumerics.*;

import inf.text.*;

import fem.Constraint ;
import fem.Element ;
import fem.Force ;
import fem.Node ;


public class EnumerateTest1 {
	
	public static void main ( String [] args ) {
		int eqn = 0;
		Node n1 = new Node (0, 0, 0);
		Node n2 = new Node (1, 1, 0);
		Element e1 = new Element (1, 1, n1 , n2 );
		Constraint c1 = new Constraint (false , true , true );
		// set constraints
		n1. setConstraint (c1 );
		
		// enumerate nodal degrees of freedom
		eqn = n1. enumerateDOFs ( eqn );
		eqn = n2. enumerateDOFs ( eqn );
		// equation numbers of element DOFs
		e1.enumerateDOFs();
		// print
		System . out . println (" Number of equation : " + eqn );
		System . out . println (" Equation numbers of nodal DOFs ");
		System . out . println ( ArrayFormat.format (n1. getDOFNumbers ()));
		System . out . println ( ArrayFormat . format (n2. getDOFNumbers ()));
		System . out . println (" Equation numbers of element DOFs ");
		System . out . println ( ArrayFormat . format (e1.getDOFNumbers()));
		}

}
