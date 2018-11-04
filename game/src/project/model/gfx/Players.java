package project.model.gfx;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import project.model.gfx.Soldier.Faction;
import project.model.gfx.core.Component;
import project.model.util.Player;

public class Players implements Component {

	public ArrayList<Player> players;

	public int numSoldiers;

	public Players() {
		players = new ArrayList<Player>();
	}

	@Override
	public void draw(Graphics2D g) {
		for (Player p : players) {
			if (p.soldier != null)
				p.soldier.draw(g);
		}
	}

	@Override
	public void update(long timePassed) {
		Iterator<Player> itr = players.iterator();
		while (itr.hasNext()) {
			Player p = itr.next();
			if (p.soldier != null) {
				p.soldier.update(timePassed);
				if (p.soldier.dead && !p.soldier.torso.player) {
					p.soldier = null;
				}
			}
		}
	}

	@Override
	public void loadResources() {
		//
	}

	public void add(Player p) {
		players.add(p);
		numSoldiers = players.size();
	}

	public void syncTeams(Faction[][] teams) {
		int[] teamCat = new int[Faction.values().length];
		for (int i = 0; i < teams.length; i++) {
			for (int j = 0; j < teams[i].length; j++) {
				if(teams[i][j] != null)
					teamCat[teams[i][j].num] = i;
			}
		}
		for (Player p : players) {
			p.team = teamCat[p.soldier.faction.num];
		}
	}
	public Player getPlayer(){
		for(Player p : players){
			if(p.name.compareTo("Local Player") == 0)
				return p; 
		}
		return null;
	}
}
