package project.model.gfx.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

import project.model.gfx.core.Component;
import project.model.util.Global;
import project.model.util.Match;

public class MatchUI implements Component {

	public static int maxNumTeams = 4;
	public static int barLen = 50;

	public Image matchBase;
	public Image teamBase;
	public Match match;

	public MatchUI(Match match) {
		this.match = match;
		loadResources();
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(teamBase, Global.leftInset + Global.screenWidth - teamBase.getWidth(null), Global.topInset, null);
		for (int i = 0; i < match.teams.length; i++) {
			int w = (int) (barLen * (match.numKills[i] / (float) match.killsToWin));
			if (w < 0)
				w = 0;
			for (int j = 0; j < match.teams[i].length; j++) {
				if(match.teams[i] != null && match.teams[i][j] != null)
				g.drawImage(
						Global.depot.ui.get(match.teams[i][j].toString()),
						Global.leftInset + 760 - (j * 32),
						Global.topInset + 10 + (i * 50),
						Global.leftInset
								+ 760
								- (j * 32)
								+ Global.depot.ui.get(
										match.teams[i][j].toString()).getWidth(
										null) / 2,
						Global.topInset
								+ 10
								+ (i * 50)
								+ Global.depot.ui.get(
										match.teams[i][j].toString())
										.getHeight(null) / 2, 0, 0,
						Global.depot.ui.get(match.teams[i][j].toString())
								.getWidth(null),
						Global.depot.ui.get(match.teams[i][j].toString())
								.getHeight(null), null);
			}
			g.setColor(new Color(170, 170, 170));
			g.fillRect(Global.leftInset + 800 - w, Global.topInset + 49
					+ (i * 50), w, 5);
			g.setColor(Color.WHITE);
			g.drawRect(Global.leftInset + 800 - w, Global.topInset + 49
					+ (i * 50), w, 5);
			Font f = g.getFont();
			g.setFont(new Font(null, Font.PLAIN, 10));
			g.drawString(match.numKills[i] + "", Global.leftInset + 800 - w
					- 15, Global.topInset + 55 + (i * 50));
			g.setFont(f);
		}
		Font f = g.getFont();
		g.setFont(new Font(null, Font.BOLD, 12));
		g.drawImage(matchBase, Global.leftInset, Global.topInset, null);
		g.drawString(Global.game.main.gameOptions.mapName.replace('_', ' ') + ":", Global.leftInset + 4, Global.topInset + 15);
		g.drawString(match.mode.toString().replace('_', ' '), Global.leftInset + 4, Global.topInset + 30);
		g.setFont(f);
	}

	@Override
	public void update(long timePassed) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadResources() {
		matchBase = Global.depot.ui.get("matchBase");
		teamBase = Global.depot.ui.get("teamBase");
	}

}
