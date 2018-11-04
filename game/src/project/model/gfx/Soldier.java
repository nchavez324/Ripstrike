package project.model.gfx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

import project.model.gfx.core.Animation;
import project.model.gfx.core.Bone;
import project.model.gfx.core.Component;
import project.model.gfx.core.Root;
import project.model.util.Global;
import project.model.util.SoldierPivots;
import project.model.util.Player;
import project.model.util.Vector2;

public class Soldier implements Component {

	public static final float SPEED = .35f;

	public enum Faction {

		New_Federation(0), Sierra(1), SRK(2), Ajax(3);
		public int num;

		Faction(int num) {
			this.num = num;
		}
	}

	public enum BodyParts {

		UnderArm(0), Head(1), OverArm(2);
		public int num;

		BodyParts(int num) {
			this.num = num;
		}

	}

	public HashMap<String, Animation> animations;
	public Animation animation;
	
	public final boolean DEBUG = false;
	public static final double armRest = Math.toRadians(75);
	public static final Vector2 legPos = new Vector2(5, 33);
	public static SoldierPivots[][] pivots;

	public Faction faction;
	public Weapon[] weapons;
	public Grenade.Type grenadeType;
	public int numGrenadesLeft;
	public int weaponLayer;
	public Weapon currentWeapon;
	public Bone[] bones;
	public Root torso;
	public Vector2 velocity, acceleration;
	public Vector2 reg;
	public Weapon.Type[] stock;
	public int rootLayer = 1;
	public boolean flippingUB = false;
	public boolean flippingLB = false;
	public float health = 100;
	public boolean frozen = false;
	public int dir = 0;
	public int thawTime = 0;
	public boolean airborne = false;
	public boolean dead = false;
	public Player player;

	public Soldier(Faction faction, Player player) {
		this.faction = faction;
		this.player = player;
		stock = new Weapon.Type[2];
		stock[0] = Weapon.Type.Submachine_Gun;
		stock[1] = Weapon.Type.Pistol;
		loadResources();
	}

	public Soldier(Faction faction, Weapon.Type[] stock,
			Grenade.Type grenadeType, int numGrenades, Player player) {
		this.faction = faction;
		this.stock = stock;
		this.grenadeType = grenadeType;
		numGrenadesLeft = numGrenades;
		this.player = player;
		loadResources();
	}

	@Override
	public void draw(Graphics2D g) {
		AffineTransform orig = g.getTransform();
		AffineTransform t = ((AffineTransform) orig.clone());
		t.scale(-1, 1);
		if (torso.player) {
			t.translate(-Global.screenWidth, 0);
			t.translate(-torso.off, 0);
		} else {
			if (this instanceof NPCSoldier) {
				t.translate(-torso.position.X * 2 + Global.camera.x * 2, 0);
				t.translate(-torso.off - 22, 0);
			} else {
				t.translate((-torso.position.X + 649 - torso.getWidth())
						- Global.screenWidth - Global.camera.x, 0);
				t.translate(-torso.off, 0);
			}
		}
		if (animation == animations.get("Crouching")
				&& bones[BodyParts.Head.num].theta > Math.PI / 2) {
			flippingLB = true;
		}
		if (animation == animations.get("Crouching")
				&& bones[BodyParts.Head.num].theta <= Math.PI / 2) {
			flippingLB = false;
		}
		if (torso.player) {
			if (bones[BodyParts.Head.num].theta > Math.PI / 2) {
				flippingUB = true;
				reg = new Vector2(13, 68);
			} else {
				flippingUB = false;
				reg = new Vector2(15, 68);
			}
		} else {
			if (bones[BodyParts.Head.num].theta > Math.PI / 2) {
				flippingUB = true;
				reg = new Vector2(13, 68);
			} else {
				flippingUB = false;
				reg = new Vector2(15, 68);
			}
		}
		int j = BodyParts.UnderArm.num;
		if (flippingUB)
			g.setTransform(t);
		else
			g.setTransform(orig);
		bones[j].draw(g);

		if (flippingUB) {
			g.setTransform(t);
			torso.drawHip(g);
			drawSecondary(g);
			g.setTransform(orig);
		} else {
			torso.drawHip(g);
			drawSecondary(g);
		}
		drawLegs(g);

		for (int i = 0; i < bones.length; i++) {
			if (i == rootLayer) {
				if (flippingUB) {
					g.setTransform(t);
				} else {
					g.setTransform(orig);
				}
				torso.draw(g);
			}
			if (i == weaponLayer) {
				if (flippingUB) {
					g.setTransform(t);
				} else {
					g.setTransform(orig);
				}
				currentWeapon.draw(g);

			}
			if (i == BodyParts.OverArm.num || i == BodyParts.Head.num) {
				if (flippingUB)
					g.setTransform(t);
				else
					g.setTransform(orig);
				bones[i].draw(g);
			}
		}
		g.setTransform(orig);
		if (frozen) {
			g.drawImage(Explosion.ice, (int) torso.position.X - Global.camera.x
					- 65, (int) torso.position.Y - Global.camera.y - 105, null);
		}

		if (DEBUG) {
			g.setColor(Color.RED);
			g.drawRect(getRectangle().x - Global.camera.x, getRectangle().y
					- Global.camera.y, getRectangle().width,
					getRectangle().height);
			g.setColor(Color.GREEN);
			g.drawRect(getHeadRect().x - Global.camera.x, getHeadRect().y
					- Global.camera.y, getHeadRect().width,
					getHeadRect().height);
			g.drawString(torso.position + "", torso.position.X - Global.camera.x, torso.position.Y - 20 - Global.camera.y);
		}

	}

