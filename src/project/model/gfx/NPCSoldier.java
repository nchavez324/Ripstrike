package project.model.gfx;

import java.awt.Graphics2D;
import java.util.Random;

import project.model.gfx.Weapon.Type;
import project.model.gfx.ui.SoldierUI;
import project.model.util.Global;
import project.model.util.Player;
import project.model.util.Vector2;

public class NPCSoldier extends Soldier {

	public long changeTimer = 0;

	public SoldierUI ui;

	public Soldier target;

	public NPCSoldier(Faction team, Player player) {
		super(team, player);
		torso.player = false;
		ui = new SoldierUI(this);
	}

	public NPCSoldier(Faction team, Type[] stock,
			project.model.gfx.Grenade.Type grenadeType, int numGrenades,
			Player player) {
		super(team, stock, grenadeType, numGrenades, player);
		torso.player = false;
		ui = new SoldierUI(this);
	}

	@Override
	public void update(long timePassed) {
		super.update(timePassed);
		long switchTime = 500;
		changeTimer += timePassed;
		boolean availTarg = aim();
		if (changeTimer >= switchTime) {
			move();
			changeTimer = 0;
		}
		if (availTarg) {
			shoot();
		}
		ui.update(timePassed);
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		ui.draw(g);
	}

	@Override
	public void drawLegs(Graphics2D g) {
		int off = -16;
		int yoff = -6;
		if (flippingLB) {
			if (animation == animations.get("Standing")) {
				off = -5;
			}
			g.drawImage(
					animation.sheet,
					(int) (torso.position.X + legPos.X - Global.camera.x + off),
					(int) (torso.position.Y + legPos.Y - Global.camera.y)
							+ yoff,
					animation.frameWidth
							+ (int) (torso.position.X + legPos.X
									- Global.camera.x + off),
					animation.getRectangle().height
							+ (int) (torso.position.Y + legPos.Y - Global.camera.y)
							+ yoff, animation.getRectangle().x
							+ animation.frameWidth, animation.getRectangle().y,
					animation.getRectangle().x, animation.getRectangle().y
							+ animation.getRectangle().height, null);
		} else {
			off = -22;
			g.drawImage(
					animation.sheet,
					(int) (torso.position.X + legPos.X - Global.camera.x + off),
					(int) (torso.position.Y + legPos.Y - Global.camera.y)
							+ yoff,
					animation.frameWidth
							+ (int) (torso.position.X + legPos.X
									- Global.camera.x + off),
					animation.getRectangle().height
							+ (int) (torso.position.Y + legPos.Y - Global.camera.y)
							+ yoff, animation.getRectangle().x,
					animation.getRectangle().y, animation.getRectangle().x
							+ animation.frameWidth, animation.getRectangle().y
							+ animation.getRectangle().height, null);
		}
	}

	public void move() {
		if (currentWeapon.melee) {
			if (target != null) {
				int room = 15;
				if (target.torso.position.X - room > torso.position.X) {
					uncrouch(1);
					run(1);
				} else if (target.torso.position.X + room < torso.position.X) {
					uncrouch(-1);
					run(-1);
				}
			}
		} else {
			int num = (new Random()).nextInt(5) - 1;
			if (num == 0) {
				if (animation == animations.get("Crouching"))
					uncrouch(0);
				stop();
			} else if (num == 2) {
				crouch();
			} else if (num == 3) {
				if (!airborne)
					jump(0);
			} else {
				if (animation == animations.get("Crouching"))
					uncrouch(num);
				run(num);
			}
		}
	}

	public boolean aim() {
		if (currentWeapon.bulletsLeft == 0
				&& currentWeapon.roundDenom - currentWeapon.bulletsPerClip <= 0) {
			int i = 0;
			if (currentWeapon == weapons[0])
				i = 1;
			if (weapons[i].bulletsLeft != 0
					|| weapons[i].roundDenom - weapons[i].bulletsPerClip > 0)
				switchWeapon();
		}
		Soldier target = null;
		float dist = -1;
		for (int i = 0; i < Global.game.main.players.players.size(); i++) {
			Player p = Global.game.main.players.players.get(i);
			if (p.soldier != null
					&& p.getTeam() != player.getTeam()
					&& (Global
							.getDist(torso.position, p.soldier.torso.position) < dist || dist == -1)) {
				target = p.soldier;
				dist = (float) Global.getDist(torso.position,
						p.soldier.torso.position);
			}
		}
		if (target != null) {
			if (currentWeapon.type == Weapon.Type.Sniper_Rifle) {
				setTheta(new Vector2((int) target.getHeadRect().getCenterX()
						- Global.camera.x, (int) target.getHeadRect()
						.getCenterY() - Global.camera.y));
			} else {
				setTheta(new Vector2(target.torso.position.X - Global.camera.x,
						target.torso.position.Y - Global.camera.y));
			}
			this.target = target;
			return true;
		} else {
			this.target = null;
			return false;
		}
	}
}
