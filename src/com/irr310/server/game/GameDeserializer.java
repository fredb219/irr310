package com.irr310.server.game;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.RessourceLoadingException;
import com.irr310.common.tools.Vec2;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.*;
import com.irr310.common.world.item.ComponentItem;
import com.irr310.common.world.item.Item;
import com.irr310.common.world.item.ShipItem;
import com.irr310.common.world.system.Nexus;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Color;
import com.irr310.server.GameServer;
import com.irr310.server.world.product.ComponentProduct;
import com.irr310.server.world.product.Product;
import com.irr310.server.world.product.ShipProduct;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.irr310.common.world.ProductionTask.*;

/**
 * Created by fred on 04/05/14.
 */
public class GameDeserializer {

    private final Game mGame;
    private final File mSaveFilePath;
    private final DocumentBuilderFactory docBuilderFactory;
    private final World mWorld;
    private Map<Long, Player> mPlayerMap = new HashMap<Long, Player>();
    private Map<Long, Faction> mFactionMap = new HashMap<Long, Faction>();
    private Map<Long, WorldSystem> mSystemMap = new HashMap<Long, WorldSystem>();
    private Map<Long, Nexus> mNexusMap = new HashMap<Long, Nexus>();
    private Map<Long, Item> mItemMap = new HashMap<Long, Item>();

    private enum Pass {
        LINK_PASS, OBJECT_PASS

    }

    public GameDeserializer(Game game, String saveFilePath) {
        mGame = game;
        mWorld = game.getWorld();
        mSaveFilePath = new File(saveFilePath);
        docBuilderFactory = DocumentBuilderFactory.newInstance();
    }