	public void drawLegs(Graphics2D g) {
		int off = -16;
		int yoff = -6;
		if (animation == animations.get("Jumping")) {
			yoff = -12;
		}
		if (flippingLB) {
			if (torso.player) {
				if (animation == animations.get("Standing")) {
					off = -3;
					if (faction == Faction.Ajax || faction == Faction.SRK) {
						off = -6;
					}
					if (faction == Faction.Sierra) {
						off = -4;
					}
				}
				if ((faction == Faction.Ajax || faction == Faction.SRK || faction == Faction.Sierra)
						&& animation == animations.get("Jumping")) {
					off = -10;
				}

				
				g.drawImage(animation.sheet, (int) (torso.screenPos.X
						+ legPos.X + off), (int) (torso.screenPos.Y + legPos.Y)
						+ yoff, animation.frameWidth
						+ (int) (torso.screenPos.X + legPos.X + off),
						animation.getRectangle().height
								+ (int) (torso.screenPos.Y + legPos.Y) + yoff,
						animation.getRectangle().x + animation.frameWidth,
						animation.getRectangle().y, animation.getRectangle().x,
						animation.getRectangle().y
								+ animation.getRectangle().height, null);
			} else {
				if (animation == animations.get("Standing")) {
					off = 2;
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
								+ animation.frameWidth,
						animation.getRectangle().y, animation.getRectangle().x,
						animation.getRectangle().y
								+ animation.getRectangle().height, null);
			}
		} else {
			off = -21;
			if (torso.player) {
				g.drawImage(animation.sheet, (int) (torso.screenPos.X
						+ legPos.X + off), (int) (torso.screenPos.Y + legPos.Y)
						+ yoff, animation.frameWidth
						+ (int) (torso.screenPos.X + legPos.X + off),
						animation.getRectangle().height
								+ (int) (torso.screenPos.Y + legPos.Y) + yoff,
						animation.getRectangle().x, animation.getRectangle().y,
						animation.getRectangle().x + animation.frameWidth,
						animation.getRectangle().y
								+ animation.getRectangle().height, null);
			} else {
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
								+ animation.frameWidth,
						animation.getRectangle().y
								+ animation.getRectangle().height, null);
			}
		}
	}

	public void drawSecondary(Graphics2D g) {
		Weapon s = null;
		if (currentWeapon == weapons[0])
			s = weapons[1];
		else
			s = weapons[0];
		AffineTransform orig = (AffineTransform) g.getTransform().clone();
		AffineTransform t = new AffineTransform();
		if (torso.player) {
			t.concatenate(AffineTransform.getTranslateInstance(
					(torso.screenPos.X + 7), torso.screenPos.Y));
		} else {
			t.concatenate(AffineTransform.getTranslateInstance(
					(torso.position.X + 7) - Global.camera.x, torso.position.Y
							- Global.camera.y));
		}
		if (s.melee) {
			t.translate(-12, 0);
			t.rotate(3 * Math.PI / 2);
		}
		t.concatenate(AffineTransform.getRotateInstance(Math.toRadians(95)));

		g.drawImage(s.sheet, t, null);
		g.setTransform(orig);
	}

	@Override
	public void update(long timePassed) {
		if (!frozen) {
			for (int i = 0; i < bones.length; i++) {
				bones[i].update(timePassed);
			}
			if (currentWeapon.oneHanded
					&& bones[BodyParts.UnderArm.num].omega == 0) {
				bones[BodyParts.UnderArm.num].theta = armRest;
			}
			currentWeapon.theta = bones[BodyParts.OverArm.num].theta;
			torso.update(timePassed);
			velocity.X += acceleration.X * timePassed;
			velocity.Y += acceleration.Y * timePassed;
			torso.position.X += velocity.X * timePassed;
			torso.position.Y += velocity.Y * timePassed;
			animation.update(timePassed);
			currentWeapon.update(timePassed);
			if (bones[BodyParts.UnderArm.num].omega == 0) {
				bones[BodyParts.UnderArm.num].theta = bones[BodyParts.Head.num].theta;
				bones[BodyParts.UnderArm.num].anim = false;
			}
			if (bones[BodyParts.OverArm.num].omega == 0 && currentWeapon.melee) {
				bones[BodyParts.OverArm.num].theta = bones[BodyParts.Head.num].theta;
				bones[BodyParts.OverArm.num].anim = false;
			}
		} else {
			thawTime += timePassed;
			if (thawTime >= Explosion.freezeTime) {
				Explosion e = new Explosion(
						Grenades.exps[Grenade.Type.Freeze.num], this,
						Grenade.Type.Freeze, 7, 6, new Vector2(
								(float) getRectangle().getCenterX(),
								(float) getRectangle().getCenterY() + 15),
						false);
				Global.game.main.grenades.addExplosion(e);
				frozen = false;
				velocity.X = 0;
				velocity.Y = 0;
				dir = 0;
				animation = animations.get("Standing");
				setTheta(new Vector2(MouseInfo.getPointerInfo().getLocation().x
						- Global.game.view.getWindow().getLocation().x,
						MouseInfo.getPointerInfo().getLocation().y
								- Global.game.view.getWindow().getLocation().y));
				thawTime = 0;
			}
		}
	}

	@Override
	public void loadResources() {

		String header = "" + faction.toString() + "/";
		torso = new Root(Global.depot.characters.get(header + "Torso"), this,
				30);
		velocity = new Vector2();
		bones = new Bone[BodyParts.values().length];

		Bone underArm = new Bone(
				Global.depot.characters.get(header + "UnderArm"),
				torso,
				Global.depot.pivots[faction.num][BodyParts.UnderArm.num].pivot,
				Global.depot.pivots[faction.num][BodyParts.UnderArm.num].anchor,
				Global.depot.pivots[faction.num][BodyParts.UnderArm.num].flipOff);
		Bone head = new Bone(Global.depot.characters.get(header + "Head"),
				torso,
				Global.depot.pivots[faction.num][BodyParts.Head.num].pivot,
				Global.depot.pivots[faction.num][BodyParts.Head.num].anchor,
				Global.depot.pivots[faction.num][BodyParts.Head.num].flipOff);
		Bone overArm = new Bone(
				Global.depot.characters.get(header + "OverArm"), torso,
				Global.depot.pivots[faction.num][BodyParts.OverArm.num].pivot,
				Global.depot.pivots[faction.num][BodyParts.OverArm.num].anchor,
				Global.depot.pivots[faction.num][BodyParts.OverArm.num].flipOff);

		bones[0] = underArm;
		bones[1] = head;
		bones[2] = overArm;

		for (int i = 0; i < BodyParts.values().length; i++) {
			bones[i].nodeNum = i;
			torso.nodes[i] = bones[i];
			torso.anchors[i] = Global.depot.pivots[faction.num][i].rootAnchor;
		}

		weaponLayer = BodyParts.OverArm.num;

		weapons = new Weapon[1];
		if (stock == null) {
			weapons[0] = new Weapon(Weapon.Type.Submachine_Gun,
					bones[BodyParts.OverArm.num].pivot.copy(), this);
		} else {
			weapons = new Weapon[stock.length];
			for (int i = 0; i < weapons.length; i++) {
				weapons[i] = new Weapon(stock[i],
						bones[BodyParts.OverArm.num].pivot.copy(), this);
			}
		}
		currentWeapon = weapons[0];

		animations = new HashMap<String, Animation>();

		Animation standing = new Animation(Global.depot.characters.get(faction
				.toString() + "/" + "Standing"), 1, 100);
		Animation running = new Animation(Global.depot.characters.get(faction
				.toString() + "/" + "Running"), 11, 45);
		Animation crouching = new Animation(Global.depot.characters.get(faction
				.toString() + "/" + "Crouching"), 1, 100);
		Animation jumping = new Animation(Global.depot.characters.get(faction
				.toString() + "/" + "Jumping"), 1, 100);
		animations.put("Standing", standing);
		animations.put("Running", running);
		animations.put("Crouching", crouching);
		animations.put("Jumping", jumping);
		animation = standing;

		acceleration = new Vector2();
		reg = new Vector2(15, 68);
	}

	public void setPosition(Vector2 p) {
		torso.position = p;
	}

	public void setTheta(Vector2 target) {
		if (!frozen) {
			Vector2 torsoPos = torso.screenPos;
			if (!torso.player) {
				torsoPos = torso.position;
				target.X = target.X + Global.camera.x;
				target.Y = target.Y + Global.camera.y;
			}
			bones[Soldier.BodyParts.Head.num].theta = Global.getAngle(
					torso.anchors[bones[Soldier.BodyParts.Head.num].nodeNum].X
							+ torsoPos.X,
					torso.anchors[bones[Soldier.BodyParts.Head.num].nodeNum].Y
							+ torsoPos.Y, target.X, target.Y);
			if (bones[Soldier.BodyParts.OverArm.num].omega == 0)
				bones[Soldier.BodyParts.OverArm.num].theta = Global
						.getAngle(
								torso.anchors[bones[Soldier.BodyParts.OverArm.num].nodeNum].X
										+ torsoPos.X,
								torso.anchors[bones[Soldier.BodyParts.OverArm.num].nodeNum].Y
										+ torsoPos.Y, target.X, target.Y);
		}
	}

	public void setTheta(double theta) {
		if (!frozen) {
			bones[Soldier.BodyParts.Head.num].theta = theta;
			if (bones[Soldier.BodyParts.OverArm.num].omega == 0)
				bones[Soldier.BodyParts.OverArm.num].theta = theta;
		}
	}

	public void throwGrenade() {
		if (numGrenadesLeft > 0) {
			numGrenadesLeft--;
			Vector2 velocity = new Vector2();
			float vMag = (float) (.003f * Math.pow(
					(Math.pow(MouseInfo.getPointerInfo().getLocation().x
							- Global.game.view.getWindow().getLocation().x
							- torso.screenPos.X, 2))
							+ (Math.pow(MouseInfo.getPointerInfo()
									.getLocation().y
									- Global.game.view.getWindow()
											.getLocation().y
									- torso.screenPos.Y, 2)), .5));
			velocity.X = vMag
					* (float) Math.cos(bones[BodyParts.Head.num].theta);
			velocity.Y = vMag
					* (float) Math.sin(bones[BodyParts.Head.num].theta);
			Global.game.main.grenades.add(new Grenade(grenadeType, this,
					velocity, new Vector2(torso.position.X, torso.position.Y)));
		}
		if (bones[BodyParts.Head.num].theta > Math.PI / 2) {
			bones[BodyParts.UnderArm.num].simpleAnim(
					bones[BodyParts.Head.num].theta + Math.PI,
					bones[BodyParts.Head.num].theta, -.01f);
		} else {
			bones[BodyParts.UnderArm.num].simpleAnim(
					bones[BodyParts.Head.num].theta - Math.PI,
					bones[BodyParts.Head.num].theta, .01f);
		}
	}

	public Vector2 getPosition() {
		return torso.position;
	}

	public Rectangle getRectangle() {
		Rectangle ans = new Rectangle();
		if (currentWeapon.theta > Math.PI / 2) {
			ans.x = (int) (torso.position.X
					+ torso.anchors[BodyParts.Head.num].X
					+ bones[BodyParts.Head.num].pivot.X
					- bones[BodyParts.Head.num].getWidth() + 12);
		} else {
			ans.x = (int) (torso.position.X
					+ torso.anchors[BodyParts.Head.num].X
					+ bones[BodyParts.Head.num].pivot.X
					- bones[BodyParts.Head.num].getWidth() + 6);
		}
		ans.y = (int) (torso.position.Y + torso.anchors[BodyParts.Head.num].Y
				+ bones[BodyParts.Head.num].pivot.Y
				- bones[BodyParts.Head.num].getHeight() - 15);
		ans.width = torso.getWidth() + 5;
		ans.height = (int) (bones[BodyParts.Head.num].getHeight()
				- bones[BodyParts.Head.num].pivot.Y + torso.getHeight()
				+ animation.sheet.getHeight(null) - 2);

		return ans;
	}

	public Rectangle getHeadRect() {
		Rectangle ans = new Rectangle();
		if (currentWeapon.theta > Math.PI / 2) {
			ans.x = (int) (torso.position.X
					+ torso.anchors[BodyParts.Head.num].X
					+ bones[BodyParts.Head.num].pivot.X
					- bones[BodyParts.Head.num].getWidth()
					- (bones[BodyParts.Head.num].getWidth() / 3)
					* Math.sin(currentWeapon.theta) + 9);
		} else {
			ans.x = (int) (torso.position.X
					+ torso.anchors[BodyParts.Head.num].X
					+ bones[BodyParts.Head.num].pivot.X
					- bones[BodyParts.Head.num].getWidth()
					+ (bones[BodyParts.Head.num].getWidth() / 3)
					* Math.sin(currentWeapon.theta) + 3);
		}
		ans.y = (int) (torso.position.Y + torso.anchors[BodyParts.Head.num].Y
				+ bones[BodyParts.Head.num].pivot.Y
				- bones[BodyParts.Head.num].getHeight() - 15);
		ans.width = torso.getWidth() + 12;
		ans.height = (int) (bones[BodyParts.Head.num].getHeight());

		return ans;
	}

	public void run(int dir) {
		if (!frozen && (animation != animations.get("Crouching")) && !airborne) {
			this.dir = dir;
			if (dir == -1) {
				flippingLB = true;
			} else if (dir == 1) {
				flippingLB = false;
			}
			animation = animations.get("Running");
		} else if (!frozen && airborne) {
			this.dir = dir;
			velocity.X = dir * Soldier.SPEED;
			if (dir == -1) {
				flippingLB = true;
			} else if (dir == 1) {
				flippingLB = false;
			}
		}
	}

	public void stop() {
		if (!frozen && (animation != animations.get("Crouching")) && !airborne) {
			dir = 0;
			animation = animations.get("Standing");
		} else if (!frozen && airborne) {
			dir = 0;
		}
	}

	public void crouch() {
		if (animation != animations.get("Crouching") && !frozen && !airborne) {
			torso.position.Y += 12;
			animation = animations.get("Crouching");
			velocity.X = 0;
			velocity.Y = 0;
			dir = 0;
		}
	}

	public void jump(int dir) {
		acceleration.Y = Global.GACCEL;
		velocity.Y = -.5f;
		airborne = true;
		animation = animations.get("Jumping");
	}

	public void land() {
		acceleration = new Vector2();
		velocity.Y = 0;
		airborne = false;

		if (dir == 1)
			run(1);
		else if (dir == -1)
			run(-1);
		else
			stop();
	}

	public void uncrouch(int dir) {
		if (animation == animations.get("Crouching") && !frozen) {
			animation = animations.get("Standing");
			torso.position.Y -= 12;
			this.dir = dir;
			if (dir == -1) {
				flippingLB = true;
				animation = animations.get("Running");
			} else if (dir == 1) {
				flippingLB = false;
				animation = animations.get("Running");
			}
		}
	}

	public int switchWeapon() {
		if (currentWeapon == weapons[0]) {
			if (frozen) {
				return 0;
			}
			currentWeapon = weapons[1];
			return 1;
		} else {
			if (frozen) {
				return 1;
			}
			currentWeapon = weapons[0];
			return 0;
		}
	}

	public boolean shoot() {
		if (currentWeapon.bulletsLeft != 0 && currentWeapon.roundDenom != 0
				&& currentWeapon.reloadTimePassed == -1 && !frozen
				&& !currentWeapon.melee) {
			currentWeapon.fire(Global.game.main.bullets);
			return true;
		} else if (currentWeapon.reloadTimePassed == -1 && !frozen
				&& currentWeapon.melee) {
			currentWeapon.fire(Global.game.main.bullets);
			return false;
		}
		return false;
	}
}
