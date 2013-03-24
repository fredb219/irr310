package com.irr310.common.world.view;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkOptionalField;

@NetworkClass
public class PlayerView {

    @NetworkField
    public long id;

    @NetworkField
    public String login;

    @NetworkOptionalField
    public FactionView faction;
    
}
