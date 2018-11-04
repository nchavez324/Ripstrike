package project.model.util;

import project.model.gfx.Grenade;
import project.model.gfx.Soldier.Faction;
import project.model.gfx.Weapon;

public class PlayerOptions {

	public Weapon.Type[] weapons;

	public Grenade.Type grenade;

	public Faction faction;

	public int numGrenades;

	public PlayerOptions(Faction faction, Weapon.Type[] weapons,
			Grenade.Type grenade, int numGrenades) {
		this.weapons = weapons;
		this.grenade = grenade;
		this.faction = faction;
		this.numGrenades = numGrenades;
	}

}
