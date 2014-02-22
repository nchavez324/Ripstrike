package project.model.gfx;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import project.model.gfx.core.Component;
import project.model.gfx.core.Sprite;
import project.model.util.Global;

public class Grenades implements Component {

	public static Image[] exps;

	public static Image flashLight = Global.depot.etc.get("White");

	public ArrayList<Grenade> grenades;

	public ArrayList<Explosion> explosions;

	public ArrayList<Sprite> puffs;

	public Rectangle fov = Global.camera;

	public Queue<Explosion> expQueue;

	public Queue<Sprite> puffQueue;

	public Queue<Grenade> grenQueue;

	public float flashInt = 0;

	public float flashVel = -.0001f;

	public float flashAccel = -.00001f;

	public Grenades() {
		grenades = new ArrayList<Grenade>();
		explosions = new ArrayList<Explosion>();
		puffs = new ArrayList<Sprite>();
		exps = new Image[Grenade.Type.values().length];
		for (int i = 0; i < exps.length; i++) {
			Image im = Global.depot.etc.get(Grenade.Type.values()[i].toString()
					.toLowerCase() + "Exp");
			im = Global.scaleUp(im, im.getWidth(null) * 5,
					im.getHeight(null) * 5);
			exps[i] = im;
		}
		expQueue = new LinkedBlockingQueue<Explosion>();
		grenQueue = new LinkedBlockingQueue<Grenade>();
		puffQueue = new LinkedBlockingQueue<Sprite>();
	}

	@Override
	public void draw(Graphics2D g) {
		for (Grenade boom : grenades) {
			boom.draw(g);
		}
		for (Sprite puff : puffs) {
			puff.draw(g);
		}
		for (Explosion exp : explosions) {
			exp.draw(g);
		}
		if (flashInt > 0) {
			Composite origC = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(flashInt / 100)));
			g.drawImage(flashLight, 0, 0,
					Global.screenWidth + Global.leftInset, Global.screenHeight
							+ Global.topInset, 0, 0, 1, 1, null);
			g.setComposite(origC);
		}
	}

	public void addExplosion(Explosion e) {
		expQueue.add(e);
	}

	public void add(Grenade boom) {
		grenQueue.add(boom);
	}

	public void add(Sprite puff) {
		puffQueue.add(puff);
	}

	@Override
	public void update(long timePassed) {
		Iterator<Grenade> itr = grenades.iterator();
		while (itr.hasNext()) {
			Grenade boom = itr.next();
			boom.update(timePassed);
			if (Global.game.main.map.collisionMap.collides(boom.position)) {
				boom.explode();
			}
			if (boom.dead) {
				Explosion e = new Explosion(exps[boom.type.num], boom.soldier, boom.type, 7,
						6, boom.position);
				e.animation.once = true;
				explosions.add(e);
				itr.remove();
			}
		}
		Iterator<Explosion> sitr = explosions.iterator();
		while (sitr.hasNext()) {
			Explosion exp = sitr.next();
			exp.update(timePassed);
			if (exp.animation.sceneIndex == exp.animation.numFrames - 1
					|| exp.animation.stopped) {
				sitr.remove();
			}
		}
		Iterator<Sprite> pitr = puffs.iterator();
		while (pitr.hasNext()) {
			Sprite puff = pitr.next();
			puff.update(timePassed);
			if (puff.animation.sceneIndex >= 24) {
				pitr.remove();
			}
		}
		while (expQueue.size() > 0) {
			explosions.add(expQueue.poll());
		}
		while (grenQueue.size() > 0) {
			grenades.add(grenQueue.poll());
		}
		while (puffQueue.size() > 0) {
			puffs.add(puffQueue.poll());
		}
		if (flashInt > 0) {
			flashVel += flashAccel;
			flashInt += flashVel * timePassed;
		}
	}

	@Override
	public void loadResources() {
	}
}
