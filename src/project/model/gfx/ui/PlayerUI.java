/*
 * Copyright (c) 2011 BleepBloop Software, All Rights Reserved
 *
 * Unpublished copyright.  All rights reserved.
 */
package project.model.gfx.ui;

import java.awt.Graphics2D;
import project.model.gfx.core.Component;
import project.model.gfx.ui.menus.PauseMenu;
import project.model.util.Match;
import project.model.util.Player;

/**
 * TODO: type comment.
 * 
 * @version Aug 17, 2011, submitted by Nick Chavez
 */
public class PlayerUI implements Component {

	public WeaponBase weaponBase;

	public HealthMeter health;

	public Player player;

	public MatchUI match;

	public PauseMenu pausemenu;

	public PlayerUI(Player player, Match match) {
		this.player = player;
		this.match = new MatchUI(match);
		loadResources();
	}

	@Override
	public void draw(Graphics2D g) {
		health.draw(g);
		weaponBase.draw(g);
		pausemenu.draw(g);
		if (match != null)
			match.draw(g);
	}

	@Override
	public void update(long timePassed) {
		if (!pausemenu.visible) {
			weaponBase.update(timePassed);
		}
		pausemenu.update(timePassed);
		if(match != null)
			match.update(timePassed);
	}

	public void loadResources() {
		int w = 0;
		if (player.soldier.currentWeapon == player.soldier.weapons[0])
			w = 0;
		else
			w = 1;
		weaponBase = new WeaponBase(player.soldier.weapons, w);
		health = new HealthMeter(player);
		pausemenu = new PauseMenu();
		pausemenu.setVisible(false);
	}
}
