package fem;

import iceb.jnumerics.Vector3D;
import inf.v3d.obj.Cone;
import inf.v3d.view.Viewer;

public class Consume {
	
	public static void main(String[] args) {
		Constraint c = new Constraint(true , false , true);
		c.print();
		
		Node n1 = new Node(1.0, 2.0 , 3.0);
		n1.getPosition();
		double[] u  = {1 , 2 , 3};
		n1.setDisplacement(u);
		double[] d = new double[3]; 
		d = n1.getDisplacement().toArray();
		for (double i : d) {
			System.out.println(i);
		}
		
		Viewer viewer = new Viewer ();
		Cone co = new Cone(0,0,0);
		co.setDirection(1, 1, 0);
		viewer.addObject3D(co);
		viewer . setVisible ( true );
		
	}

}
