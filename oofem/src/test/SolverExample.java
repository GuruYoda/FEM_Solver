package test;

import iceb . jnumerics .*;
import org.ejml.data.SingularMatrixException;
import org.ejml.simple.SimpleMatrix;
import org.ojalgo.*;
import iceb . jnumerics .lse .*;
import inf . text . ArrayFormat ;

public class SolverExample {
	
	public static void main(String[] args) throws Exception {
		int neq = 4;

		SimpleMatrix a = new SimpleMatrix(neq, neq);

		SimpleMatrix b = new SimpleMatrix(neq, 1);

		a.set(0, 0, neq + 1);
		b.set(0, 2 * neq);
		for (int i = 1; i < neq; i++) {
			a.set(0, i, 1);
			a.set(i, 0, 1);
			a.set(i, i, 1);
			b.set(i, 2);
		}

		System.out.println("Matrix a: " + a);
		System.out.println("Vector b: " + b);
		
		try {
			SimpleMatrix x = a.solve(b);
			System.out.println("Solution vector x: " + x);
		} catch (SingularMatrixException e) {
			throw new Exception("Singular matrix");
		}
	}

}
