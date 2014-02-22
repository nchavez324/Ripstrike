package project.model;

import Resources.ResourceLoader;
import project.Runner.Scene;
import project.model.gfx.core.Animation;
import project.model.gfx.core.Component;
import project.model.gfx.core.Sprite;
import project.model.util.Global;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;

public class IntroScene extends GameScene {

    public static final int OPENING = 0;
    public static final int PRESSENTER = 1;
    public HashMap<Integer, Component> components;
    public ResourceLoader depot;
    public Sprite pressEnter;
    public Sprite logo;

    public IntroScene() {

        type = Scene.INTRO;
        components = new HashMap<Integer, Component>();
        initComponents();
    }

    public void initComponents() {

        Global.camera = new Rectangle(0, 0, Global.screenWidth, Global.screenHeight);

        depot = Global.depot;
        logo = new Sprite(depot.ui.get("opening"));
        components.put(OPENING, logo);
        Animation anim = new Animation(depot.ui.get("PressEnter"), 2, 500);
        pressEnter = new Sprite(anim);
        pressEnter.position.X = (Global.screenWidth - pressEnter.getWidth()) / 2;
        pressEnter.position.Y = 475;
        pressEnter.hud = true;
        components.put(PRESSENTER, pressEnter);
    }

    @Override
    public void draw(Graphics2D g) {
        Integer[] keys = components.keySet().toArray(new Integer[0]);
        for (int i = 0; i < keys.length; i++) {
            components.get(keys[i]).draw(g);
        }
    }

    @Override
    public void update(long timePassed) {
        Integer[] keys = components.keySet().toArray(new Integer[0]);
        for (int i = 0; i < keys.length; i++) {
            components.get(keys[i]).update(timePassed);
        }
        if (logo.position.X + logo.getWidth() <= 0) {
            try {
                Global.game.switchScenes(Scene.MENU);
            } catch (Exception e) {
                System.out.println("Scene switch fail");
            }
        }
    }
}
