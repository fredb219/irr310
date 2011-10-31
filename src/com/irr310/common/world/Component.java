package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vect3;
import com.irr310.common.world.view.ComponentView;


public final class  Component extends WorldObject {

	
	private double durabilityMax;
	private double durability;
	private double quality;
	private double efficiency;
	private Container container;
	private Vect3 shipPosition;
	private Vect3 shipRotation;
	private List<Slot> slots;
	
	public Component(long id) {
	    super(id);
		slots = new ArrayList<Slot>();
		shipRotation = Vect3.origin();
		shipPosition = Vect3.origin();
	}
	
	public Slot getSlot(Vect3 position) {
		Slot minSlot = null;
		double minLenght = Double.MAX_VALUE;
		for(Slot slot: slots) {
			Double distanceTo = slot.getPosition().distanceTo(position);
			if(minLenght > distanceTo) {
				minLenght = distanceTo;
				minSlot = slot;
			}
		}
		return minSlot;
	}
	
	public Slot addSlot(long slotId, Part part, Vect3 position) {
		Slot slot = new Slot(slotId, this,part, position);
		slots.add(slot);
		return slot;
		
	}
	
	
	
	private void computeEfficiency() {
		double durabilityFactor = (((durability/ durabilityMax) -0.3)/0.7) -1;
		efficiency = quality*(1-durabilityFactor*durabilityFactor);
	}
	
	public double getEfficiency() {
		return efficiency;
	}

	public boolean isUnusable() {
		return efficiency <=0; 
	}

	public Container getContainer() {
		
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public void setShipPosition(Vect3 shipPosition) {
		this.shipPosition = shipPosition;
	}

	public void setShipRotation(Vect3 shipRotation) {
		this.shipRotation = shipRotation;
	}

	public Vect3 getShipPosition() {
		return shipPosition;
	}

	public Vect3 getShipRotation() {
		return shipRotation;
	}


    public void changeTranslation(Vect3 position) {
	    for(Part part: parts) {
	        part.getTransform().setTranslation(position);    
	    }
        
    }

    public void changeLinearSpeed(Vect3 linearSpeed) {
        for(Part part: parts) {
            part.getLinearSpeed().set(linearSpeed);
        }
    }

    public void changeRotationSpeed(Vect3 rotationSpeed) {
        for(Part part: parts) {
            part.getRotationSpeed().set(rotationSpeed);
        }
    }
	
	
	public Vect3 getLocalShipPosition(Vect3 absolutePosition) {

		TransformMatrix tmp = TransformMatrix.identity();
		
		tmp.setTranslation(shipPosition.negative());
		tmp.setTranslation(absolutePosition);
		
		
		tmp.rotateX(Math.toRadians(shipRotation.x));
		tmp.rotateY(Math.toRadians(shipRotation.y));
		tmp.rotateZ(Math.toRadians(shipRotation.z));
		
		return tmp.getTranslation();
	}

	public Vect3 getAbsoluteShipPosition(Vect3 position) {
		TransformMatrix tmp = TransformMatrix.identity();
		
		tmp.rotateX(Math.toRadians(shipRotation.x));
		tmp.rotateY(Math.toRadians(shipRotation.y));
		tmp.rotateZ(Math.toRadians(shipRotation.z));
		
		tmp.setTranslation(shipPosition);
		tmp.setTranslation(position);
		
		return tmp.getTranslation();
	}

    public ComponentView toView() {
        ComponentView componentView = new ComponentView();
        
        componentView.id = getId();
        componentView.shipPosition = shipPosition;
        componentView.shipRotation = shipRotation;
        for(Part part: parts) {
            componentView.parts.add(part.toView());    
        }
        
        for(Slot slot: slots) {
            componentView.slots.add(slot.toView());    
        }
        
        return componentView;
    }

    
	
}
