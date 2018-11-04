package project.controller;

import project.Runner.Scene;

import java.awt.MouseInfo;
import java.awt.event.KeyEvent;

import project.model.MenuScene;
import project.model.GameScene;
import project.model.IntroScene;
import project.model.util.Global;
import project.model.util.Vector2;
import project.model.MainScene;
import project.model.gfx.Soldier;

public class KeyManager {

	public static final int OLD_STATE = 0;

	public static final int CURRENT_STATE = 1;

	public int offset;

	public GameScene engine;

	public MainScene mainengine;

	public IntroScene introengine;

	public MenuScene menuengine;

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
		} else if (engine.type == Scene.MENU) {
			menuengine = (MenuScene) engine;
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
		} else if (engine.type == Scene.MENU) {
			menuengine = (MenuScene) engine;
		}
	}

	// What happens when a key is down;
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
		} else if (engine.type == Scene.MENU) {
			if (keyCode == KeyEvent.VK_ESCAPE) {
				Global.game.stop();
			}
			if (menuengine.options.popup == null
					|| !menuengine.options.popup.visible) {
				if (keyCode == KeyEvent.VK_A) {
					if (menuengine.options.team.num > 0) {
						menuengine.options.team = Soldier.Faction.values()[menuengine.options.team.num - 1];
						menuengine.options.teamArrow.position.Y = menuengine.options.position.Y
								+ 220 + (menuengine.options.team.num * 100);
						menuengine.options.teamLogo.singleImg = menuengine.options.factionLogos[menuengine.options.team.num].singleImg;
						menuengine.options.refreshSoldier();
						menuengine.options
								.setPosition(menuengine.options.position);
						Global.game.controller.mouseManager.handleMouseMoved(
								MouseInfo.getPointerInfo().getLocation().x
										- Global.game.view.getWindow()
												.getLocation().x, MouseInfo
										.getPointerInfo().getLocation().y
										- Global.game.view.getWindow()
												.getLocation().y);

					}
				}
				if (keyCode == KeyEvent.VK_D) {
					if (menuengine.options.team.num < Soldier.Faction.values().length - 1) {
						menuengine.options.team = Soldier.Faction.values()[menuengine.options.team.num + 1];
						menuengine.options.teamArrow.position.Y = menuengine.options.position.Y
								+ 220 + (menuengine.options.team.num * 100);
						menuengine.options.teamLogo.singleImg = menuengine.options.factionLogos[menuengine.options.team.num].singleImg;
						menuengine.options.refreshSoldier();
						menuengine.options
								.setPosition(menuengine.options.position);
						Global.game.controller.mouseManager.handleMouseMoved(
								MouseInfo.getPointerInfo().getLocation().x
										- Global.game.view.getWindow()
												.getLocation().x, MouseInfo
										.getPointerInfo().getLocation().y
										- Global.game.view.getWindow()
												.getLocation().y);
					}
				}
			}

		} else if (engine.type == Scene.MAIN) {
			if (mainengine.ui.pausemenu == null
					|| !mainengine.ui.pausemenu.visible) {
				if (keyCode == KeyEvent.VK_A) {
					mainengine.player.soldier.run(-1);
				}
				if (keyCode == KeyEvent.VK_D) {
					mainengine.player.soldier.run(1);
				}
				if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_CONTROL) {
					mainengine.player.soldier.crouch();
				}
				if (keyCode == KeyEvent.VK_W) {
					if (!mainengine.player.soldier.airborne
							&& !keyState[KeyEvent.VK_W][OLD_STATE]) {
						int dir = 0;
						if (keyState[KeyEvent.VK_A][CURRENT_STATE])
							dir = -1;
						if (keyState[KeyEvent.VK_D][CURRENT_STATE])
							dir = 1;
						mainengine.player.soldier.jump(dir);
					}
				}
				if (keyCode == KeyEvent.VK_R) {
					if (!mainengine.player.soldier.frozen) {
						mainengine.player.soldier.currentWeapon.manualReload();
					}
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
		} else if (engine.type == Scene.MENU) {
			if (keyCode == KeyEvent.VK_ENTER) {
				if (menuengine.options.popup != null) {
					menuengine.options.popup.visible = false;
					menuengine.options.popup = null;
				} else {
					if (menuengine.options.buttons.get("Next").enabled) {
						menuengine.options.setVelocity(new Vector2(-2.5f, 0));
						menuengine.options.buttons.get("Next").enabled = false;
					} else {
						Global.game.switchScenes(Scene.MAIN);
					}
				}
			}
		} else if (engine.type == Scene.MAIN) {
			if (keyCode == KeyEvent.VK_ENTER) {
				mainengine.ui.pausemenu
						.setVisible(!mainengine.ui.pausemenu.visible);
			}
			if (mainengine.ui.pausemenu == null
					|| !mainengine.ui.pausemenu.visible) {
				if (mainengine.running) {
					if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D) {
						mainengine.player.soldier.stop();
					}
					if (keyCode == KeyEvent.VK_S
							|| keyCode == KeyEvent.VK_CONTROL) {
						if (keyState[KeyEvent.VK_A][CURRENT_STATE]) {
							mainengine.player.soldier.uncrouch(-1);
						} else if (keyState[KeyEvent.VK_D][CURRENT_STATE]) {
							mainengine.player.soldier.uncrouch(1);
						} else {
							mainengine.player.soldier.uncrouch(0);
						}
					}
					if (keyCode == KeyEvent.VK_Q) {
						mainengine.ui.weaponBase
								.switchWeapon(mainengine.player.soldier
										.switchWeapon());
					}
				}
			}
		}
	}

	public void clear() {
		for (int i = 0; i < keyState.length; i++) {
			for (int j = 0; j < keyState[0].length; j++) {
				keyState[i][j] = false;
			}
		}

	}
}
