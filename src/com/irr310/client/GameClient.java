package com.irr310.client;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.UiEngineObserver;
import com.irr310.client.input.InputEngine;
import com.irr310.client.input.InputEngineObserver;
import com.irr310.common.engine.EngineManager;
import com.irr310.common.tools.Log;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.server.ParameterAnalyser;
import com.irr310.server.game.GameManager;

import fr.def.iss.vd2.lib_v3d.V3DControllerEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;

public class GameClient {
   
    private EngineManager mEngineManager;

    private InputEngine mInputEngine;

    private GameManager mGameManager;

    public static GameClient sInstance = null;

    public static GameClient getInstance() {
        return sInstance;
    }

    public GameClient(ParameterAnalyser parameterAnalyser) {
        // this.parameterAnalyser = parameterAnalyser;
        sInstance = this;
        // stillRunning = true;

        mGameManager = new GameManager();
        mEngineManager = new EngineManager();
        
        // clientNetworkEngine = new ClientNetworkEngine("127.0.0.10", 22310);
        // physicEngine = new PhysicEngine();
//        uiEngine = ;

        mInputEngine = new InputEngine();
        mInputEngine.getInputEnginObservable().register(this, new InputEngineObserver() {
            
            @Override
            public void onQuitEvent() {
                mEngineManager.stop();
            }
            
            @Override
            public void onMouseEvent(I3dMouseEvent event) {
            }
            
            @Override
            public void onKeyEvent(V3DKeyEvent event) {
            }

            @Override
            public void onControllerEvent(V3DControllerEvent event) {
            }
        });
        
        UiEngine uiEngine = new UiEngine();
        
        uiEngine.getUiEnginObservable().register(this, new UiEngineObserver() {
            
            @Override
            public void onQuitEvent() {
                mEngineManager.stop();
            }
        });
        
        
        mEngineManager.add(mInputEngine);
        
        mEngineManager.add(uiEngine);
        
        
        
        /*if(ClientConfig.sound_isEnabled()) {
            engineList.add(new SoundEngine());
        }*/
        
        
        // engineList.add(new ClientGameEngine());
        // engineList.add(physicEngine);
        // engineList.add(clientNetworkEngine);
//        engineList.add(uiEngine);
        
        
        // engineList.add(new ClientScriptEngine());

        //inputEngine = new InputEngine();
        // clientGameEngine = new ClientGameEngine();
        // scriptEngine = new ClientScriptEngine();

        // commandManager = new CommandManager();

    }

//    @Override
//    public World getWorld() {
//        return world;
//    }

    public void run() {

//        Log.perfBegin("Start");
//        Log.perfBegin("Start Engine");

        

//        Log.perfEnd(); // Start Engine
//        Log.perfBegin("Finish Start");
        

        java.lang.System.out.println("Irr310 Client - v0.1a");

        mEngineManager.run();
        
        // autologin();
        /*
         * LoginForm loginForm = new LoginForm();
         * loginForm.setLocationRelativeTo(loginForm.getParent());
         * loginForm.setVisible(true);
         */

        // Reader reader = new InputStreamReader(System.in);
        // BufferedReader input = new BufferedReader(reader);

        Log.perfEnd(); // Finish Start

        /*
         * try { while (true) { System.out.print("> "); String command =
         * input.readLine(); String output = commandManager.execute(command); if
         * (output != null) { if (!output.isEmpty()) {
         * System.out.println(output); } } else {
         * System.out.println("Game : Exiting..."); break; } } } catch
         * (IOException e) { // Todo handle exception }
         */
        
//        engineManager.waitStop();
        
        java.lang.System.out.println("Game Client: Stopped");

    }

    public void stop() {

    }

    public EngineManager getEngineManager() {
        return mEngineManager;
    }

    public InputEngine getInputEngine() {
        return mInputEngine;
    }

    public GameManager getGameManager() {
        return mGameManager;
    }


    /*private void autologin() {
        SignupTask signupTask = new SignupTask("default", "");
        signupTask.startAndWait();

        LoginTask loginTask = new LoginTask("default", "");
        loginTask.startAndWait();

    }*/

//    public void updateCapacityTask(com.irr310.common.world.capacity.Capacity capacity) {
//        if (clientNetworkEngine != null) {
//            clientNetworkEngine.send(new CapacityUpdateMessage(capacity.toView()));
//        }
//    }
//
//    public PhysicEngine getPhysicEngine() {
//        return physicEngine;
//    }
//
//    public ClientNetworkEngine getNetWorkEngine() {
//        return clientNetworkEngine;
//    }
//
//    public UiEngine getGraphicEngine() {
//        return uiEngine;
//    }

//    public World playSoloGame() {
//        engineManager.sendToAll(new LoadingGameEvent("Create new game ..."));


        
        
        // World Engine
//        WorldEngine worldEngine = new WorldEngine(null);
//        engineManager.startAndWait(worldEngine);
        // Physic
//        physicEngine = new PhysicEngine();
//        engineList.add(physicEngine);
//        worldEngineList.add(physicEngine);

        // ClientGame
//        ClientGameEngine clientGameEngine = new ClientGameEngine();
//        engineList.add(clientGameEngine);
//        worldEngineList.add(clientGameEngine);

        // ServerGameEngine
//        ServerGameEngine serverGameEngine = new ServerGameEngine();
//        engineList.add(serverGameEngine);
//        worldEngineList.add(serverGameEngine);
        
        // AIEngine
//        AIEngine aiEngine = new AIEngine();
//        engineList.add(aiEngine);
//        worldEngineList.add(aiEngine);

        

//        sendToAll(new StartEngineEvent());
                                                
        
        
//        Player player = createPlayer("player", "");
//        LoginManager.localPlayer = player;

//          engineManager.sendToAll(new CreatePlayerEvent());                                 
                                               
//        sendToAll(new WorldReadyEvent());
        
//        java.lang.System.out.println("Game begin");
        
        
//        engineManager.sendToAll(new CreatePlayerEvent("fredb219"));
        
//        Player player = createPlayer("player", "");
//        LoginManager.localPlayer = player;
        
//        return worldEngine.getWorld();
//    }

    

//    public void gameOver() {
//        java.lang.System.err.println("Game over");
//        
//        for (Engine engine : worldEngineList) {
//            engine.pushEvent(new QuitGameEvent());
//        }
//        
//        boolean waitStop = true;
//
//        while (waitStop) {
//            waitStop = false;
//            for (Engine engine : worldEngineList) {
//                if (!engine.isStopped() && !(engine instanceof ServerGameEngine) ) {
//                    waitStop = true;
//                    break;
//                }
//            }
//            Duration.HUNDRED_MILLISECONDE.sleep();
//        }
//        
//        for (Engine engine : worldEngineList) {
//            engineList.remove(engine);
//        }
//        
//        worldEngineList.clear();
//        physicEngine = null;
//        java.lang.System.err.println("Game cleaned");
//    }
    
    

}
