package project.model.gfx;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import project.model.gfx.core.Component;
import project.model.util.Global;
import project.model.util.Vector2;

public class Bullet implements Component {

	public static Image sheet = Global.depot.etc.get("bullet");

	public static Image altSheet = Global.depot.etc.get("expBullet");

	public Vector2 lastPos, position, velocity;

	public double theta;

	public Weapon weapon;

	public boolean explosive = false;

	public boolean gone = false;

	public float damage;

	public long timeDead = -1;

	public static long timeToFade = 20;

	public static float SPEED = 1.2f; // .9

	public Bullet(Weapon weapon, double theta) {
		this.weapon = weapon;
		this.position = new Vector2(weapon.torso.position.X,
				weapon.torso.position.Y + 3);
		this.lastPos = new Vector2(position.X, position.Y);
		this.theta = theta;
		this.velocity = new Vector2(SPEED * (float) Math.cos(theta), SPEED
				* (float) Math.sin(theta));
		this.damage = weapon.damage;
	}

	public Bullet(Weapon weapon, double theta, boolean explosive) {
		this(weapon, theta);
		this.explosive = explosive;
	}

	@Override
	public void draw(Graphics2D g) {
		AffineTransform xform = new AffineTransform();
		Composite c = g.getComposite();

		if(timeDead > 0)
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				((float) timeDead / timeToFade)));

		if (theta > Math.PI / 2) {
			xform.translate(
					position.X + weapon.torso.off - 1 + weapon.getWidth()
							- Global.camera.x, position.Y - Global.camera.y);
			xform.rotate(theta,
					weapon.pivot.X + weapon.torso.off - weapon.getWidth() - 1,
					weapon.pivot.Y);
			xform.translate(weapon.weaponPosition.X + weapon.torso.off - 1,
					weapon.weaponPosition.Y);
			xform.translate(0, 7);
		} else {
			xform.translate(position.X - Global.camera.x + weapon.getWidth(),
					position.Y - Global.camera.y);
			xform.rotate(theta, weapon.pivot.X - weapon.getWidth(),
					weapon.pivot.Y);
			xform.translate(weapon.weaponPosition.X,
					weapon.weaponPosition.Y + 2);
		}
		if (!explosive)
			g.drawImage(sheet, xform, null);
		else
			g.drawImage(altSheet, xform, null);
		g.setComposite(c);
	}

	@Override
	public void update(long timePassed) {
		lastPos.X = position.X;
		lastPos.Y = position.Y;
		position.X += timePassed * velocity.X;
		position.Y += timePassed * velocity.Y;
		if (gone) {
			timeDead += timePassed;
		}
	}

	@Override
	public void loadResources() {
		//
	}

	public int getWidth() {
		return sheet.getWidth(null);
	}

	public int getHeight() {
		return sheet.getHeight(null);
	}

}
