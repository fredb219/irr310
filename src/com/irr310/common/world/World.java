package com.irr310.common.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.Game;
import com.irr310.common.event.PlayerAddedEvent;
import com.irr310.common.event.WorldObjectAddedEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.tools.Vect3;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.view.ComponentView;
import com.irr310.common.world.view.PartView;
import com.irr310.common.world.view.PlayerView;
import com.irr310.common.world.view.ShipView;

public class World {

    private final List<WorldObject> objects;
    private final List<Ship> ships;
    private final List<Player> players;
    private final List<Part> parts;
    private final List<Part> myParts;
    private final Map<Long, Player> playerIdMap;
    private final Map<Long, Ship> shipIdMap;
    private final Map<Long, Capacity> capacityIdMap;
    private final Map<Long, Component> componentIdMap;
    private final Map<Long, Slot> slotIdMap;
    private final Map<Long, Part> partIdMap;

    public World() {
        objects = new ArrayList<WorldObject>();
        ships = new ArrayList<Ship>();
        players = new ArrayList<Player>();
        parts = new ArrayList<Part>();
        myParts = new ArrayList<Part>();
        playerIdMap = new HashMap<Long, Player>();
        shipIdMap = new HashMap<Long, Ship>();
        capacityIdMap = new HashMap<Long, Capacity>();
        slotIdMap = new HashMap<Long, Slot>();
        componentIdMap = new HashMap<Long, Component>();
        partIdMap = new HashMap<Long, Part>();
    }

    public void addObject(WorldObject o) {
        objects.add(o);
        Game.getInstance().sendToAll(new WorldObjectAddedEvent(o));
    }

    public void addComponent(Component component) {
        componentIdMap.put(component.getId(), component);
        List<Capacity> capacities = component.getCapacities();
        for (Capacity capacity : capacities) {
            capacityIdMap.put(capacity.getId(), capacity);
        }
    }

    public void addPart(Part part) {
        partIdMap.put(part.getId(), part);
        parts.add(part);
        if(part.getOwner() == LoginManager.localPlayer) {
            myParts.add(part);
        }
    }

    private void addPlayer(Player player) {
        players.add(player);
        playerIdMap.put(player.getId(), player);
        Game.getInstance().sendToAll(new PlayerAddedEvent(player));
    }

    public void addShip(Ship ship, Vect3 position) {
        ships.add(ship);
        shipIdMap.put(ship.getId(), ship);
        Game.getInstance().sendToAll(new WorldShipAddedEvent(ship, position));
    }

    public void addSlot(Slot slot) {
        slotIdMap.put(slot.getId(), slot);
    }

    public Player loadPlayer(PlayerView playerView) {
        if (playerIdMap.containsKey(playerView.id)) {
            return playerIdMap.get(playerView.id);
        }

        Player player = new Player(playerView.id, playerView.login);
        player.fromView(playerView);
        addPlayer(player);
        return player;
    }

    public Ship loadShip(ShipView shipView) {
        if (shipIdMap.containsKey(shipView.id)) {
            return shipIdMap.get(shipView.id);
        }

        Ship ship = new Ship(shipView.id);
        ship.fromView(shipView);
        addShip(ship, null);
        return ship;
    }

    public Slot getSlotById(long slotId) {
        return slotIdMap.get(slotId);
    }

    public Part getPartById(long partId) {
        return partIdMap.get(partId);
    }

    public Player getPlayerById(long playerId) {
        return playerIdMap.get(playerId);
    }

    public Ship getShipById(long shipId) {
        return shipIdMap.get(shipId);
    }

    public Component getComponentBy(long componentId) {
        return componentIdMap.get(componentId);
    }

    public Capacity getCapacityById(long capacityId) {
        return capacityIdMap.get(capacityId);
    }

    public Component loadComponent(ComponentView componentView) {
        if (componentIdMap.containsKey(componentView.id)) {
            return componentIdMap.get(componentView.id);
        }

        Component component = new Component(componentView.id, componentView.name);
        component.fromView(componentView);
        addComponent(component);
        return component;
    }

    public Part loadPart(PartView partView) {
        if (partIdMap.containsKey(partView.id)) {
            return partIdMap.get(partView.id);
        }

        Part part = new Part(partView.id);
        part.fromView(partView);
        addPart(part);
        return part;
    }

    public List<Part> getParts() {
        return parts;
    }

    public List<Part> getMyParts() {
        return myParts;
    }

}
