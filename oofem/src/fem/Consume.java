package fem;

import iceb.jnumerics.Vector3D;

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
		
	}

}
