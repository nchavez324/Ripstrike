package project.model.util;

import project.model.gfx.Grenade;
import project.model.gfx.Soldier;
import project.model.gfx.Weapon;
import project.model.gfx.Soldier.Faction;

public class Human extends Player {

	public Human(String name, Faction faction) {
		this.name = name;
		soldier = new Soldier(faction, this);
	}

	public Human(String name, Faction faction, Weapon.Type[] stock,
			Grenade.Type grenadeType, int numGrenades) {
		this.name = name;
		soldier = new Soldier(faction, stock, grenadeType, numGrenades, this);
	}

}
