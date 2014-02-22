package project.controller;

import project.Runner.Scene;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import project.model.GameScene;
import project.model.IntroScene;
import project.model.MainScene;
import project.model.MenuScene;
import project.model.gfx.Grenade;
import project.model.gfx.Soldier.BodyParts;
import project.model.gfx.Soldier.Faction;
import project.model.gfx.Weapon;
import project.model.gfx.ui.Button;
import project.model.gfx.ui.Cursor;
import project.model.util.Global;
import project.model.util.Vector2;

public class MouseManager {

	private GameScene engine;

	private MainScene mainengine;

	@SuppressWarnings("unused")
	private IntroScene introengine;

	private MenuScene menuengine;

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
		} else if (engine.type == Scene.MENU) {
			menuengine = (MenuScene) engine;
		}
	}

	public void switchScenes(GameScene engine) {
		this.engine = engine;
		if (engine.type == Scene.MAIN) {
			mainengine = (MainScene) engine;
		} else if (engine.type == Scene.INTRO) {
			introengine = (IntroScene) engine;
		} else if (engine.type == Scene.MENU) {
			menuengine = (MenuScene) engine;
		}
	}

	public void handleMouseMoved(int x, int y) {
		if (engine.type == Scene.MENU) {
			Cursor p = (Cursor) menuengine.components.get(MenuScene.CURSOR);
			p.position.X = x;
			p.position.Y = y;
			if (menuengine.options.popup == null
					|| !menuengine.options.popup.visible) {
				if (menuengine.running) {
					menuengine.options.soldier.setTheta(new Vector2(x, y));
				}
				buttonMove(menuengine.options.buttons, x, y);
			}
		} else if (engine.type == Scene.MAIN) {
			Cursor p = (Cursor) mainengine.components.get(MainScene.CURSOR);
			p.position.X = x;
			p.position.Y = y;
			buttonMove(mainengine.ui.pausemenu.buttons, x, y);
			if (mainengine.ui.pausemenu == null
					|| !mainengine.ui.pausemenu.visible) {
				if (mainengine.running) {
					mainengine.player.soldier.setTheta(new Vector2(x, y));
				}
			}
		}
	}

	public void handleMouseClicked(MouseEvent e) {
		e.consume();
	}

	public void handleMousePressed(MouseEvent e) {
		mouseState[OLD_STATE][e.getButton() - 1] = mouseState[CURRENT_STATE][e
				.getButton() - 1];
		mouseState[CURRENT_STATE][e.getButton() - 1] = true;
		if (e.getButton() - 1 == LEFT_BUTTON) {
			if (engine.type == Scene.MAIN) {
				((Cursor) mainengine.components.get(MainScene.CURSOR)).down = true;
			} else if (engine.type == Scene.MENU) {
				menuengine.cursor.down = true;
			}
		}
		e.consume();
	}

	public void handleMouseReleased(MouseEvent e) {
		mouseState[OLD_STATE][e.getButton() - 1] = mouseState[CURRENT_STATE][e
				.getButton() - 1];
		mouseState[CURRENT_STATE][e.getButton() - 1] = false;
		if (e.getButton() - 1 == LEFT_BUTTON) {
			if (engine.type == Scene.MAIN) {
				((Cursor) mainengine.components.get(MainScene.CURSOR)).down = false;
				buttonRelease(mainengine.ui.pausemenu.buttons, e.getX(),
						e.getY());
			} else if (engine.type == Scene.MENU) {
				menuengine.cursor.down = false;
				buttonRelease(menuengine.options.buttons, e.getX(), e.getY());
			}
		} else if (e.getButton() - 1 == RIGHT_BUTTON) {
			if (engine.type == Scene.MAIN) {
				if (mainengine.running && !mainengine.ui.pausemenu.visible) {
					if (!mainengine.player.soldier.frozen
							&& mainengine.player.soldier.bones[BodyParts.UnderArm.num].omega == 0) {
						mainengine.player.soldier.throwGrenade();
					}
				}
			}
		}
		e.consume();
	}

	public void hitButton(Vector2 mousePos, HashMap<String, Button> buttons,
			Scene scene) {
		String[] keys = buttons.keySet().toArray(new String[0]);
		for (int i = 0; i < keys.length; i++) {
			Button b = buttons.get(keys[i]);
			if (b.enabled) {
				if (mousePos.X >= b.position.X
						&& mousePos.X <= b.position.X + b.getWidth()
						&& mousePos.Y >= b.position.Y
						&& mousePos.Y <= b.position.Y + b.getHeight()) {
					b.state = Button.DEPRESSED;
					if (scene == Scene.MENU) {
						if (b.msg.compareTo("Next") == 0) {
							menuengine.options
									.setVelocity(new Vector2(-2.5f, 0));
							b.enabled = false;
							menuengine.options.buttons.get("Next").enabled = false;
							menuengine.options.buttons.get("Back").enabled = true;
						}
						if (b.msg.compareTo("Back") == 0) {
							menuengine.options
									.setVelocity(new Vector2(2.5f, 0));
							b.enabled = false;
							menuengine.options.buttons.get("Back").enabled = false;
							menuengine.options.buttons.get("Next").enabled = true;
						}
						if (b.msg.compareTo("Play!") == 0) {
							Global.game.switchScenes(Scene.MAIN);
						}
					} else {
						if (b.msg.compareTo("Exit") == 0) {
							Global.game.stop();
						}
						if (b.msg.compareTo("Resume") == 0) {
							mainengine.ui.pausemenu.setVisible(false);
						}
						if (b.msg.compareTo("Restart") == 0) {
							//
						}
					}
				}
			}
		}

	}

	public void buttonRelease(HashMap<String, Button> buttons, int x, int y) {
		String[] keys = buttons.keySet().toArray(new String[0]);
		for (int i = 0; i < keys.length; i++) {
			Button b = buttons.get(keys[i]);
			if (x >= b.position.X && x <= b.position.X + b.getWidth()
					&& y >= b.position.Y && y <= b.position.Y + b.getHeight()) {
				b.state = Button.HOVERSELECTED;
				if (b.msg.compareTo("LW1") == 0) {
					if (menuengine.options.wp0 > 0)
						menuengine.options.wp0--;
					else
						menuengine.options.wp0 = Weapon.pCutoff;
					menuengine.options.refreshSoldier();
					menuengine.options.setPosition(menuengine.options.position);
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("RW1") == 0) {
					if (menuengine.options.wp0 < Weapon.pCutoff)
						menuengine.options.wp0++;
					else
						menuengine.options.wp0 = 0;
					menuengine.options.refreshSoldier();
					menuengine.options.setPosition(menuengine.options.position);
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("LW2") == 0) {
					if (menuengine.options.wp1 > Weapon.pCutoff + 1)
						menuengine.options.wp1--;
					else
						menuengine.options.wp1 = Weapon.Type.values().length - 1;
					menuengine.options.refreshSoldier();
					menuengine.options.setPosition(menuengine.options.position);
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("RW2") == 0) {
					if (menuengine.options.wp1 < Weapon.Type.values().length - 1)
						menuengine.options.wp1++;
					else
						menuengine.options.wp1 = Weapon.pCutoff + 1;
					menuengine.options.refreshSoldier();
					menuengine.options.setPosition(menuengine.options.position);
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("LG") == 0) {
					if (menuengine.options.g > 0)
						menuengine.options.g--;
					else
						menuengine.options.g = Grenade.Type.values().length - 1;
					menuengine.options.refreshSoldier();
					menuengine.options.setPosition(menuengine.options.position);
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("RG") == 0) {
					if (menuengine.options.g < Grenade.Type.values().length - 1)
						menuengine.options.g++;
					else
						menuengine.options.g = 0;
					menuengine.options.refreshSoldier();
					menuengine.options.setPosition(menuengine.options.position);
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("LM") == 0) {
					if (menuengine.options.mapNum > 0)
						menuengine.options.mapNum--;
					else
						menuengine.options.mapNum = menuengine.options.mapNames.length - 1;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("RM") == 0) {
					if (menuengine.options.mapNum < menuengine.options.mapNames.length - 1)
						menuengine.options.mapNum++;
					else
						menuengine.options.mapNum = 0;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("LMT") == 0) {
					if (menuengine.options.mode.num > 0)
						menuengine.options.mode = menuengine.options.modes[menuengine.options.mode.num - 1];
					else
						menuengine.options.mode = menuengine.options.modes[menuengine.options.modes.length - 1];
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("RMT") == 0) {
					if (menuengine.options.mode.num < menuengine.options.modes.length - 1)
						menuengine.options.mode = menuengine.options.modes[menuengine.options.mode.num + 1];
					else
						menuengine.options.mode = menuengine.options.modes[0];
					handleMouseMoved(x, y);
				}
				//==============
				if (b.msg.compareTo("LT1") == 0) {
					if (menuengine.options.teamNums[0] > 0)
						menuengine.options.teamNums[0]--;
					else
						menuengine.options.teamNums[0] = Faction.values().length;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("RT1") == 0) {
					if (menuengine.options.teamNums[0] < Faction.values().length)
						menuengine.options.teamNums[0]++;
					else
						menuengine.options.teamNums[0] = 0;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("LT2") == 0) {
					if (menuengine.options.teamNums[1] > 0)
						menuengine.options.teamNums[1]--;
					else
						menuengine.options.teamNums[1] = Faction.values().length;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("RT2") == 0) {
					if (menuengine.options.teamNums[1] < Faction.values().length)
						menuengine.options.teamNums[1]++;
					else
						menuengine.options.teamNums[1] = 0;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("LT3") == 0) {
					if (menuengine.options.teamNums[2] > 0)
						menuengine.options.teamNums[2]--;
					else
						menuengine.options.teamNums[2] = Faction.values().length;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("RT3") == 0) {
					if (menuengine.options.teamNums[2] < Faction.values().length)
						menuengine.options.teamNums[2]++;
					else
						menuengine.options.teamNums[2] = 0;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("LT4") == 0) {
					if (menuengine.options.teamNums[3] > 0)
						menuengine.options.teamNums[3]--;
					else
						menuengine.options.teamNums[3] = Faction.values().length;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("RT4") == 0) {
					if (menuengine.options.teamNums[3] < Faction.values().length)
						menuengine.options.teamNums[3]++;
					else
						menuengine.options.teamNums[3] = 0;
					handleMouseMoved(x, y);
				}
				//=============================================
				if (b.msg.compareTo("UT1") == 0) {
					if (menuengine.options.numPlayers[0] > 0)
						menuengine.options.numPlayers[0]--;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("DT1") == 0) {
					if (menuengine.options.numPlayers[0] < menuengine.options.maxNumPlayers)
						menuengine.options.numPlayers[0]++;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("UT2") == 0) {
					if (menuengine.options.numPlayers[1] > 0)
						menuengine.options.numPlayers[1]--;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("DT2") == 0) {
					if (menuengine.options.numPlayers[1] < menuengine.options.maxNumPlayers)
						menuengine.options.numPlayers[1]++;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("UT3") == 0) {
					if (menuengine.options.numPlayers[2] > 0)
						menuengine.options.numPlayers[2]--;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("DT3") == 0) {
					if (menuengine.options.numPlayers[2] < menuengine.options.maxNumPlayers)
						menuengine.options.numPlayers[2]++;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("UT4") == 0) {
					if (menuengine.options.numPlayers[3] > 0)
						menuengine.options.numPlayers[3]--;
					handleMouseMoved(x, y);
				}
				if (b.msg.compareTo("DT4") == 0) {
					if (menuengine.options.numPlayers[3] < menuengine.options.maxNumPlayers)
						menuengine.options.numPlayers[3]++;
					handleMouseMoved(x, y);
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
			if (x >= b.position.X && x <= b.position.X + b.getWidth()
					&& y >= b.position.Y && y <= b.position.Y + b.getHeight()) {
				b.state = Button.HOVERSELECTED;
			} else {
				b.state = Button.UNPRESSED;
			}
		}
	}
	public void clear(){
		for(int i = 0; i < mouseState.length; i++){
			for(int j = 0; j < mouseState[i].length; j++){
				mouseState[i][j] = false;
			}
		}
	}
}
