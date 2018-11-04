package project.model.util;

import project.model.gfx.Players;
import project.model.gfx.Soldier.Faction;
import project.model.util.Match.Mode;

public class GameOptions {

	public Mode mode;
	public Faction[][] teams;
	public Players players;
	public String mapName;
	public int killsToWin;
	
	public GameOptions(Mode mode, Faction[][] teams, Players players, String mapName, int killsToWin){
		this.mode = mode;
		this.teams = teams;
		this.players = players;
		this.mapName = mapName;
		this.killsToWin = killsToWin;
	}
}
