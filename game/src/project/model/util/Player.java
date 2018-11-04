
package project.model.util;

import project.model.gfx.Soldier;


public abstract class Player {

	public int numKills = 0;
	public int team;
	public Soldier soldier;
	public String name = "<default>";
	
	public int getTeam(){
		return team;
	}
}
