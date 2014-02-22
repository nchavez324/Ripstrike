package project.model;

import java.awt.Graphics2D;
import java.util.HashMap;

import project.Runner;
import project.Runner.Scene;
import project.model.gfx.core.Component;

public abstract class GameScene{
    
    public Runner game;
    public Scene type;
    public HashMap <Integer, Component> components;
    public boolean running;
    public int mspf = 100; //mspf stands for MiliSeconds Per Frame, which is how long a frame should last.
    
    public abstract void draw(Graphics2D g);
    public abstract void update(long timePassed);
    public void stop(){
        running = false;
    }
}

