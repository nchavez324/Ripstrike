package project.model.gfx;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Random;

import project.model.gfx.Soldier.BodyParts;
import project.model.gfx.core.Component;
import project.model.gfx.core.Root;
import project.model.util.Global;
import project.model.util.Player;
import project.model.util.Vector2;

/*
 * Unpublished Copyright 2012 RipStrike
 * Nicolas Chavez, USA
 */

public class Weapon implements Component {

	public static int pCutoff = 3;

	public enum Type {

		Submachine_Gun(0), Sniper_Rifle(1), Rocket_Launcher(2), Shotgun(3), Pistol(
				4), Dagger(5), Neo_Uzi(6), Mini_Blaster(7);
		public int num;

		Type(int num) {
			this.num = num;
		}
	}

	public Image sheet;

	public static Image flash = Global.depot.etc.get("flash");

	public Type type;

	public Vector2 weaponPosition, pivot;

	public Root torso;

	public Soldier soldier;

	public float accuracy, damage;

	public int dispersion, rateOfFire, reloadTime, bulletsPerClip, numClips,
			reloadTimePassed, bulletsLeft, roundDenom, fireTimer, colLen,
			flashTime, flashTimer;

	public boolean oneHanded, melee, explosive, resetAmmo;

	public double theta;

	public Weapon(Weapon.Type type, Vector2 pivot, Soldier soldier) {
		this.pivot = pivot;
		this.torso = soldier.torso;
		this.soldier = soldier;
		this.type = type;
		loadResources();
	}

	@Override
	public void draw(Graphics2D g) {
		AffineTransform trans = new AffineTransform();
		if (torso.player) {
			trans.translate(torso.screenPos.X, torso.screenPos.Y + 3);

		} else {
			trans.translate(torso.position.X - Global.camera.x,
					torso.position.Y + 3 - Global.camera.y);
		}
		if (theta != 0) {
			if (theta > Math.PI / 2) {
				trans.rotate(-theta + (3 / 2) * Math.PI, pivot.X, pivot.Y);

			} else {
				trans.rotate(theta, pivot.X, pivot.Y);
			}
		}

		trans.translate(weaponPosition.X, weaponPosition.Y);
		g.drawImage(sheet, trans, null);
		if (!melee)
			drawFlash(g);
	}

	public void drawFlash(Graphics2D g) {
		AffineTransform xform = new AffineTransform();
		if (flashTimer <= flashTime && flashTimer >= 0) {
			if (torso.player) {
				if (theta > Math.PI / 2) {
					xform.translate(torso.screenPos.X + getWidth(),
							torso.screenPos.Y + 3);
					xform.rotate(-theta + Math.PI, pivot.X - getWidth(),
							pivot.Y);
					xform.translate(weaponPosition.X, weaponPosition.Y - 11);
				} else {
					xform.translate(torso.screenPos.X + getWidth(),
							torso.screenPos.Y + 3);
					xform.rotate(theta, pivot.X - getWidth(), pivot.Y);
					xform.translate(weaponPosition.X, weaponPosition.Y - 11);
				}
			} else {
				if (theta > Math.PI / 2) {
					xform.translate(torso.position.X + getWidth()
							- Global.camera.x, torso.position.Y + 3
							- Global.camera.y);
					xform.rotate(-theta + Math.PI, pivot.X - getWidth(),
							pivot.Y);
					xform.translate(weaponPosition.X, weaponPosition.Y - 11);
				} else {
					xform.translate(torso.position.X + getWidth()
							- Global.camera.x, torso.position.Y + 3
							- Global.camera.y);
					xform.rotate(theta, pivot.X - getWidth(), pivot.Y);
					xform.translate(weaponPosition.X, weaponPosition.Y - 11);
				}
			}
			if (!Global.game.main.player.soldier.frozen)
				g.drawImage(flash, xform, null);
		}
	}

	@Override
	public void update(long timePassed) {
		if (fireTimer != -1) {
			fireTimer += timePassed;
		}
		if (fireTimer >= rateOfFire) {
			fireTimer = -1;
		}
		if (reloadTimePassed != -1) {
			reloadTimePassed += timePassed;
		}
		if (bulletsLeft <= 0 && roundDenom > bulletsPerClip
				&& reloadTimePassed == -1) {
			reloadTimePassed = 0;
		}
		if (reloadTimePassed >= reloadTime) {
			if (roundDenom - bulletsPerClip < bulletsPerClip - bulletsLeft) {
				bulletsLeft += roundDenom - bulletsPerClip;
				roundDenom = bulletsPerClip;
				resetAmmo = true;
			} else {
				roundDenom -= bulletsPerClip - bulletsLeft;
				bulletsLeft = bulletsPerClip;
				resetAmmo = true;
			}
			reloadTimePassed = -1;
		}
		if (roundDenom <= 0) {
			bulletsLeft = 0;
			roundDenom = 0;
		}
		if (flashTimer >= 0) {
			flashTimer += timePassed;
		}
		if (flashTimer >= flashTime) {
			flashTimer = -1;
		}
	}

