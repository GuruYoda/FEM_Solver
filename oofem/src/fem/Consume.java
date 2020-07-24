package fem;

import iceb.jnumerics.Vector3D;
import inf.v3d.obj.Cone;
import inf.v3d.obj.Extrusion;
import inf.v3d.obj.PolygonSet;
import inf.v3d.obj.Polyline;
import inf.v3d.view.Viewer;

public class Consume {
	
	public static void main(String[] args) {
		Constraint c = new Constraint(true , false , true);
		c.print();
		
		double[] n1 = {1,0,0};
		double[] n2 = {0,1,0};
		Vector3D v1 = new Vector3D(n1);
		Vector3D v2 = new Vector3D(n2);
		System.out.println(v1.angle(v2));
		System.out.println(v1.scalarProduct(v1));
		System.out.println(v1.dot(v1));
		
		Viewer v = new Viewer();
		 PolygonSet ps = new PolygonSet();
		 
		 ps.insertVertex(0, 0, 0, 0);
		 ps.insertVertex(1, 0, 0, 0);
		 ps.insertVertex(1, 1, 0, 0);
		 ps.insertVertex(0, 1, 0, 0);
		 ps.polygonComplete();
		 v.addObject3D(ps);
		 v.setVisible(true);
	}

}
