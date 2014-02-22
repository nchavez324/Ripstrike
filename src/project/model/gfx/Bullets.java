package project.model.gfx;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import project.model.gfx.core.Component;
import project.model.util.Global;

public class Bullets implements Component {

	public ArrayList<Bullet> bullets;

	public Rectangle fov = Global.camera;

	public Bullets() {
		bullets = new ArrayList<Bullet>();
		loadResources();
	}

	@Override
	public void draw(Graphics2D g) {
		for (Bullet bill : bullets) {
			bill.draw(g);
		}
	}

	public void add(Bullet bill) {
		bullets.add(bill);
	}

	public void addBullet(Weapon w, double theta) {
		add(new Bullet(w, theta));
	}

	public void addBulletEx(Weapon w, double theta) {
		add(new Bullet(w, theta, true));
	}

	public void remove(Bullet bill) {
		bullets.remove(bill);
	}

	@Override
	public void update(long timePassed) {
		Iterator<Bullet> itr = bullets.iterator();
		while (itr.hasNext()) {
			Bullet bill = itr.next();
			bill.update(timePassed);
			if ((bill.position.X > fov.x + fov.width && bill.velocity.X > 0)
					|| (bill.position.X + bill.getWidth() < fov.x && bill.velocity.X < 0)
					|| (bill.position.Y > fov.y + fov.height && bill.velocity.Y > 0)
					|| (bill.position.Y + bill.getHeight() < fov.y && bill.velocity.Y < 0)
					|| Global.game.main.map.collisionMap.collides(
							bill.position, bill.velocity)
					|| Global.game.main.hit(bill)) {
				bill.gone = true;
			}
			if (bill.explosive
					&& (Global.game.main.map.collisionMap.collides(
							bill.position, bill.velocity) || Global.game.main
							.hit(bill))) {
				Explosion e = new Explosion(
						Grenades.exps[Grenade.Type.Frag.num],
						bill.weapon.soldier, Grenade.Type.Frag, 7, 6,
						bill.position);
				bill.gone = true;
				e.animation.once = true;
				Global.game.main.grenades.explosions.add(e);
			}
			if (bill.gone && bill.timeDead >= Bullet.timeToFade) {
				itr.remove();
			}
		}
	}

	@Override
	public void loadResources() {
		//
	}

}
