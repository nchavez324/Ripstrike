package project.model.util;

import project.model.gfx.Grenade;
import project.model.gfx.NPCSoldier;
import project.model.gfx.Weapon;
import project.model.gfx.Soldier.Faction;

public class NPC extends Player {

	public NPC(String name, Faction faction) {
		this.name = name;
		soldier = new NPCSoldier(faction, this);
	}

	public NPC(String name, Faction faction, Weapon.Type[] stock,
			Grenade.Type grenadeType, int numGrenades) {
		this.name = name;
		soldier = new NPCSoldier(faction, stock, grenadeType, numGrenades, this);
	}

}
