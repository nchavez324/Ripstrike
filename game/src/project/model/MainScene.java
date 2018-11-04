package project.model;

import Resources.ResourceLoader;
import project.Runner.Scene;
import project.controller.MouseManager;
import project.model.gfx.core.Component;
import project.model.util.GameOptions;
import project.model.util.Global;
import project.model.util.Human;
import project.model.util.Match;
import project.model.util.PlayerOptions;
import project.model.util.Player;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.HashMap;

import project.model.gfx.Bullet;
import project.model.gfx.Bullets;
import project.model.gfx.Grenades;
import project.model.gfx.Players;
import project.model.gfx.Soldier;
import project.model.gfx.Weapon;
import project.model.gfx.Soldier.Faction;
import project.model.gfx.ui.Cursor;
import project.model.gfx.ui.Reticule;
import project.model.gfx.ui.PlayerUI;
import project.model.maps.CollisionMap;
import project.model.maps.CollisionTile;
import project.model.maps.Map;
import project.model.util.Vector2;

public class MainScene extends GameScene {

	public static final int MAPUNDER = 0;

	public static final int PLAYERS = 1;

	public static final int BULLETS = 2;

	public static final int GRENADES = 3;

	public static final int MAPOVER = 4;

	public static final int UI = 5;

	public static final int CURSOR = 6;

	public HashMap<Integer, Component> components;

	public ResourceLoader depot;

	public Player player;

	public Players players;

	public Bullets bullets;

	public Grenades grenades;

	public PlayerUI ui;

	public Cursor reticule;

	public PlayerOptions playerOptions;

	public GameOptions gameOptions;

	public Map map;

	public Match match;

	public MainScene() {

		type = Scene.MAIN;
		running = true;
		components = new HashMap<Integer, Component>();
		initComponents();
	}

	public void initComponents() {

		Global.camera = new Rectangle(0, 0, Global.screenWidth,
				Global.screenHeight);
		depot = Global.depot;

		player = new Human("Local Player", Faction.values()[0]);
		player.soldier.setPosition(new Vector2(100, 174));
		reticule = new Reticule(player);

		bullets = new Bullets();

		grenades = new Grenades();

		ui = new PlayerUI(player, null);

		players = new Players();

		components.put(MAPUNDER, map);
		components.put(PLAYERS, players);
		components.put(BULLETS, bullets);
		components.put(GRENADES, grenades);
		components.put(MAPOVER, map);
		components.put(UI, ui);
		components.put(CURSOR, reticule);
	}

	public void setOptions() {
		Global.camera = new Rectangle(0, 0, Global.screenWidth,
				Global.screenHeight);
		depot = Global.depot;
		
		players = gameOptions.players;
		player = players.getPlayer();
		
		map = Global.depot.maps.get(gameOptions.mapName);
		map.position = new Vector2(150, 242 - map.h * CollisionMap.SIZE
				+ CollisionMap.SIZE);

		players.syncTeams(gameOptions.teams);
		
		match = new Match(gameOptions.mode, gameOptions.teams,
				gameOptions.killsToWin, players);

		reticule = new Reticule(player);

		bullets = new Bullets();

		ui = new PlayerUI(player, match);

		components.put(PLAYERS, players);
		components.put(BULLETS, bullets);
		components.put(UI, ui);
		components.put(CURSOR, reticule);

	}

	@Override
	public void draw(Graphics2D g) {
		Integer[] keys = components.keySet().toArray(new Integer[0]);
		for (int i = 0; i < keys.length; i++) {
			if (i == MAPUNDER && map != null) {
				map.drawUnder(g);
			} else if (i == MAPOVER && map != null) {
				map.drawOver(g);
				//g.setColor(Color.black);
				//g.fillRect(0, 0, Global.camera.width, (int)(map.position.Y - Global.camera.y + CollisionMap.SIZE));
				//g.fillRect(0, (int)(map.position.Y + ((map.h-1) * CollisionMap.SIZE) - Global.camera.y), Global.camera.width, (int)(Global.camera.height - (map.position.Y + ((map.h-1) * CollisionMap.SIZE) - Global.camera.y)));
			}else {
				if (components.get(keys[i]) != null)
					components.get(keys[i]).draw(g);
			}
		}
	}

