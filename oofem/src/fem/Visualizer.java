package fem;

import iceb.jnumerics.*;

import inf.text.*;

import inf.v3d.*;

import inf.v3d.obj.CylinderSet;

import inf.v3d.obj.*;


public class Visualizer {
	
	private double diaplacementScale , symbolScale = 1;
	private Structure struct;
	
	public Visualizer(Structure struct) {
		this.struct = struct;
	}
	
	public void drawElements() {
		CylinderSet cs = new CylinderSet();
		for(int i = 0 ; i < struct.getNumberOfElements() ; i++) {
			cs.addCylinder(struct.getElement(i).getNode1().getPosition().toArray(), 
						   struct.getElement(i).getNode2().getPosition().toArray() ,
						   struct.getElement(i).getArea()*symbolScale);
		}
	}
	
	public void drawConstraints() {
		for(int i = 0 ; i < struct.getNumberOfNodes() ; i++) {
			if(struct.getNode(i).getConstraint() != null) {
				
			}
		}
	}
	
	public void drawElementForces() {
		
	}
	
	public void drawDisplacements() {
		
	}

}
