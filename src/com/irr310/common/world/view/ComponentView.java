package com.irr310.common.world.view;

import java.util.ArrayList;
import java.util.List;


import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;
import com.irr310.common.network.NetworkOptionalField;
import com.irr310.common.tools.Vect3;
import com.irr310.common.world.Container;
import com.irr310.common.world.Part;
import com.irr310.common.world.Slot;
import com.irr310.common.world.capacity.KernelCapacity;

@NetworkClass
public class ComponentView {

    @NetworkField
    public long id;
    
    @NetworkField
    public String name;
    
    @NetworkListField(PartView.class)
    public List<PartView> parts = new ArrayList<PartView>();

    @NetworkListField(SlotView.class)
    public List<SlotView> slots = new ArrayList<SlotView>();
    
    @NetworkListField(CapacityView.class)
    public List<CapacityView> capacities = new ArrayList<CapacityView>();
    
    
    @NetworkField
    public Vect3 shipPosition;
    
    @NetworkField
    public Vect3 shipRotation;
    
    @NetworkField
    public double durabilityMax;
    
    @NetworkField
    public double durability;
    
    @NetworkField
    public double quality;
    
}