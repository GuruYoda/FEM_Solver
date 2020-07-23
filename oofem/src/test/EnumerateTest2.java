package test ;
import fem . Structure ;
import inf . text . ArrayFormat ;
import models . SmallTetraeder ;
public class EnumerateTest2 {
public static void main ( String [] args ) {
	Structure struct = SmallTetraeder . createStructure ();
	// solve
	struct . solve ();
	// print equation numbers
	System .out . println (" Node degrees of freedom ");
	for (int i = 0; i < struct . getNumberOfNodes (); i ++) {
		int [] dofNumbers = struct . getNode (i). getDOFNumbers ();
		System . out . println ( ArrayFormat . format ( dofNumbers ));
	}
	System .out . println (" Element degrees of freedom ");
	for (int i = 0; i < struct . getNumberOfElements (); i++) {
		int [] dofNumbers = struct . getElement (i). getDOFNumbers ();
		System . out . println ( ArrayFormat . format ( dofNumbers ));
	}
	}
}