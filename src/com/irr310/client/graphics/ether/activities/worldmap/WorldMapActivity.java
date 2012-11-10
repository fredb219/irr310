package com.irr310.client.graphics.ether.activities.worldmap;

import java.util.List;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.world.Faction;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.ScrollView;
import com.irr310.server.Time;

public class WorldMapActivity extends Activity {

    
    private RelativeLayout map;
    private Faction faction;
    private ScrollView mapScrollView;
    private boolean firstUpdate = true;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/world_map");
        map = (RelativeLayout) findViewById("map@layout/world_map");
        mapScrollView = (ScrollView) findViewById("mapScrollView@layout/world_map");
        
        World world = (World) bundle.getObject();
        
        Player player = LoginManager.getLocalPlayer();
        
        faction = player.getFaction();
        List<WorldSystem> knownSystems = faction.getKnownSystems();
        
        for (WorldSystem system : world.getMap().getSystems()) {
            map.addChild(new SystemView(system));
        }
        
        
        
        
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }
    
    @Override
    public void onDestroy() {
    }

    @Override
    protected void onUpdate(Time absTime, Time gameTime) {
        if(firstUpdate ) {
            firstUpdate = false;
            WorldSystem homeSystem = faction.getHomeSystem();
            mapScrollView.setCenterScroll((float)homeSystem.getLocation().x, (float)homeSystem.getLocation().y);
//            mapScrollView.setCenterScroll(0,0);
        }
    }

}