package com.irr310.common.world.capacity.controller;

import com.irr310.common.world.Component;

public abstract class CapacityController {

    public abstract void update(double duration);
    
    public abstract Component getComponent();

}
