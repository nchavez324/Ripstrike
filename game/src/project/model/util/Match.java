package project.model.util;

import project.model.gfx.Players;
import project.model.gfx.Soldier.Faction;

public class Match {

	public enum Mode {
		Team_Deathmatch(0), Capture_The_Flag(1), Free_For_All(2);
		public int num;

		Mode(int num) {
			this.num = num;
		}
	}
	public Mode mode;
	public Faction[][] teams;
	public Players players;
	public int[] numKills;
	public int killsToWin;

	public Match(Mode mode, Faction[][] teams, int killsToWin, Players players) {
		this.mode = mode;
		this.teams = teams;
		this.killsToWin = killsToWin;
		this.players = players;
		this.numKills = new int[teams.length];
	}
}
