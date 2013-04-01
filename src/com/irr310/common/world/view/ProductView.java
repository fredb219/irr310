package com.irr310.common.world.view;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;

@NetworkClass
public class ProductView {

    @NetworkField
    public long id;
    
    @NetworkField
    public String name;
}
