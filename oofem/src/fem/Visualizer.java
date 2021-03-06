package fem;

import iceb.jnumerics.Vector3D;
import inf.v3d.obj.*;

import inf .v3d . view .* ;

public class Visualizer {
	
	private Structure struct;
	private Viewer view;
	private double dispalcementScale;  //= 3000;
	private double symbolScale; // = 3;
	private double forceSymbolScale;
	private double forceSymbolRadius;
	private double constraintSymbolScale;
	private double normalforceSymbolScale; // = 2;
	
	public Visualizer(Structure struct,Viewer view){
		this.struct=struct;
		this.view=view;
	}
	
	public void setDispalcementScale(double ds) {
		this.dispalcementScale=ds;
	}
	
	public void setSymbolScale(double ss) {
		this.symbolScale=ss;
	}
	
	public void setForceSymbolScale(double fss) {
		this.forceSymbolScale=fss;
	}
	
	public void setForceSymbolRadius(double fsr) {
		this.forceSymbolRadius=fsr;
	}
	
	public void setConstraintSymbolScale(double css) {
		this.constraintSymbolScale=css;
	}
	
	public void setNormalForceSymbolScale(double nfss) {
		this.normalforceSymbolScale=nfss;
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
			cs.setColor(150, 150, 150);
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
						
						if(j == 0) {
							c.setColor(150,150,50);
							c.setDirection(1, 0, 0);
							c.translate(-1, 0, 0);
						}
						else if(j == 1) {
							c.setColor(50,150,150);
							c.setDirection(0, 1, 0);
							c.translate(0, -1, 0);
						}
						else {
							c.setColor(150,50,150);
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
			cs.setColor(0, 0 , 250);
			view.addObject3D(cs);
		}
		
	}
	
	public void drawElementNormalForces() {
		double[] n1 = {1,0,0};
		double[] n2 = {0,1,0};
		Vector3D v1 = new Vector3D(n1);
		Vector3D v2 = new Vector3D(n2);
		Vector3D d , p , s1 , s2 , x1 , x2;
		
		for(int i = 0 ; i < struct.getNumberOfElements() ; i++) {
			x1 = struct.getElement(i).getNode1().getPosition().add(struct.getElement(i).getNode1().getDisplacement().multiply(dispalcementScale));
			x2 = struct.getElement(i).getNode2().getPosition().add(struct.getElement(i).getNode2().getDisplacement().multiply(dispalcementScale));
			d = struct.getElement(i).getNode1().getPosition().add(struct.getElement(i).getNode1().getDisplacement().multiply(dispalcementScale)).subtract(struct.getElement(i).getNode2().getPosition().add(struct.getElement(i).getNode2().getDisplacement().multiply(dispalcementScale))).normalize();
			if (v1.dot(d) != 1) {
				p = v1.vectorProduct(d);
				s1 = struct.getElement(i).getNode1().getPosition().add(struct.getElement(i).getNode1().getDisplacement().multiply(dispalcementScale)).add(d.vectorProduct(p).multiply(normalforceSymbolScale)); 
				s2 = struct.getElement(i).getNode2().getPosition().add(struct.getElement(i).getNode2().getDisplacement().multiply(dispalcementScale)).add(d.vectorProduct(p).multiply(normalforceSymbolScale));
				
				PolygonSet ps = new PolygonSet();
				ps.insertVertex(x1.get(0), x1.get(1), x1.get(2), 0);
				ps.insertVertex(x2.get(0), x2.get(1), x2.get(2), 0);
				ps.insertVertex(s2.get(0), s2.get(1), s2.get(2), 0);
				ps.insertVertex(s1.get(0), s1.get(1), s1.get(2), 0);
				ps.polygonComplete();
				if (Math.abs(struct.getElement(i).computeForce()) > 2E4) {
					ps.setColor(250, 0 , 0);
				}
				else {
					ps.setColor(255, 255 , 0);
				}
				view.addObject3D(ps);
			}
			else {
				p = v2.vectorProduct(d);
				s1 = struct.getElement(i).getNode1().getPosition().add(struct.getElement(i).getNode1().getDisplacement().multiply(dispalcementScale)).add(d.vectorProduct(p).multiply(normalforceSymbolScale)); 
				s2 = struct.getElement(i).getNode2().getPosition().add(struct.getElement(i).getNode2().getDisplacement().multiply(dispalcementScale)).add(d.vectorProduct(p).multiply(normalforceSymbolScale));
				PolygonSet ps = new PolygonSet();
				ps.insertVertex(x1.get(0), x1.get(1), x1.get(2), 0);
				ps.insertVertex(x2.get(0), x2.get(1), x2.get(2), 0);
				ps.insertVertex(s2.get(0), s2.get(1), s2.get(2), 0);
				ps.insertVertex(s1.get(0), s1.get(1), s1.get(2), 0);
				ps.polygonComplete();
				if (Math.abs(struct.getElement(i).computeForce()) > 2E4) {
					ps.setColor(250, 0 , 0);
				}
				else {
					ps.setColor(255, 255 , 0);
				}
				view.addObject3D(ps);
			}
		}
	}
	
}