	@Override
	public void update(long timePassed) {
		if (running && !ui.pausemenu.visible) {
			Integer[] keys = components.keySet().toArray(new Integer[0]);
			for (int i = 0; i < keys.length; i++) {
				if (components.get(keys[i]) != null)
					components.get(keys[i]).update(timePassed);
			}
			Global.centerCamera(new Rectangle((int) player.soldier
					.getPosition().X, (int) player.soldier.getPosition().Y,
					player.soldier.torso.getWidth(), player.soldier.torso
							.getHeight()));
			if (Global.game.controller.mouseManager.mouseState[MouseManager.CURRENT_STATE][MouseManager.LEFT_BUTTON]) {
				shoot();
			}

			handleCollisions(timePassed);
		}
		if (!running || ui.pausemenu.visible) {
			reticule.update(timePassed);
			ui.update(timePassed);
		}
	}

	public void handleCollisions(long timePassed) {
		for (Player p : players.players) {
			if (p.soldier != null) {
				CollisionTile currentTile = map.collisionMap
						.getTile(new Vector2(p.soldier.reg.X
								+ p.soldier.torso.position.X, p.soldier.reg.Y
								+ p.soldier.torso.position.Y));
				CollisionTile aboveTile = map.collisionMap.getTile(new Vector2(
						p.soldier.reg.X + p.soldier.torso.position.X,
						p.soldier.reg.Y + p.soldier.torso.position.Y
								- CollisionMap.SIZE));
				CollisionTile underTile = map.collisionMap.getTile(new Vector2(
						p.soldier.reg.X + p.soldier.torso.position.X,
						p.soldier.reg.Y + p.soldier.torso.position.Y
								+ CollisionMap.SIZE));
				if (p.soldier.animation != p.soldier.animations
						.get("Crouching") && !p.soldier.airborne) {
					if (currentTile != null && aboveTile != null) {
						setTerr(p.soldier, currentTile, aboveTile, underTile);
					} else {
						p.soldier.velocity.X = p.soldier.dir * Soldier.SPEED;
					}
				} else if (p.soldier.airborne) {
					if (p.soldier.velocity.Y > 0
							&& aboveTile != null
							&& ((currentTile != null && (currentTile.type == CollisionTile.Type.SLOPE)) || (underTile != null && (underTile.type == CollisionTile.Type.BLOCK)))) {
						setTerr(p.soldier, currentTile, aboveTile, underTile);
						p.soldier.land();
					}
				}
			}
		}
	}

