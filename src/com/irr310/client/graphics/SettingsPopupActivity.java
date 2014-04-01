package com.irr310.client.graphics;

import com.irr310.client.graphics.ether.activities.SettingsActivity;
import com.irr310.client.graphics.ether.activities.StatusActivity.StatusActivityController;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
import com.irr310.i3d.view.View.OnMouseEventListener;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public class SettingsPopupActivity extends Activity {


    private Button settingsButton;
    private StatusActivityController mListener;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/status_settings_popup");
        setStackable(false);
        
        mListener = (StatusActivityController) bundle.getObject();
        
        Button quitButton = (Button) findViewById("quitButton@layout/status_settings_popup");
        Button settingsButton = (Button) findViewById("settingsButton@layout/status_settings_popup");
        
        
        quitButton.setOnMouseListener(new OnMouseEventListener() {
            
            @Override
            public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
                if(mouseEvent.getAction() == Action.MOUSE_PRESSED && mouseEvent.getButton() == 1) {
                mListener.onQuit();
                return true;
                }
                return false;
            }
        });
        
        settingsButton.setOnMouseListener(new OnMouseEventListener() {
            
            @Override
            public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
                if(mouseEvent.getAction() == Action.MOUSE_PRESSED && mouseEvent.getButton() == 1) {
                mListener.getControlledSurface().startActivity(new Intent(SettingsActivity.class));
                // Do not return true to exit the popup
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        
    }

}