package models;

import iceb.jnumerics.*;
import inf.text.*;
import inf.v3d.view.Viewer;
import fem.Constraint ;
import fem.Element ;
import fem.Force ;
import fem.Node ;
import fem.Structure;
import fem.Visualizer;

public class TrussBridge {
	
	public static Structure createStructure () {
		Structure struct = new Structure ();
		double lb = 15.0;
		double r = 457.2 / 2000;
		double t = 10.0 / 1000;
		double a = Math.PI * ( Math . pow (r, 2) - Math .pow (r - t, 2));
		double e = 2.1e11 ;
		Constraint c1 = new Constraint (false , false , false );
		Constraint c2 = new Constraint (true , true , false );
		Force f = new Force (0, 0 , -100e3 );
		// create nodes
		Node n1 = struct . addNode (0.0 , 0.0 , 0.0);
		Node n2 = struct . addNode (3.0, 0.0, 0.0);
		Node n3 = struct . addNode (3.0, 4.0, 0.0);
		Node n4 = struct . addNode (6.0, 0.0, 0.0);
		Node n5 = struct . addNode (6.0, 4.0, 0.0);
		Node n6 = struct . addNode (9.0, 0.0, 0.0);
		Node n7 = struct . addNode (9.0, 4.0, 0.0);
		Node n8 = struct . addNode (12.0, 0.0, 0.0);
		Node n9 = struct . addNode (12.0, 4.0, 0.0);
		Node n10 = struct . addNode (15.0, 0.0, 0.0);
		Node n11 = struct . addNode (15.0, 4.0, 0.0);
		Node n12 = struct . addNode (18.0, 0.0, 0.0);
		Node n13 = struct . addNode (18.0, 4.0, 0.0);
		Node n14 = struct . addNode (21.0, 0.0, 0.0);
		Node n15 = struct . addNode (21.0, 4.0, 0.0);
		Node n16 = struct . addNode (24.0, 0.0, 0.0);
		Node n17 = struct . addNode (0.0, 0.0, 10.0);
		Node n18 = struct . addNode (3.0, 0.0, 10.0);
		Node n19 = struct . addNode (3.0, 4.0, 10.0);
		Node n20 = struct . addNode (6.0, 0.0, 10.0);
		Node n21 = struct . addNode (6.0, 4.0, 10.0);
		Node n22 = struct . addNode (9.0, 0.0, 10.0);
		Node n23 = struct . addNode (9.0, 4.0, 10.0);
		Node n24 = struct . addNode (12.0, 0.0, 10.0);
		Node n25 = struct . addNode (12.0, 4.0, 10.0);
		Node n26 = struct . addNode (15.0, 0.0, 10.0);
		Node n27 = struct . addNode (15.0, 4.0, 10.0);
		Node n28 = struct . addNode (18.0, 0.0, 10.0);
		Node n29 = struct . addNode (18.0, 4.0, 10.0);
		Node n30 = struct . addNode (21.0, 0.0, 10.0);
		Node n31 = struct . addNode (21.0, 4.0, 10.0);
		Node n32 = struct . addNode (24.0, 0.0, 10.0);
		
		// apply BCs
		n8. setForce (f);
		n24.setForce(f);
		
		n1. setConstraint (c1 );
		n16. setConstraint (c1 );
		n17. setConstraint (c1 );
		n32.setConstraint(c2);
		
		// create elements
		struct . addElement (e, a, 0, 1);
		struct . addElement (e, a, 0, 2);
		struct . addElement (e, a, 1, 2);
		struct . addElement (e, a, 1, 3);
		struct . addElement (e, a, 2, 3);
		struct . addElement (e, a, 2, 4);
		struct . addElement (e, a, 3, 4);
		struct . addElement (e, a, 3, 5);
		struct . addElement (e, a, 4, 5);
		struct . addElement (e, a, 4, 6);
		struct . addElement (e, a, 5, 6);
		struct . addElement (e, a, 5, 7);
		struct . addElement (e, a, 6, 7);
		struct . addElement (e, a, 6, 8);
		struct . addElement (e, a, 7, 8);
		struct . addElement (e, a, 7, 9);
		struct . addElement (e, a, 7, 10);
		struct . addElement (e, a, 8, 10);
		struct . addElement (e, a, 9, 10);
		struct . addElement (e, a, 9, 11);
		struct . addElement (e, a, 9, 12);
		struct . addElement (e, a, 10, 12);
		struct . addElement (e, a, 11, 12);
		struct . addElement (e, a, 11, 13);
		struct . addElement (e, a, 11, 14);
		struct . addElement (e, a, 12, 14);
		struct . addElement (e, a, 12, 14);
		struct . addElement (e, a, 13, 15);
		struct . addElement (e, a, 14, 15);
		struct . addElement (e, a, 16, 17);
		struct . addElement (e, a, 16, 18);
		struct . addElement (e, a, 17, 18);
		struct . addElement (e, a, 17, 19);
		struct . addElement (e, a, 18, 19);
		struct . addElement (e, a, 18, 20);
		struct . addElement (e, a, 19, 20);
		struct . addElement (e, a, 19, 21);
		struct . addElement (e, a, 20, 21);
		struct . addElement (e, a, 20, 22);
		struct . addElement (e, a, 21, 22);
		struct . addElement (e, a, 21, 23);
		struct . addElement (e, a, 22, 23);
		struct . addElement (e, a, 22, 24);
		struct . addElement (e, a, 23, 24);
		struct . addElement (e, a, 23, 25);
		struct . addElement (e, a, 23, 26);
		struct . addElement (e, a, 24, 26);
		struct . addElement (e, a, 25, 26);
		struct . addElement (e, a, 25, 27);
		struct . addElement (e, a, 25, 28);
		struct . addElement (e, a, 26, 28);
		struct . addElement (e, a, 27, 28);
		struct . addElement (e, a, 27, 29);
		struct . addElement (e, a, 27, 30);
		struct . addElement (e, a, 28, 30);
		struct . addElement (e, a, 29, 30);
		struct . addElement (e, a, 29, 31);
		struct . addElement (e, a, 30, 31);
		struct . addElement (e, a, 0, 16);
		struct . addElement (e, a, 2, 18);
		struct . addElement (e, a, 1, 17);
		struct . addElement (e, a, 4, 20);
		struct . addElement (e, a, 3, 19);
		struct . addElement (e, a, 5, 21);
		struct . addElement (e, a, 6, 22);
		struct . addElement (e, a, 8, 24);
		struct . addElement (e, a, 7, 23);
		struct . addElement (e, a, 9, 25);
		struct . addElement (e, a, 10, 26);
		struct . addElement (e, a, 11, 27);
		struct . addElement (e, a, 12, 28);
		struct . addElement (e, a, 13, 29);
		struct . addElement (e, a, 14, 30);
		struct . addElement (e, a, 15, 31);
		
		// return the new structure
		return struct ;
		}
	
		public static void main ( String [] args ) throws Exception {
			
			Structure struct = createStructure ();
			struct.printStructure();
			//struct.solve();
			//struct.printResults();
			
			struct.solve1();
			Viewer viewer = new Viewer ();
			
			Visualizer viz = new Visualizer (struct , viewer );
			viz . setConstraintSymbolScale (1);
			viz . setForceSymbolScale (3e-5);
			viz . setForceSymbolRadius (0.075);
			viz . setNormalForceSymbolScale(2);
			viz . setSymbolScale(3);
			viz . setDispalcementScale(3000);
			viz . drawElements ();
			viz . drawConstraints ();
			viz . drawElementForces ();
			viz.drawNodes();
			viz.drawDisplacements();
			viz.drawElementNormalForces();
			viewer . setVisible ( true );
			
		}

}