	public void setTerr(Soldier soldier, CollisionTile currentTile,
			CollisionTile aboveTile, CollisionTile underTile) {
		if (soldier.dir == 0) {
			soldier.velocity.X = 0;
			soldier.velocity.Y = 0;
			if (currentTile.type == CollisionTile.Type.BLOCK
					&& aboveTile.type == CollisionTile.Type.SLOPE) {
				int x = (int) (soldier.reg.X + soldier.torso.position.X - map.position.X)
						/ (CollisionMap.SIZE);
				int y = (int) (soldier.reg.Y + soldier.torso.position.Y
						- map.position.Y - CollisionMap.SIZE)
						/ (CollisionMap.SIZE);
				float dx = (soldier.reg.X + soldier.torso.position.X
						- map.position.X - x * CollisionMap.SIZE);
				dx /= CollisionMap.SIZE;
				soldier.torso.position.X = (x * CollisionMap.SIZE)
						+ map.position.X - soldier.reg.X + dx
						* CollisionMap.SIZE;
				soldier.torso.position.Y = (y * CollisionMap.SIZE)
						+ map.position.Y - soldier.reg.Y
						+ (aboveTile.b * CollisionMap.SIZE)
						+ (aboveTile.m * CollisionMap.SIZE * dx);
			} else if (currentTile.type == CollisionTile.Type.SLOPE) {
				int x = (int) (soldier.reg.X + soldier.torso.position.X - map.position.X)
						/ (CollisionMap.SIZE);
				int y = (int) (soldier.reg.Y + soldier.torso.position.Y - map.position.Y)
						/ (CollisionMap.SIZE);
				float dx = (soldier.reg.X + soldier.torso.position.X
						- map.position.X - x * CollisionMap.SIZE);
				dx /= CollisionMap.SIZE;
				soldier.torso.position.Y = (y * CollisionMap.SIZE)
						+ map.position.Y - soldier.reg.Y
						+ (currentTile.b * CollisionMap.SIZE)
						+ (currentTile.m * CollisionMap.SIZE * dx);
			} else if (currentTile.type == CollisionTile.Type.BLOCK
					&& aboveTile.type == CollisionTile.Type.EMPTY) {
				int y = (int) (soldier.reg.Y + soldier.torso.position.Y
						- map.position.Y - CollisionMap.SIZE)
						/ (CollisionMap.SIZE);
				soldier.torso.position.Y = map.position.Y
						+ ((y + 1) * CollisionMap.SIZE) - soldier.reg.Y;
			} else if (underTile != null
					&& underTile.type == CollisionTile.Type.BLOCK
					&& currentTile.type == CollisionTile.Type.EMPTY) {
				int y = (int) (soldier.reg.Y + soldier.torso.position.Y - map.position.Y)
						/ (CollisionMap.SIZE);
				soldier.torso.position.Y = map.position.Y
						+ ((y + 1) * CollisionMap.SIZE) - soldier.reg.Y;
			}
		}
		if (soldier.dir == 1) {
			int xb = (int) ((soldier.reg.X + soldier.torso.position.X - map.position.X) / (CollisionMap.SIZE)) + 1;
			int yb = (int) ((soldier.reg.Y + soldier.torso.position.Y - map.position.Y) / (CollisionMap.SIZE)) - 1;
			if ((xb >= 0 && yb >= 0 && xb < map.collisionMap.tiles.length && yb < map.collisionMap.tiles[0].length)
					&& soldier.getRectangle().intersects(
							map.collisionMap.getRectangle(xb, yb))
					&& map.collisionMap.tiles[xb][yb].type == CollisionTile.Type.BLOCK) {
				soldier.velocity.X = 0;
				soldier.velocity.Y = 0;
				soldier.dir = 0;
			} else {

				if (currentTile.type == CollisionTile.Type.BLOCK
						&& aboveTile.type == CollisionTile.Type.SLOPE) {
					int x = (int) (soldier.reg.X + soldier.torso.position.X - map.position.X)
							/ (CollisionMap.SIZE);
					int y = (int) (soldier.reg.Y + soldier.torso.position.Y
							- map.position.Y - CollisionMap.SIZE)
							/ (CollisionMap.SIZE);
					float dx = (soldier.reg.X + soldier.torso.position.X
							- map.position.X - x * CollisionMap.SIZE);
					dx /= CollisionMap.SIZE;
					soldier.torso.position.X = (x * CollisionMap.SIZE)
							+ map.position.X - soldier.reg.X + dx
							* CollisionMap.SIZE;
					soldier.torso.position.Y = (y * CollisionMap.SIZE)
							+ map.position.Y - soldier.reg.Y
							+ (aboveTile.b * CollisionMap.SIZE)
							+ (aboveTile.m * CollisionMap.SIZE * dx);
					soldier.velocity.X = soldier.dir
							* (float) (Soldier.SPEED * Math.cos(Math
									.atan(currentTile.m)));
					soldier.velocity.Y = soldier.dir
							* (float) (Soldier.SPEED * Math.sin(Math
									.atan(currentTile.m)));
				} else if (currentTile.type == CollisionTile.Type.SLOPE) {
					int x = (int) (soldier.reg.X + soldier.torso.position.X - map.position.X)
							/ (CollisionMap.SIZE);
					int y = (int) (soldier.reg.Y + soldier.torso.position.Y - map.position.Y)
							/ (CollisionMap.SIZE);
					float dx = (soldier.reg.X + soldier.torso.position.X
							- map.position.X - x * CollisionMap.SIZE);
					dx /= CollisionMap.SIZE;
					soldier.torso.position.Y = (y * CollisionMap.SIZE)
							+ map.position.Y - soldier.reg.Y
							+ (currentTile.b * CollisionMap.SIZE)
							+ (currentTile.m * CollisionMap.SIZE * dx);
					soldier.velocity.X = soldier.dir
							* (float) (Soldier.SPEED * Math.cos(Math
									.atan(currentTile.m)));
					soldier.velocity.Y = soldier.dir
							* (float) (Soldier.SPEED * Math.sin(Math
									.atan(currentTile.m)));
				} else if (currentTile.type == CollisionTile.Type.BLOCK
						&& aboveTile.type == CollisionTile.Type.EMPTY) {
					int y = (int) (soldier.reg.Y + soldier.torso.position.Y
							- map.position.Y - CollisionMap.SIZE)
							/ (CollisionMap.SIZE);
					soldier.velocity.X = soldier.dir * Soldier.SPEED;
					soldier.velocity.Y = 0;
					soldier.torso.position.Y = map.position.Y
							+ ((y + 1) * CollisionMap.SIZE) - soldier.reg.Y;
				} else if (underTile != null
						&& underTile.type == CollisionTile.Type.BLOCK
						&& currentTile.type == CollisionTile.Type.EMPTY) {
					int y = (int) (soldier.reg.Y + soldier.torso.position.Y - map.position.Y)
							/ (CollisionMap.SIZE);
					soldier.velocity.X = soldier.dir * Soldier.SPEED;
					soldier.velocity.Y = 0;
					soldier.torso.position.Y = map.position.Y
							+ ((y + 1) * CollisionMap.SIZE) - soldier.reg.Y;
				} else if (currentTile.type == CollisionTile.Type.BLOCK
						&& aboveTile.type == CollisionTile.Type.BLOCK) {
					soldier.velocity.X = 0;
					soldier.velocity.Y = 0;
					soldier.dir = 0;
				}
			}
		} else if (soldier.dir == -1) {
			int xb = (int) ((soldier.reg.X + soldier.torso.position.X - map.position.X) / (CollisionMap.SIZE)) - 1;
			int yb = (int) ((soldier.reg.Y + soldier.torso.position.Y - map.position.Y) / (CollisionMap.SIZE)) - 1;
			if ((xb >= 0 && yb >= 0 && xb < map.collisionMap.tiles.length && yb < map.collisionMap.tiles[0].length)
					&& soldier.getRectangle().intersects(
							map.collisionMap.getRectangle(xb, yb))
					&& map.collisionMap.tiles[xb][yb].type == CollisionTile.Type.BLOCK) {
				soldier.velocity.X = 0;
				soldier.velocity.Y = 0;
				soldier.dir = 0;
			} else {
				if (currentTile.type == CollisionTile.Type.BLOCK
						&& aboveTile.type == CollisionTile.Type.SLOPE) {
					int x = (int) (soldier.reg.X + soldier.torso.position.X - map.position.X)
							/ (CollisionMap.SIZE);
					int y = (int) (soldier.reg.Y + soldier.torso.position.Y
							- map.position.Y - CollisionMap.SIZE)
							/ (CollisionMap.SIZE);
					float dx = (soldier.reg.X + soldier.torso.position.X
							- map.position.X - (x + 1) * CollisionMap.SIZE);
					dx /= CollisionMap.SIZE;
					dx += 1;
					soldier.torso.position.X = (x * CollisionMap.SIZE)
							+ map.position.X - soldier.reg.X + dx
							* CollisionMap.SIZE;
					soldier.torso.position.Y = (y * CollisionMap.SIZE)
							+ map.position.Y - soldier.reg.Y
							+ (aboveTile.b * CollisionMap.SIZE)
							+ (aboveTile.m * CollisionMap.SIZE * dx);
					soldier.velocity.X = soldier.dir
							* (float) (Soldier.SPEED * Math.cos(Math
									.atan(currentTile.m)));
					soldier.velocity.Y = soldier.dir
							* (float) (Soldier.SPEED * Math.sin(Math
									.atan(currentTile.m)));
				} else if (currentTile.type == CollisionTile.Type.SLOPE) {
					int x = (int) (soldier.reg.X + soldier.torso.position.X - map.position.X)
							/ (CollisionMap.SIZE);
					int y = (int) (soldier.reg.Y + soldier.torso.position.Y - map.position.Y)
							/ (CollisionMap.SIZE);
					float dx = (soldier.reg.X + soldier.torso.position.X
							- map.position.X - x * CollisionMap.SIZE);
					dx /= CollisionMap.SIZE;
					soldier.torso.position.Y = (y * CollisionMap.SIZE)
							+ map.position.Y - soldier.reg.Y
							+ (currentTile.b * CollisionMap.SIZE)
							+ (currentTile.m * CollisionMap.SIZE * dx);
					soldier.velocity.X = soldier.dir
							* (float) (Soldier.SPEED * Math.cos(Math
									.atan(currentTile.m)));
					soldier.velocity.Y = soldier.dir
							* (float) (Soldier.SPEED * Math.sin(Math
									.atan(currentTile.m)));
				} else if (currentTile.type == CollisionTile.Type.BLOCK
						&& aboveTile.type == CollisionTile.Type.EMPTY) {
					int y = (int) (soldier.reg.Y + soldier.torso.position.Y
							- map.position.Y - CollisionMap.SIZE)
							/ (CollisionMap.SIZE);
					soldier.velocity.X = soldier.dir * Soldier.SPEED;
					soldier.velocity.Y = 0;
					soldier.torso.position.Y = map.position.Y
							+ ((y + 1) * CollisionMap.SIZE) - soldier.reg.Y;
				} else if (underTile != null
						&& underTile.type == CollisionTile.Type.BLOCK
						&& currentTile.type == CollisionTile.Type.EMPTY) {
					int y = (int) (soldier.reg.Y + soldier.torso.position.Y - map.position.Y)
							/ (CollisionMap.SIZE);
					soldier.velocity.X = soldier.dir * Soldier.SPEED;
					soldier.velocity.Y = 0;
					soldier.torso.position.Y = map.position.Y
							+ ((y + 1) * CollisionMap.SIZE) - soldier.reg.Y;
				} else if (currentTile.type == CollisionTile.Type.BLOCK
						&& aboveTile.type == CollisionTile.Type.BLOCK) {
					soldier.velocity.X = 0;
					soldier.velocity.Y = 0;
					soldier.dir = 0;
				}
			}
		}
	}

