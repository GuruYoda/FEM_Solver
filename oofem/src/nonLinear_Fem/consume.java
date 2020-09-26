package nonLinear_Fem;

import fem.Constraint;
import fem.Force;
import fem.Node;
import fem.Structure;

public class consume {
	
	public static nonLinear_Structure createNLStructure () {
		nonLinear_Structure struct  = new nonLinear_Structure();
		double lb = 15.0;
		double r = 457.2 / 2000;
		double t = 10.0 / 1000;
		double a = Math.PI * ( Math . pow (r, 2) - Math .pow (r - t, 2));
		double e = 2.1e11 ;
		Constraint c1 = new Constraint (false , false , false );
		Constraint c2 = new Constraint (true , true , false );
		Force f = new Force (0, -20e3 , -100e5 );
		// create nodes
		Node n1 = struct . addNode (0.0 , 0.0 , lb * Math . sqrt (2.0 / 3.0));
		Node n2 = struct . addNode (0.0 , lb / Math . sqrt (3) , 0);
		Node n3 = struct . addNode (-lb / 2, -lb / Math . sqrt (12.0) , 0);
		Node n4 = struct . addNode (lb / 2, -lb / Math . sqrt (12.0) , 0);
		// apply BCs
		n1. setForce (f);
		n2. setConstraint (c1 );
		n3. setConstraint (c1 );
		n4. setConstraint (c2 );
		
		// create elements
		struct . addElement (e, a, 0, 1);
		struct . addElement (e, a, 0, 2);
		struct . addElement (e, a, 0, 3);
		struct . addElement (e, a, 1, 2);
		struct . addElement (e, a, 2, 3);
		struct . addElement (e, a, 3, 1);
		
		// return the new structure
		return struct ;
		}
	
	public static void main(String[] args) throws Exception {
		
		nonLinear_Structure struct = createNLStructure ();
		struct.printStructure();
		struct.newtonMethod(0.001, 0.1, 10, 2, 0.001);
		
		
	}

}
