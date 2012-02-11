package com.irr310.client.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.irr310.client.GameClient;
import com.irr310.common.Game;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.KeyPressedEvent;
import com.irr310.common.event.KeyReleasedEvent;
import com.irr310.common.event.MinimizeWindowEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.server.Duration;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public class InputEngine extends FramerateEngine {

    private boolean dragging;
    private long[] pressTime;

    public InputEngine() {
        framerate = new Duration(15000000);
        dragging = false;
        pressTime = new long[10];
    }

    @Override
    protected void frame() {

        
        try { 
        
        while (Keyboard.next()) {
            if(interceptSpecialKeys()) {
                continue;
            }
            
            
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventCharacter() == 0) {
                    Game.getInstance().sendToAll(new KeyPressedEvent(Keyboard.getEventKey(), ""));
                } else {
                    Game.getInstance().sendToAll(new KeyPressedEvent(Keyboard.getEventKey(), Character.toString(Keyboard.getEventCharacter())));
                }

            } else {
                if (Keyboard.getEventCharacter() == 0) {
                    Game.getInstance().sendToAll(new KeyReleasedEvent(Keyboard.getEventKey(), ""));
                } else {
                    Game.getInstance().sendToAll(new KeyReleasedEvent(Keyboard.getEventKey(), Character.toString(Keyboard.getEventCharacter())));
                }
            }
        }

        while (Mouse.next()) {
          

            if (Mouse.getEventButton() == -1) {
                if (dragging) {
                    // Drag
                    GameClient.getInstance().onMouseEvent(new V3DMouseEvent(Action.MOUSE_DRAGGED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1));
                } else {
                    // Move
                    GameClient.getInstance().onMouseEvent(new V3DMouseEvent(Action.MOUSE_MOVED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1));
                }

            } else {
                if (Mouse.getEventButtonState()) {
                    // Pressed
                    dragging = true;
                    pressTime[Mouse.getEventButton()] = Mouse.getEventNanoseconds();
                    GameClient.getInstance().onMouseEvent(new V3DMouseEvent(Action.MOUSE_PRESSED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1));
                } else {
                    // Released
                    dragging = false;
                    GameClient.getInstance().onMouseEvent(new V3DMouseEvent(Action.MOUSE_RELEASED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1));
                    if( Mouse.getEventNanoseconds()  - pressTime[Mouse.getEventButton()] < 500000000 ) {
                        GameClient.getInstance().onMouseEvent(new V3DMouseEvent(Action.MOUSE_CLICKED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1));
                    }
                }

            }

        }
        
        } catch(IllegalStateException e) {
            System.err.println("IllegalStateException");
        }

    }

    private boolean interceptSpecialKeys() {
        if (Keyboard.getEventKeyState()) {
            if(Keyboard.getEventKey() == Keyboard.KEY_F4 && Keyboard.isKeyDown(Keyboard.KEY_LMENU)){
                System.out.println("Alt + F4 pressed !");
                Game.getInstance().sendToAll(new QuitGameEvent());
            }
            
            if(Keyboard.getEventKey() == Keyboard.KEY_TAB && Keyboard.isKeyDown(Keyboard.KEY_LMENU)){
                System.out.println("Alt + F4 pressed !");
                Game.getInstance().sendToAll(new MinimizeWindowEvent());
            }
            
        }
        return false;
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new InputEngineEventVisitor());
    }

    private final class InputEngineEventVisitor extends DefaultEngineEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping input engine");
            setRunning(false);
        }

        @Override
        public void visit(StartEngineEvent event) {
            pause(false);
        }

        @Override
        public void visit(PauseEngineEvent event) {
            pause(true);
        }

    }

    @Override
    protected void init() {
    }

    @Override
    protected void end() {
    }

}