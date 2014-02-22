/*
 * Copyright (c) 2011 BleepBloop Software, All Rights Reserved
 *
 * Unpublished copyright.  All rights reserved.
 */
package project.model.gfx.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import project.model.gfx.Weapon;
import project.model.util.Global;
import project.model.util.Vector2;

/**
 * TODO: type comment.
 * 
 * @version Aug 24, 2011, submitted by Nick Chavez
 */
public class WeaponBase {

	public Image weaponBase;

	public Weapon weapon;

	public int w;

	public Image weaponImage;

	public AmmoIconSet[] sets;

	public AmmoIconSet ammo;

	public Weapon[] stock;

	public WeaponBase(Weapon[] stock, int w) {
		this.stock = stock;
		this.weapon = stock[w];
		weaponImage = weapon.sheet;
		weaponBase = Global.depot.ui.get("weaponbase");
		initSets();

	}

	public void draw(Graphics2D g) {
		g.drawImage(weaponBase, Global.leftInset + Global.screenWidth
				- (weaponBase.getWidth(null)), Global.screenHeight
				- (weaponBase.getHeight(null)), Global.screenWidth,
				Global.screenHeight, 0, 0, weaponBase.getWidth(null),
				weaponBase.getHeight(null), null);

		drawWeaponName(g);
		if (weapon.melee) {
			AffineTransform xform = AffineTransform.getTranslateInstance(
					Global.leftInset + Global.screenWidth
							- weaponBase.getWidth(null) + 117
							- weaponImage.getWidth(null) / 2,
					Global.screenHeight - (weaponBase.getHeight(null)) + 17);
			xform.rotate(3 * Math.PI / 2);
			g.drawImage(weaponImage, xform, null);
		} else {
			g.drawImage(
					weaponImage,
					Global.leftInset + Global.screenWidth
							- weaponBase.getWidth(null) + 131
							- weaponImage.getWidth(null) / 2,
					Global.screenHeight - (weaponBase.getHeight(null)) + 7,
					Global.leftInset + Global.screenWidth
							- weaponBase.getWidth(null) + 131
							+ weaponImage.getWidth(null) / 2,
					Global.screenHeight - (weaponBase.getHeight(null)) + 7
							+ weaponImage.getHeight(null), 0, 0,
					weaponImage.getWidth(null), weaponImage.getHeight(null),
					null);
			drawWeaponAmmoCount(g);
			ammo.draw(g);
		}

	}

	public void drawWeaponName(Graphics2D g) {
		String name = weapon.type.toString().replaceAll("_", " ");
		g.setColor(Color.WHITE);
		FontMetrics fm = g.getFontMetrics(g.getFont());
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(name, g);

		int x = (165 - (int) (rect.getWidth())) / 2 + Global.leftInset
				+ Global.screenWidth - ((weaponBase.getWidth(null))) + 104
				- (int) rect.getWidth() / 2;
		int y = (15 - (int) (rect.getHeight())) / 2 + fm.getAscent()
				+ Global.screenHeight - (weaponBase.getHeight(null)) + 50;
		g.drawString(name, x, y);
	}

	public void drawWeaponAmmoCount(Graphics2D g) {
		g.setColor(Color.WHITE);
		FontMetrics fm = g.getFontMetrics(g.getFont());
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(weapon.bulletsLeft
				+ "/" + weapon.roundDenom, g);
		int x = (165 - (int) (rect.getWidth())) / 2 + Global.leftInset
				+ Global.screenWidth - ((weaponBase.getWidth(null))) + 48;
		int y = (15 - (int) (rect.getHeight())) / 2 + fm.getAscent()
				+ Global.screenHeight - (weaponBase.getHeight(null)) + 30;
		if (weapon.bulletsLeft <= 0 || weapon.reloadTimePassed != -1) {
			g.setColor(Color.RED);
		}
		g.drawString(weapon.bulletsLeft + "/" + weapon.roundDenom, x, y);
		g.setColor(Color.WHITE);
	}

	public void update(long timePassed) {
		if (!weapon.melee)
			ammo.update(timePassed);
	}

	public void initSets() {
		sets = new AmmoIconSet[stock.length];
		for (int i = 0; i < stock.length; i++) {
			if (!stock[i].melee) {
				sets[i] = new AmmoIconSet(stock[i]);
				sets[i].position = new Vector2(Global.leftInset
						+ Global.screenWidth - (weaponBase.getWidth(null)) + 75
						- (sets[i].getWidth() - sets[i].getDiag()),
						Global.screenHeight - (weaponBase.getHeight(null)) + 27
								- sets[i].getHeight() / 2);
			}
		}
		ammo = sets[w];
	}

	public void switchWeapon(int w) {
		this.weapon = stock[w];
		this.w = w;
		ammo = sets[w];
		weaponImage = weapon.sheet;
	}
}