    public void load() {
        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            Document doc;
            doc = docBuilder.parse(mSaveFilePath);

            Element root = doc.getDocumentElement();

            if (!root.getNodeName().equals("irr310")) {
                Log.warn("The file '" + mSaveFilePath.getAbsolutePath() + "' is not a irr310 save file");
                return;
            }

            parseRoot(root, Pass.OBJECT_PASS);
            parseRoot(root, Pass.LINK_PASS);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            Log.error("Failed to parse file '" + mSaveFilePath.getAbsolutePath(), e);
        } catch (IOException e) {
            Log.error("Failed to load file '" + mSaveFilePath.getAbsolutePath(), e);
        }


    }

    private void parseRoot(Element element, Pass pass) {
        NodeList childNodes = element.getChildNodes();
        // First pass create items
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // TODO error
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("world")) {
                parseWorld(subElement, pass);
            } else {
                Log.error("Unknown tag: " + subElement.getNodeName());
            }
        }
    }

    private void parseWorld(Element element, Pass pass) {
        if(pass == Pass.OBJECT_PASS) {
            GameServer.setNextId(getLongAttribute(element, "next-item-id"));
        }


        NodeList childNodes = element.getChildNodes();
        // First pass create items
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // TODO error
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("players")) {
                parsePlayers(subElement, pass);
            } else if (subElement.getNodeName().equals("factions")) {
                parseFactions(subElement, pass);
            } else if (subElement.getNodeName().equals("items")) {
                parseItems(subElement, pass);
            } else if (subElement.getNodeName().equals("map")) {
                parseMap(subElement, pass);
            } else {
                Log.error("Unknown tag: " + subElement.getNodeName());
            }
        }
    }

    private void parsePlayers(Element element, Pass pass) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("player")) {
                parsePlayer(subElement, pass);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag players element");
            }
        }
    }

    private void parseFactions(Element element, Pass pass) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("faction")) {
                parseFaction(subElement, pass);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag players element");
            }
        }
    }

    private void parseFaction(Element element, Pass pass) {
        Faction faction = null;
        switch(pass) {
            case OBJECT_PASS: {
                long id = getLongAttribute(element, "id");
                String login = element.getAttribute("login");
                Color color = getColorAttribute(element,"color");
                long koliumAmount = getLongAttribute(element, "kolium-amount");
                long neuridiumAmount = getLongAttribute(element, "neuridium-amount");
                long oresAmount = getLongAttribute(element, "ores-amount");
                long statersAmount = getLongAttribute(element, "staters-amount");

                faction = new Faction(mWorld, id);
                faction.getAvailableProductList().setProductManager(mWorld.getProductManager());
                faction.setColor(color);
                faction.setKoliumAmount(koliumAmount);
                faction.setNeuridiumAmount(neuridiumAmount);
                faction.setOresAmount(oresAmount);
                faction.setStatersAmount(statersAmount);

                mFactionMap.put(id, faction);
                mWorld.addFaction(faction);
            }
            break;
            case LINK_PASS: {
                long id = getLongAttribute(element, "id");
                long systemId = getLongAttribute(element, "home-system");
                long nexusId = getLongAttribute(element, "root-nexus");


                faction = mFactionMap.get(id);
                WorldSystem system = mSystemMap.get(systemId);
                Nexus nexus = mNexusMap.get(nexusId);

                faction.setHomeSystem(system);
                faction.setRootNexus(nexus);
            }
            break;
        }

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("players")) {
                parseFactionPlayers(subElement, pass, faction);
            } else if (subElement.getNodeName().equals("known-systems")) {
                 parseFactionKnownSystems(subElement, pass, faction);
            } else if (subElement.getNodeName().equals("ships")) {
                // TODO parse ships
            } else if (subElement.getNodeName().equals("production")) {
                parseFactionProduction(subElement, pass, faction);
                // TODO parse production
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag faction element");
            }
        }
    }

    private void parseFactionPlayers(Element element, Pass pass, Faction faction) {
        if(pass == Pass.LINK_PASS) {
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    // White space
                    continue;
                }
                Element subElement = (Element) node;
                if (subElement.getNodeName().equals("player")) {
                        long playerId = getLongAttribute(subElement, "id");
                        Player player = mPlayerMap.get(playerId);
                        faction.assignPlayer(player);
                } else {
                    throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag faction element");
                }
            }
        }
    }

    private void parseFactionProduction(Element element, Pass pass, Faction faction) {
        switch(pass) {
            case OBJECT_PASS: {
                long factoryCapacity = getLongAttribute(element, "factory-capacity");
                long factoryCapacityIncreaseRound = getLongAttribute(element, "next-factory-capacity-increase-round");
                faction.getProduction().setFactoryCapacity(factoryCapacity);
                faction.getProduction().setNextFactoryCapacityIncreaseRounds(factoryCapacityIncreaseRound);
            }
            break;
            case LINK_PASS: {
                if(element.hasAttribute("active-task")) {
                    long activeTaskId = getLongAttribute(element, "active-task");

                    for (ProductionTask productionTask : faction.getProduction().getProductionTaskQueue()) {
                        if (productionTask.getId() == activeTaskId) {
                            faction.getProduction().setActiveTask(productionTask);
                            break;
                        }
                    }
                }
            }
            break;
        }

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("next-factory-capacity-order")) {
                if(pass == Pass.OBJECT_PASS) {
                    FactionProduction.FactoryCapacityOrder factoryCapacityOrder = parseFactionProductionCapacityOrder(subElement);
                    faction.getProduction().setNextFactoryCapacityOrder(factoryCapacityOrder);
                }
            } else if (subElement.getNodeName().equals("factory-capacity-orders")) {
                if(pass == Pass.OBJECT_PASS) {
                    parseFactionProductionCapacityOrders(subElement, faction);
                }
            } else if (subElement.getNodeName().equals("factory-capacity-active-orders")) {
                if(pass == Pass.OBJECT_PASS) {
                    parseFactionProductionActiveCapacityOrders(subElement, faction);
                }
            } else if (subElement.getNodeName().equals("production-tasks")) {
                parseProductionTasks(subElement, pass, faction);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag faction element");
            }
        }

    }

    private void parseProductionTasks(Element element, Pass pass, Faction faction) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("production-task")) {
                parseProductionTask(subElement, pass, faction);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag faction element");
            }
        }
    }

    private void parseProductionTask(Element element, Pass pass, Faction faction) {
        if(pass == Pass.OBJECT_PASS) {
            long id = getLongAttribute(element, "id");
            Element rootWorkUnitElement = (Element) element.getElementsByTagName("root-work-unit").item(0);
            ProductionTask.BatchWorkUnit rootWorkUnit = (BatchWorkUnit) parseWorkUnit(rootWorkUnitElement, faction);
            ProductionTask productionTask = new ProductionTask(faction.getProduction(), id, rootWorkUnit.getProduct(), rootWorkUnit.getRequestedQuantity());
            faction.getProduction().getProductionTaskQueue().add(productionTask);
            productionTask.setRootWorkUnit(rootWorkUnit);
        } else {
            long id = getLongAttribute(element, "id");

            for (ProductionTask productionTask : faction.getProduction().getProductionTaskQueue()) {
                if(productionTask.getId() == id) {
                    Element rootWorkUnitElement = (Element) element.getElementsByTagName("root-work-unit").item(0);
                    updateBatchWorkUnit(rootWorkUnitElement, productionTask.getRootWorkUnit());
                    break;
                }
            }

        }
    }

    private void updateBatchWorkUnit(Element workUnitElement, BatchWorkUnit batchWorkUnit) {
        Element currentWorkUnitElement = (Element) workUnitElement.getElementsByTagName("current-work-unit").item(0);
        if(batchWorkUnit.getCurrentWorkUnit() instanceof BatchWorkUnit) {
            updateBatchWorkUnit(currentWorkUnitElement, (BatchWorkUnit) batchWorkUnit.getCurrentWorkUnit());
        } else if (batchWorkUnit.getCurrentWorkUnit() instanceof BuildWorkUnit) {
            updateBuildWorkUnit(currentWorkUnitElement, (BuildWorkUnit) batchWorkUnit.getCurrentWorkUnit());
        } else {
            throw new RessourceLoadingException("Unknown class '"+batchWorkUnit.getCurrentWorkUnit().getClass().getSimpleName());
        }

    }
    private void updateBuildWorkUnit(Element workUnitElement, BuildWorkUnit buildWorkUnit) {
        Element reservedItemsElement = (Element) workUnitElement.getElementsByTagName("reserved-items").item(0);
        Map<String, Item> reservedItems = new HashMap<String, Item>();
        buildWorkUnit.setReservedItems(reservedItems);

        Element subitemWorkUnitElement = (Element) workUnitElement.getElementsByTagName("subitem-work-unit").item(0);
        if(subitemWorkUnitElement != null) {
            updateBuildWorkUnit(subitemWorkUnitElement, buildWorkUnit.getSubItemWorkUnit());
        }

        NodeList childNodes = reservedItemsElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("reserved-item")) {
                long itemId = getLongAttribute(subElement, "item");
                String key = subElement.getAttribute("key");
                Item item = mItemMap.get(itemId);
                reservedItems.put(key, item);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag faction element");
            }
        }
    }

    private ProductionTask.WorkUnit parseWorkUnit(Element workUnitElement, Faction faction) {
        String type = workUnitElement.getAttribute("type");
        if(type.equals("batch")) {
            String productId = workUnitElement.getAttribute("product");
            long doneQuantity = getLongAttribute(workUnitElement, "done-quantity");
            long requestedQuantity = getLongAttribute(workUnitElement, "requested-quantity");
            Product product = mWorld.getProductManager().getProductById(productId);

            Element currentWorkUnitElement = (Element) workUnitElement.getElementsByTagName("current-work-unit").item(0);
            WorkUnit currentWorkUnit = parseWorkUnit(currentWorkUnitElement, faction);

            ProductionTask.BatchWorkUnit batchWorkUnit = new ProductionTask.BatchWorkUnit(faction.getProduction(), product, requestedQuantity);
            batchWorkUnit.setDoneQuantity(doneQuantity);
            batchWorkUnit.setCurrentWorkUnit(currentWorkUnit);

            return batchWorkUnit;
        } else if (type.equals("build")) {
            long accumulatedProductionCapacity = getLongAttribute(workUnitElement, "accumulated-production-capacity");
            long pendingOres = getLongAttribute(workUnitElement, "pending-ores");
            String productId = workUnitElement.getAttribute("product");
            WorkState workState = WorkState.valueOf(workUnitElement.getAttribute("work-state"));

            Product product = mWorld.getProductManager().getProductById(productId);


            BuildWorkUnit buildWorkUnit = new BuildWorkUnit(faction.getProduction(), product);
            buildWorkUnit.setAccumulatedProductionCapacity(accumulatedProductionCapacity);
            buildWorkUnit.setPendingOres(pendingOres);
            buildWorkUnit.setWorkState(workState);

            Element subitemWorkUnitElement = (Element) workUnitElement.getElementsByTagName("subitem-work-unit").item(0);
            if(subitemWorkUnitElement != null) {
                BuildWorkUnit subitemWorkUnit = (BuildWorkUnit) parseWorkUnit(subitemWorkUnitElement, faction);
                buildWorkUnit.setSubItemWorkUnit(subitemWorkUnit);
            }

            return buildWorkUnit;
        } else {
            throw new RessourceLoadingException("Unknown type '"+type+"'for work-unit element");
        }
    }

    private void parseFactionProductionCapacityOrders(Element element, Faction faction) {

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("factory-capacity")) {
                FactionProduction.FactoryCapacityOrder factoryCapacityOrder = parseFactionProductionCapacityOrder(subElement);
                faction.getProduction().getFactoryCapacityOrderList().add(factoryCapacityOrder);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag faction element");
            }
        }
    }

    private void parseFactionProductionActiveCapacityOrders(Element element, Faction faction) {

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("factory-capacity")) {
                FactionProduction.FactoryCapacityOrder factoryCapacityOrder = parseFactionProductionCapacityOrder(subElement);
                faction.getProduction().getFactoryCapacityActiveList().add(factoryCapacityOrder);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag faction element");
            }
        }
    }

    private FactionProduction.FactoryCapacityOrder parseFactionProductionCapacityOrder(Element element) {
        long count = getLongAttribute(element, "count");
        long sellPricePerCount = getLongAttribute(element, "sell-price-per-count");
        return new FactionProduction.FactoryCapacityOrder(sellPricePerCount, count);
    }

    private void parseFactionKnownSystems(Element element, Pass pass, Faction faction) {
        if(pass == Pass.LINK_PASS) {
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    // White space
                    continue;
                }
                Element subElement = (Element) node;
                if (subElement.getNodeName().equals("system")) {
                    long playerId = getLongAttribute(subElement, "id");
                    WorldSystem system = mSystemMap.get(playerId);
                    faction.discoverSystem(system);
                } else {
                    throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag faction element");
                }
            }
        }
    }

    private void parsePlayer(Element element, Pass pass) {
        switch(pass) {
            case OBJECT_PASS: {
                long id = getLongAttribute(element, "id");
                String login = element.getAttribute("login");
                boolean local = getBooleanAttribute(element, "local");
                boolean human = getBooleanAttribute(element, "human");

                Player player = new Player(mWorld, id, login);
                player.setLocal(local);
                player.setHuman(human);

                mPlayerMap.put(id, player);
                mWorld.addPlayer(player);
            }
                break;
            case LINK_PASS: {
                long id = getLongAttribute(element, "id");
                long factionId = getLongAttribute(element, "faction");

                Player player = mPlayerMap.get(id);
                Faction faction = mFactionMap.get(factionId);

                player.setFaction(faction);
            }
                break;
        }
    }

    private void parseItems(Element element, Pass pass) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("item")) {
                parseItem(subElement, pass);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag players element");
            }
        }
    }

    private void parseItem(Element element, Pass pass) {

        switch(pass) {
            case OBJECT_PASS: {

                Map<String, Item> subitems = new HashMap<String, Item>();

                if(element.getElementsByTagName("subitems").getLength() > 0) {
                    NodeList childNodes = element.getElementsByTagName("subitems").item(0).getChildNodes();
                    for (int i = 0; i < childNodes.getLength(); i++) {
                        Node node = childNodes.item(i);
                        if (node.getNodeType() != Node.ELEMENT_NODE) {
                            // White space
                            continue;
                        }
                        Element subElement = (Element) node;
                        if (subElement.getNodeName().equals("subitem")) {
                            String key = subElement.getAttribute("key");
                            long itemId = getLongAttribute(subElement, "item");
                            Item item = mItemMap.get(itemId);
                            subitems.put(key, item);
                        } else {
                            throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag subtems element");
                        }
                    }
                }

                long id = getLongAttribute(element, "id");
                String productId = element.getAttribute("product");
                Product product = mWorld.getProductManager().getProductById(productId);
                Item.ItemType type = Item.ItemType.valueOf(element.getAttribute("type"));
                Item.State state = Item.State.valueOf(element.getAttribute("state"));

                switch (type) {
                    case COMPONENT:

                        ComponentItem componentItem = new ComponentItem((ComponentProduct) product, mWorld, id, subitems);
                        componentItem.setState(state);
                        mItemMap.put(componentItem.getId(), componentItem);
                        break;
                    case SHIP:
                        ShipItem shipItem = new ShipItem((ShipProduct) product, mWorld, id, subitems);
                        shipItem.setState(state);
                        mItemMap.put(shipItem.getId(), shipItem);
                        break;

                }
            }
            break;
            case LINK_PASS: {
                    long id = getLongAttribute(element, "id");
                    long ownerId = getLongAttribute(element, "owner");
                    long usufructId = getLongAttribute(element, "usufruct");

                    Item item = mItemMap.get(id);
                    Faction owner = mFactionMap.get(ownerId);
                    Faction usufruct = mFactionMap.get(usufructId);

                    item.setOwner(owner);
                    item.setUsufruct(usufruct);
                    owner.getStocks().addItem(item);
            }
            break;
        }
    }


    private void parseMap(Element element, Pass pass) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("system")) {
                parseWorldSystem(subElement, pass);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag players element");
            }
        }
    }

    private void parseWorldSystem(Element element, Pass pass) {
        WorldSystem system = null;

        switch(pass) {
            case OBJECT_PASS: {
                long id = getLongAttribute(element, "id");
                String name = element.getAttribute("name");
                boolean homeSystem = getBooleanAttribute(element, "home-system");
                double radius = getDoubleAttribute(element, "radius");
                Vec2 location = getVec2Attribute(element, "location");

                system = new WorldSystem(mWorld, id, location);

                system.setName(name);
                system.setHomeSystem(homeSystem);
                system.setRadius(radius);

                mSystemMap.put(id, system);
                mWorld.getMap().addZone(system);
            }
            break;
            case LINK_PASS: {
                if(element.hasAttribute("owner")) {
                    long id = getLongAttribute(element, "id");
                    long factionId = getLongAttribute(element, "owner");

                    system = mSystemMap.get(id);
                    Faction faction = mFactionMap.get(factionId);

                    system.setOwner(faction);
                }
            }
            break;
        }


        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("nexuses")) {
                parseNexuses(subElement, pass, system);
            } else if (subElement.getNodeName().equals("ships")) {
                //TODO
            } else if (subElement.getNodeName().equals("parts")) {
                //TODO
            } else if (subElement.getNodeName().equals("slots")) {
                //TODO
            } else if (subElement.getNodeName().equals("components")) {
                //TODO
            } else if (subElement.getNodeName().equals("capacities")) {
                //TODO
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag system element");
            }
        }

    }

    private void parseNexuses(Element element, Pass pass, WorldSystem system) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("nexus")) {
                parseNexus(subElement, pass, system);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag nexuses element");
            }
        }
    }

    private void parseNexus(Element element, Pass pass, WorldSystem system) {
        switch(pass) {
            case OBJECT_PASS: {
                long id = getLongAttribute(element, "id");
                Vec3 location = getVec3Attribute(element, "location");
                double radius = getDoubleAttribute(element, "radius");

                Nexus nexus = new Nexus(system, id);
                nexus.setLocation(location);
                nexus.setRadius(radius);

                mNexusMap.put(id, nexus);
                system.addNexus(nexus);
            }
            break;
            case LINK_PASS: {
                long id = getLongAttribute(element, "id");
                long factionId = getLongAttribute(element, "owner");

                Nexus nexus = mNexusMap.get(id);
                Faction faction = mFactionMap.get(factionId);

                nexus.setOwner(faction);
            }
            break;
        }
    }

    private long getLongAttribute(Element element, String attribute) {
        return Long.parseLong(element.getAttribute(attribute));
    }

    private boolean getBooleanAttribute(Element element, String attribute) {
        return Boolean.parseBoolean(element.getAttribute(attribute));
    }

    private double getDoubleAttribute(Element element, String attribute) {
        return Double.parseDouble(element.getAttribute(attribute));
    }

    private Color getColorAttribute(Element element, String attribute) {
        return Color.parseColor(element.getAttribute(attribute));
    }

    private Vec2 getVec2Attribute(Element element, String attribute) {
        return Vec2.parseVec2(element.getAttribute(attribute));
    }

    private Vec3 getVec3Attribute(Element element, String attribute) {
        return Vec3.parseVec3(element.getAttribute(attribute));
    }
}