	public boolean hit(Bullet bill) {
		Vector2 point = bill.position;
		Vector2 velocity = bill.velocity;
		long timeFrame = 75;
		Line2D.Float trajectory = new Line2D.Float(point.X, point.Y, point.X
				+ velocity.X * timeFrame, point.Y + velocity.Y * timeFrame);
		for (Player p : players.players) {
			Soldier target = p.soldier;
			if (target != null
					&& target.getRectangle().intersectsLine(trajectory)
					&& bill.weapon.torso != target.torso && !bill.gone) {
				if (!bill.explosive) {
					if (target.getHeadRect().intersectsLine(trajectory)
							&& bill.weapon.type == Weapon.Type.Sniper_Rifle) {
						target.health -= bill.damage * 30;
					} else {
						target.health -= bill.damage;
					}
				}
				if (target.health <= 0
						&& !target.dead
						&& bill.weapon.soldier != null
						&& bill.weapon.soldier != target
						&& target.player.getTeam() != bill.weapon.soldier.player
								.getTeam()) {
					target.dead = true;
					killPoint(bill.weapon.soldier.player, 1);
					return true;
				}
				bill.gone = true;
			}
		}
		return false;
	}

	public void killPoint(Player killer, int d) {
		killer.numKills += d;
		match.numKills[killer.getTeam()] += d;
	}

	public void shoot() {
		if (player.soldier.shoot())
			ui.weaponBase.ammo.next();
	}
}
