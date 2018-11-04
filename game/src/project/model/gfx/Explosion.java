package project.model.gfx;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

import project.model.gfx.core.Animation;
import project.model.gfx.core.Sprite;
import project.model.util.Global;
import project.model.util.Player;
import project.model.util.Vector2;

public class Explosion extends Sprite {
	public static Image ice = Global.depot.etc.get("Ice");

	public static int freezeTime = 10000;

	public Image sheet;

	public Grenade.Type type;

	public Soldier soldier;

	public int w;

	public boolean real;

	public boolean effected = false;

	public int h;

	public Explosion(Image sheet, Soldier soldier, Grenade.Type type, int rows,
			int cols, Vector2 position) {
		super(new Animation(sheet, rows, cols, 30));
		this.type = type;
		this.soldier = soldier;
		w = sheet.getWidth(null) / cols;
		h = sheet.getHeight(null) / rows;
		real = true;
		this.sheet = sheet;
		this.position = position = new Vector2(position.X - w / 2, position.Y
				- h / 1.5f);
	}

	public Explosion(Image sheet, Soldier soldier, Grenade.Type type, int rows,
			int cols, Vector2 position, boolean real) {
		this(sheet, soldier, type, rows, cols, position);
		this.real = real;
	}

	@Override
	public void update(long timePassed) {
		super.update(timePassed);
		if (real) {
			if (animation.sceneIndex <= 28) {
				effect(Global.game.main.players.players);
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

	public void effect(ArrayList<Player> players) {
		if (!effected) {
			Shape blast = new Ellipse2D.Float(position.X, position.Y, w, h);
			for (Player p : players) {
				if (p.soldier != null) {
					Rectangle r = p.soldier.getRectangle();
					if (type == Grenade.Type.Frag && blast.intersects(r)) {
						p.soldier.health -= 1.5f;
						if (p.soldier.health <= 0
								&& !p.soldier.dead
								&& soldier != null
								&& soldier != p.soldier
								&& p.soldier.player.getTeam() != soldier.player
										.getTeam()) {
							p.soldier.dead = true;
							Global.game.main.killPoint(soldier.player, 1);
						} else if (p.soldier.health <= 0 && !p.soldier.dead
								&& soldier != null && soldier != p.soldier) {
							p.soldier.dead = true;
							Global.game.main.killPoint(soldier.player, -1);
						}
					} else if (type == Grenade.Type.Freeze) {
						if (!p.soldier.frozen && !effected
								&& blast.intersects(r)) {
							p.soldier.frozen = true;
							Explosion e = new Explosion(sheet, soldier, type,
									animation.rows, animation.cols,
									new Vector2((float) r.getCenterX(),
											(float) r.getCenterY() + 15), false);
							Global.game.main.grenades.addExplosion(e);
							effected = true;
						}
					} else if (type == Grenade.Type.Flash) {
						Global.game.main.grenades.flashInt = 100;
						Global.game.main.grenades.flashVel = -.001f;
					}
				}
			}
		}
	}

	public Vector2 generateRandPos() {
		Vector2 ans = new Vector2(position.X + w / 4, position.Y + h / 4);
		int x = (new Random()).nextInt(1000);
		int y = (new Random()).nextInt(100);
		x -= 500;
		y -= 50;
		ans.X += x;
		ans.Y += y;
		return ans;
	}

	public Vector2 generateRandVel() {
		Vector2 ans = new Vector2();
		double theta = ((new Random()).nextDouble()) * 2 * Math.PI;
		float speed = (((new Random()).nextFloat()) * .01f);
		ans.X = (float) Math.cos(theta) * speed;
		ans.Y = (float) Math.sin(theta) * speed;
		return ans;
	}
}
