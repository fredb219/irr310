package com.irr310.common.event;

import com.irr310.common.world.zone.Ship;

public class WorldShipRemovedEvent extends EngineEvent {

	final private Ship ship;

	public WorldShipRemovedEvent(Ship ship) {
		this.ship = ship;
	}

	@Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}

	public Ship getShip() {
		return ship;
	}

}
