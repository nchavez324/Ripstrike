package project;

import Resources.ResourceLoader;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import project.controller.InputManager;
import project.controller.MouseManager;
import project.model.GameScene;
import project.model.IntroScene;
import project.model.MainScene;
import project.model.gfx.Background;
import project.model.util.Global;
import project.model.util.Vector2;
import project.view.ViewCore;
import project.view.ViewCore.Display;

public class Runner {

    public enum Scene {

        MAIN, INTRO
    }

    public boolean running;

    public Display mode;

    public GameScene scene;

    public MainScene main;

    public IntroScene intro;

    public InputManager controller;

    public ViewCore view;

    public Background std;

    String where;

    public Runner(Display mode, String where) throws IOException {
        Global.game = this;
        this.where = where;
        this.mode = mode;
        Global.depot = new ResourceLoader();// LOADING HAPPENS HERE!!
        intro = new IntroScene();
        main = new MainScene();
        scene = intro;
        view = new ViewCore(mode, intro);
        controller = new InputManager(intro, view.screenmanager.getWindow());
    }

    public void init() {
        running = true;
        intro.running = true;
        std = new Background(Global.depot.ui.get("stdbg"));
    }

    public void run() { // Starts program.
        // try {
        init();
        gameLoop(); // Starts Main game loop.
        // } finally {
        try {
            view.restoreScreen(); // Ends by closing window.
        } catch (Exception e) {
            Robot robot;
            try {
                robot = new Robot();
                robot.keyPress(KeyEvent.VK_ESCAPE);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
        }

        System.exit(0);
        // }
    }

    public void gameLoop() {

        long startTime = System.currentTimeMillis(); // This is the time the program started at, in milliseconds since
                                                     // some date in 1970. Idk why.
        long cumTime = startTime; // This is startTime, except it changes in real time, while startTime remains
                                  // constant.

        while (running) { // This lets the Main loop run.
            long timePassed = System.currentTimeMillis() - cumTime; // This is the time that has passed between now and
                                                                    // the last time the method was called.
            // Basically it tells you how much time has passed in between cycles.
            cumTime += timePassed; // Updates cumTime to reflect real time.
            update(timePassed);
            handleDrag();
            if (!running) {
                break;
            }

            try {
                Thread.sleep(20); // Lets the computer take a short nap so that it doesn't overload. This is the only
                                  // thing I know how to use threads for. lol.
            } catch (Exception ex) {
                System.out.println("Sleep Fail");
            }
        }
    }

    public void switchScenes(Scene newScene) {

        if (scene.type != newScene) {
            scene.stop();
            for (int i = 0; i < 3; i++) {
                controller.mouseManager.mouseState[MouseManager.OLD_STATE][0] = false;
                controller.mouseManager.mouseState[MouseManager.CURRENT_STATE][0] = false;
            }

            if (newScene == Scene.MAIN) {
                scene = main;
                controller.switchScenes(main);
            } else if (newScene == Scene.INTRO) {
                scene = intro;
                controller.switchScenes(intro);
            }
            view.switchScene(scene);
            scene.running = true;
        }

    }

    public void update(long timePassed) {
        if (scene.type == Scene.MAIN) {
            main.update(timePassed);
        } else if (scene.type == Scene.INTRO) {
            intro.update(timePassed);
        }

        view.draw(std);

    }

    public void handleDrag() {/*
                              if (scene.type == Scene.ACTION && !action.announcements.isPaused) {
                              if (controller.mouseManager.currentState == MouseManager.MOUSE_PRESSED) {
                              controller.mouseManager.handleMouseMoved((int) (MouseInfo.getPointerInfo().getLocation().x - view.getWindow().getLocation().x),
                              (int) (MouseInfo.getPointerInfo().getLocation().y - view.getWindow().getLocation().y));
                              }
                              holding();
                              } else*/
        if (scene.type == Scene.MAIN) {
            if (controller.mouseManager.mouseState[MouseManager.CURRENT_STATE][MouseManager.LEFT_BUTTON]
                    || controller.mouseManager.mouseState[MouseManager.CURRENT_STATE][MouseManager.MOUSE_WHEEL]
                    || controller.mouseManager.mouseState[MouseManager.CURRENT_STATE][MouseManager.RIGHT_BUTTON]) {
                controller.mouseManager.handleMouseMoved(MouseInfo.getPointerInfo().getLocation().x
                        - view.getWindow().getLocation().x, MouseInfo.getPointerInfo().getLocation().y
                        - view.getWindow().getLocation().y);
            }
            holding();
        }

    }

    public void holding() {
        if (scene.type == Scene.MAIN) {
            if (controller.mouseManager.mouseState[MouseManager.CURRENT_STATE][MouseManager.LEFT_BUTTON]) {
                controller.mouseManager.mouseState[MouseManager.OLD_STATE][MouseManager.LEFT_BUTTON] = true;
                controller.mouseManager.mouseState[MouseManager.CURRENT_STATE][MouseManager.LEFT_BUTTON] = true;
                controller.mouseManager.hitButton(new Vector2((MouseInfo.getPointerInfo().getLocation().x - view
                        .getWindow().getLocation().x), (MouseInfo.getPointerInfo().getLocation().y - view.getWindow()
                        .getLocation().y)), main.ui.buttons, Scene.MAIN);
            }
        }
    }

    public void stop() {
        running = false;
    }

    public static void main(String[] args) throws IOException {
        Runner game = new Runner(Display.WINDOW, args[0]);
        game.run();
    }
}
