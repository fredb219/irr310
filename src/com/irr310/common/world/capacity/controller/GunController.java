package com.irr310.common.world.capacity.controller;

import com.irr310.common.world.Component;
import com.irr310.common.world.capacity.WeaponCapacity;

public class GunController extends WeaponController{

    public GunController(Component component, WeaponCapacity capacity) {
        super(component, capacity);
    }

    @Override
    protected void shot(int barrel) {
        genericShot(barrel);
    }

}
