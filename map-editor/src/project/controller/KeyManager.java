package project.controller;

import project.Runner.Scene;

import java.awt.event.KeyEvent;

import project.model.GameScene;
import project.model.IntroScene;
import project.model.util.Global;
import project.model.MainScene;

public class KeyManager {

    public final int OLD_STATE = 0;

    public final int CURRENT_STATE = 1;

    public int offset;

    public GameScene engine;

    public MainScene mainengine;

    public IntroScene introengine;

    // In this aray, the index is accessed by the keyCode, and the values it
    // holds are
    // 0.CurrentState
    // 1.OldState
    // True is pressed
    // False is released
    public boolean[][] keyState;

    public KeyManager(GameScene engine) {
        this.engine = engine;
        if (engine.type == Scene.MAIN) {
            mainengine = (MainScene) engine;
        } else if (engine.type == Scene.INTRO) {
            introengine = (IntroScene) engine;
        }
        keyState = new boolean[600][2];
        offset = 90;
    }

    public void switchScenes(GameScene engine) {
        this.engine = engine;
        if (engine.type == Scene.MAIN) {
            mainengine = (MainScene) engine;
        } else if (engine.type == Scene.INTRO) {
            introengine = (IntroScene) engine;
        }
    }

    // What happens when a key is down.
    public void handleKeyPressed(int keyCode) {
        // Update the current and old states.

        keyState[keyCode][OLD_STATE] = keyState[keyCode][CURRENT_STATE];
        keyState[keyCode][CURRENT_STATE] = true;
        if (keyCode == KeyEvent.VK_TAB) {
            Global.minimizeScreen();
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            Global.game.stop();
        }
        if (engine.type == Scene.INTRO) {
            if (keyCode == KeyEvent.VK_ESCAPE) {
                Global.game.stop();
            }
        } else if (engine.type == Scene.MAIN) {
            if (keyCode == KeyEvent.VK_W) {
                Global.camera.y -= 8;
                mainengine.grid.shiftY(-8);
            }
            if (keyCode == KeyEvent.VK_S) {
                Global.camera.y += 8;
                mainengine.grid.shiftY(8);
            }
            if (keyCode == KeyEvent.VK_A) {
                Global.camera.x -= 8;
                mainengine.grid.shiftX(-8);
            }
            if (keyCode == KeyEvent.VK_D) {
                Global.camera.x += 8;
                mainengine.grid.shiftX(8);
            }
            if (keyCode == KeyEvent.VK_S) {
                if (keyState[KeyEvent.VK_CONTROL][CURRENT_STATE]) {
                    mainengine.ui.saveFile();
                }
            }
            if (keyCode == KeyEvent.VK_O) {
                if (keyState[KeyEvent.VK_CONTROL][CURRENT_STATE]) {
                    mainengine.ui.openFile();
                }
            }
        }
    }

    public void handleKeyReleased(int keyCode) {
        // Update the current and old states.
        keyState[keyCode][OLD_STATE] = keyState[keyCode][CURRENT_STATE];
        keyState[keyCode][CURRENT_STATE] = false;
        if (engine.type == Scene.INTRO) {
            if (keyCode == KeyEvent.VK_ENTER) {
                introengine.logo.velocity.X = -1.1f;
                introengine.pressEnter.velocity.X = -1.1f;
            }
        } else if (engine.type == Scene.MAIN) {
            if (keyCode == KeyEvent.VK_UP) {
                if (keyState[KeyEvent.VK_CONTROL][CURRENT_STATE]) {
                    mainengine.ui.map.shrinkY(-1);
                } else {
                    mainengine.ui.map.resizeY(-1);
                }
            }
            if (keyCode == KeyEvent.VK_DOWN) {
                if (keyState[KeyEvent.VK_CONTROL][CURRENT_STATE]) {
                    mainengine.ui.map.shrinkY(1);
                } else {
                    mainengine.ui.map.resizeY(1);
                }
            }
            if (keyCode == KeyEvent.VK_LEFT) {
                if (keyState[KeyEvent.VK_CONTROL][CURRENT_STATE]) {
                    mainengine.ui.map.shrinkX(-1);
                } else {
                    mainengine.ui.map.resizeX(-1);
                }
            }
            if (keyCode == KeyEvent.VK_RIGHT) {
                if (keyState[KeyEvent.VK_CONTROL][CURRENT_STATE]) {
                    mainengine.ui.map.shrinkX(1);
                } else {
                    mainengine.ui.map.resizeX(1);
                }
            }
        }
    }

    public boolean canMove() {
        return true;
    }
}
