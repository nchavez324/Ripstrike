package project.controller;

import project.Runner.Scene;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import project.model.GameScene;
import project.model.IntroScene;
import project.model.MainScene;
import project.model.gfx.ui.Button;
import project.model.gfx.ui.Cursor;
import project.model.maps.CollisionTile.Type;
import project.model.util.Global;
import project.model.util.Vector2;

public class MouseManager {

    private GameScene engine;

    private MainScene mainengine;

    @SuppressWarnings("unused")
    private IntroScene introengine;

    public static final int OLD_STATE = 0;

    public static final int CURRENT_STATE = 1;

    public static final int LEFT_BUTTON = 0;

    public static final int MOUSE_WHEEL = 1;

    public static final int RIGHT_BUTTON = 2;

    public boolean mouseState[][];

    public MouseManager(GameScene engine) {
        this.engine = engine;
        mouseState = new boolean[2][3];

        if (engine.type == Scene.MAIN) {
            mainengine = (MainScene) engine;
        } else if (engine.type == Scene.INTRO) {
            introengine = (IntroScene) engine;
        }
    }

    public void switchScenes(GameScene engine) {
        this.engine = engine;
        if (engine.type == Scene.MAIN) {
            mainengine = (MainScene) engine;
        } else if (engine.type == Scene.INTRO) {
            introengine = (IntroScene) engine;
        }
    }

    public void handleMouseMoved(int x, int y) {
        if (engine.type == Scene.MAIN) {
            Cursor p = (Cursor) mainengine.ui.cursor;
            p.position.X = x;
            p.position.Y = y;
            buttonMove(mainengine.ui.buttons, x, y);
        }
    }

    public void handleMouseClicked(MouseEvent e) {
        e.consume();
    }

    public void handleMousePressed(MouseEvent e) {
        mouseState[OLD_STATE][e.getButton() - 1] = mouseState[CURRENT_STATE][e.getButton() - 1];
        mouseState[CURRENT_STATE][e.getButton() - 1] = true;
        if (e.getButton() - 1 == LEFT_BUTTON) {
            if (engine.type == Scene.MAIN) {
                mainengine.ui.cursor.down = true;
                mainengine.ui.place(e.getX(), e.getY());
            }
        }
        e.consume();
    }

    public void handleMouseReleased(MouseEvent e) {
        mouseState[OLD_STATE][e.getButton() - 1] = mouseState[CURRENT_STATE][e.getButton() - 1];
        mouseState[CURRENT_STATE][e.getButton() - 1] = false;
        if (e.getButton() - 1 == LEFT_BUTTON) {
            if (engine.type == Scene.MAIN) {
                mainengine.ui.cursor.down = false;
                buttonRelease(mainengine.ui.buttons, e.getX(), e.getY());
            }
        } else if (e.getButton() - 1 == RIGHT_BUTTON) {
            if (engine.type == Scene.MAIN) {
                //
            }
        }
        e.consume();
    }

    public void hitButton(Vector2 mousePos, HashMap<String, Button> buttons, Scene scene) {
        String[] keys = buttons.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            Button b = buttons.get(keys[i]);
            if (b.enabled) {
                if (mousePos.X >= b.position.X && mousePos.X <= b.position.X + b.getWidth()
                        && mousePos.Y >= b.position.Y && mousePos.Y <= b.position.Y + b.getHeight()) {
                    b.state = Button.DEPRESSED;

                    if (b.msg.compareTo("Exit") == 0) {
                        Global.game.stop();
                    }
                }
            }
        }

    }

    public void buttonRelease(HashMap<String, Button> buttons, int x, int y) {
        String[] keys = buttons.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            Button b = buttons.get(keys[i]);
            if (x >= b.position.X && x <= b.position.X + b.getWidth() && y >= b.position.Y
                    && y <= b.position.Y + b.getHeight()) {
                b.state = Button.HOVERSELECTED;

                if (b.msg.compareTo("Block") == 0) {
                    mainengine.ui.currentType = Type.BLOCK;
                }
                if (b.msg.compareTo("Empty") == 0) {
                    mainengine.ui.currentType = Type.EMPTY;
                }
                if (b.msg.compareTo("Slope") == 0) {
                    mainengine.ui.currentType = Type.SLOPE;
                }
                if (b.msg.compareTo("Open...") == 0) {
                    mainengine.ui.openFile();
                }
                if (b.msg.compareTo("Save...") == 0) {
                    mainengine.ui.saveFile();
                }
                if (b.msg.compareTo("Export...") == 0) {
                    mainengine.ui.export();
                }

            } else {
                b.state = Button.UNPRESSED;
            }
        }
    }

    public void buttonMove(HashMap<String, Button> buttons, int x, int y) {
        String[] keys = buttons.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            Button b = buttons.get(keys[i]);
            if (x >= b.position.X && x <= b.position.X + b.getWidth() && y >= b.position.Y
                    && y <= b.position.Y + b.getHeight()) {
                b.state = Button.HOVERSELECTED;
            } else {
                b.state = Button.UNPRESSED;
            }
        }
    }
}
