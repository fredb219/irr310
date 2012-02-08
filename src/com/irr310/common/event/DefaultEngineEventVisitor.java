package com.irr310.common.event;

public abstract class DefaultEngineEventVisitor implements EngineEventVisitor {

    public void visit(QuitGameEvent event) {
    }

    public void visit(StartEngineEvent event) {
    }

    public void visit(InitEngineEvent event) {
    }

    public void visit(PauseEngineEvent event) {
    }

    public void visit(UseScriptEvent event) {
    }

    public void visit(AddWorldObjectEvent event) {
    }

    public void visit(WorldObjectAddedEvent event) {
    }

    @Override
    public void visit(AddShipEvent event) {
    }

    @Override
    public void visit(WorldShipAddedEvent event) {
    }

    @Override
    public void visit(NetworkEvent event) {
    }

    @Override
    public void visit(PlayerAddedEvent event) {
    }

    @Override
    public void visit(KeyPressedEvent event) {
    }

    @Override
    public void visit(KeyReleasedEvent event) {
    }

    @Override
    public void visit(PlayerLoggedEvent event) {
    }
    
}