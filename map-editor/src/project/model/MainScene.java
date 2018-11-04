package project.model;

import Resources.ResourceLoader;
import project.Runner.Scene;
import project.model.gfx.core.Component;
import project.model.util.Global;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;

import project.model.maps.CollisionMap;
import project.model.maps.Grid;
import project.model.util.Vector2;
import project.model.gfx.ui.UI;

public class MainScene extends GameScene {

    public static final int GRID = 0;

    public static final int MAP = 1;

    public static final int UI = 2;

    public HashMap<Integer, Component> components;

    public ResourceLoader depot;

    public Grid grid;

    public CollisionMap map;

    public UI ui;

    public MainScene() {

        type = Scene.MAIN;
        running = true;
        components = new HashMap<Integer, Component>();
        initComponents();
    }

    public void initComponents() {

        Global.camera = new Rectangle(0, 0, Global.screenWidth, Global.screenHeight);
        depot = Global.depot;

        // map = Global.depot.maps.get("Test").collisionMap;
        // map.position = new Vector2(150, 242 - map.height * CollisionMap.SIZE + CollisionMap.SIZE);
        // Global.depot.loadedMap = map;
        grid = new Grid(new Vector2(-10, -14));
        ui = new UI();

        components.put(GRID, grid);
        components.put(MAP, map);
        components.put(UI, ui);

    }

    @Override
    public void draw(Graphics2D g) {

        Integer[] keys = components.keySet().toArray(new Integer[0]);
        for (int i = 0; i < keys.length; i++) {
            if (components.get(keys[i]) != null)
                components.get(keys[i]).draw(g);
        }
    }

    @Override
    public void update(long timePassed) {
        if (running) {
            Integer[] keys = components.keySet().toArray(new Integer[0]);
            for (int i = 0; i < keys.length; i++) {
                if (components.get(keys[i]) != null)
                    components.get(keys[i]).update(timePassed);
            }
        }

    }
}