	public void manualReload() {
		// reload!
		// Two Cases:
		// One: Has enough -> roundDenom - bulletsPerRound[type] >=
		// bulletsPerRound[type] - bulletsLeft
		// Two: Can only fill partially -> 0 < roundDenom -
		// bulletsPerRound[type] < bulletsPerRound[type] - bulletsLeft
		if (canReload()) {
			if (roundDenom - bulletsPerClip >= bulletsPerClip - bulletsLeft) {
				// Case 1
				reloadTimePassed = 0;

			} else if (roundDenom - bulletsPerClip < bulletsPerClip
					- bulletsLeft) {
				// Case 2
				System.out.println("Case 2");
				reloadTimePassed = 0;
			}
		}

	}

	public boolean canReload() {
		boolean ans = false;
		if (roundDenom - bulletsPerClip > 0 && bulletsLeft != bulletsPerClip
				&& bulletsLeft >= 0 && reloadTimePassed == -1) {
			ans = true;
		}
		return ans;
	}

	public int getWidth() {
		return sheet.getWidth(null);
	}

	public int getHeight() {
		return sheet.getHeight(null);
	}

	@Override
	public void loadResources() {
		sheet = Global.depot.weapons.get(type.toString());
		weaponPosition = Global.depot.weaponData.get(type.toString()).weaponPosition;
		oneHanded = Global.depot.weaponData.get(type.toString()).oneHanded;
		accuracy = Global.depot.weaponData.get(type.toString()).accuracy;
		damage = Global.depot.weaponData.get(type.toString()).damage;// cant yet
		dispersion = Global.depot.weaponData.get(type.toString()).dispersion;
		melee = Global.depot.weaponData.get(type.toString()).melee;// cant yet
		explosive = Global.depot.weaponData.get(type.toString()).explosive;// sorta
		rateOfFire = Global.depot.weaponData.get(type.toString()).rateOfFire;
		reloadTime = Global.depot.weaponData.get(type.toString()).reloadTime;
		bulletsPerClip = Global.depot.weaponData.get(type.toString()).bulletsPerClip;
		numClips = Global.depot.weaponData.get(type.toString()).numClips;
		colLen = Global.depot.weaponData.get(type.toString()).colLen;
		theta = 0;
		resetAmmo = false;
		reloadTimePassed = -1;
		roundDenom = numClips * bulletsPerClip;
		bulletsLeft = bulletsPerClip;
		flashTime = 40;
		flashTimer = -1;
		fireTimer = -1;
	}

	public void fire(Bullets bullets) {
		if (bulletsLeft != 0 && roundDenom != 0
				&& (fireTimer >= rateOfFire || fireTimer == -1) && !melee) {
			for (int i = 0; i < dispersion; i++) {
				if (explosive) {
					bullets.addBulletEx(this, accu(theta));
				} else {
					bullets.addBullet(this, accu(theta));
				}
				bulletsLeft--;
			}
			fireTimer = 0;
			flashTimer = 1;
		} else if (bulletsLeft != 0 && roundDenom != 0
				&& (fireTimer >= rateOfFire || fireTimer == -1) && melee) {
			if (torso.nodes[BodyParts.Head.num].theta > Math.PI / 2) {
				torso.nodes[BodyParts.OverArm.num].simpleAnim(
						torso.nodes[BodyParts.Head.num].theta + (Math.PI / 2),
						torso.nodes[BodyParts.Head.num].theta, -.005f);
			} else {
				torso.nodes[BodyParts.OverArm.num].simpleAnim(
						torso.nodes[BodyParts.Head.num].theta - (Math.PI / 2),
						torso.nodes[BodyParts.Head.num].theta, .005f);
			}
			for (Player p : Global.game.main.players.players) {
				if (p.soldier != null) {
					Rectangle r = p.soldier.getRectangle();
					int d = 12;
					if (p.soldier != this.soldier
							&& r.intersects(new Rectangle(soldier
									.getRectangle().x - d, soldier
									.getRectangle().y - d, soldier
									.getRectangle().width + d * 2, soldier
									.getRectangle().height + d * 2))) {
						p.soldier.health -= damage;
					}
				}
			}
			fireTimer = 0;
			flashTimer = 1;
		}
	}

	public double accu(double theta) {
		double off = (new Random()).nextDouble();
		off *= Math.PI / 12;
		off *= (1 - accuracy);
		int sign = (new Random()).nextInt(3) - 1;
		return (off * sign) + theta;
	}

}
