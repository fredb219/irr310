package com.irr310.server.game.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.server.Vect3;


public class Component extends WorldObject {

	
	private double durabilityMax;
	private double durability;
	private double quality;
	private double efficiency;
	private Container container;
	private Vect3 shipPosition;
	private Vect3 shipRotation;
	List<Slot> slots;
	
	public Component() {
		slots = new ArrayList<Slot>();
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
	
	public Slot addSlot(Vect3 position) {
		Slot slot = new Slot(this, position);
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
	
	

	
	
}
