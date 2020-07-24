package fem;

import iceb.jnumerics.Vector3D;
import inf.v3d.obj.*;

import inf .v3d . view .* ;

public class Visualizer {
	private double dispalcementScale = 3000;
	private double symbolScale = 3;
	private Structure struct;
	private Viewer view;
	private double forceSymbolScale;
	private double forceSymbolRadius;
	private double constraintSymbolScale;
	
	public Visualizer(Structure struct,Viewer view){
		this.struct=struct;
		this.view=view;
	}
	
	public void setForceSymbolScale(double s) {
		this.forceSymbolScale=s;
	}
	
	public void setForceSymbolRadius(double r) {
		this.forceSymbolRadius=r;
	}
	
	public void setConstraintSymbolScale(double s) {
		this.forceSymbolScale=s;
	}
	
	public void drawNodes() {
		for(int i = 0 ; i < struct.getNumberOfNodes() ; i++) {
			Sphere s=new Sphere(struct.getNode(i).getPosition().toArray());
			s.setRadius(0.1 * symbolScale);
			s.setColor(0,0,0);
			view.addObject3D(s);
		}
	}
	
	public void drawElements() {
		CylinderSet cs = new CylinderSet();
		for(int i=0;i<struct.getNumberOfElements();i++) {
			cs.addCylinder(struct.getElement(i).getNode1().getPosition().toArray(), 
					struct.getElement(i).getNode2().getPosition().toArray(),
					Math.sqrt(struct.getElement(i).getArea()/Math.PI)*symbolScale);
			cs.setColor(0, 0, 255);
		}
		view.addObject3D(cs);
	} 
	
	public void drawConstraints() {
		for(int i = 0 ; i < struct.getNumberOfNodes() ; i++) {
			if(struct.getNode(i).getConstraint() != null) {
				for(int j = 0 ; j < struct.getNode(i).getPosition().getSize() ; j++) {
					if(struct.getNode(i).getDOFNumbers()[j] == -1) {
						Cone c = new Cone(struct.getNode(i).getPosition().toArray()[0],
								          struct.getNode(i).getPosition().toArray()[1],
								          struct.getNode(i).getPosition().toArray()[2]);
						c.setColor(128,128,128);
						if(j == 0) {
							c.setDirection(1, 0, 0);
							c.translate(-1, 0, 0);
						}
						else if(j == 1) {
							c.setDirection(0, 1, 0);
							c.translate(0, -1, 0);
						}
						else {
							c.setDirection(0, 0, 1);
							c.translate(0, 0, -1);
						}
						view.addObject3D(c);
					}
				}
			}
		}
	}
	
	public void drawElementForces() {
		for(int i = 0 ; i < struct.getNumberOfNodes() ; i++) {
			if(struct.getNode(i).getForce()!=null) {
					Arrow a=new Arrow();
					a.setRadius(forceSymbolRadius);
					a.setColor(255,0,0);
					a.setPoint2(struct.getNode(i).getPosition().toArray());
					a.setPoint1(struct.getNode(i).getPosition().toArray()[0]-struct.getNode(i).getForce().getComponent(0)*forceSymbolScale, 
							struct.getNode(i).getPosition().toArray()[1]-struct.getNode(i).getForce().getComponent(1)*forceSymbolScale, 
							struct.getNode(i).getPosition().toArray()[2]-struct.getNode(i).getForce().getComponent(2)*forceSymbolScale);
					view.addObject3D(a);
			}
		}
	}
	
	public void drawDisplacements() {
		Vector3D Scaled_Disp_n1;
		Vector3D Scaled_Disp_n2;
		for(int i = 0 ; i < struct.getNumberOfElements() ; i++) {
			Scaled_Disp_n1 = struct.getElement(i).getNode1().getPosition().add(struct.getElement(i).getNode1().getDisplacement().multiply(dispalcementScale));
			Scaled_Disp_n2 = struct.getElement(i).getNode2().getPosition().add(struct.getElement(i).getNode2().getDisplacement().multiply(dispalcementScale));
			Sphere s1=new Sphere();
			s1.setCenter(Scaled_Disp_n1.toArray());
			s1.setRadius(0.1 * symbolScale);
			s1.setColor(0,60,0);
			view.addObject3D(s1);
			Sphere s2=new Sphere();
			s2.setCenter(Scaled_Disp_n2.toArray());
			s2.setRadius(0.1 * symbolScale);
			s2.setColor(0,60,0);
			view.addObject3D(s2);
			CylinderSet cs = new CylinderSet();
			cs.addCylinder(Scaled_Disp_n1.toArray(), Scaled_Disp_n2.toArray(), Math.sqrt(struct.getElement(i).getArea()/Math.PI)*symbolScale);
			cs.setColor(0, 100, 0);
			view.addObject3D(cs);
		}
		
	}
	
}
