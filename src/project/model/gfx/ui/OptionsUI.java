package project.model.gfx.ui;

import Resources.ResourceLoader;
import project.model.gfx.Grenade;
import project.model.gfx.Players;
import project.model.gfx.Soldier;
import project.model.gfx.Soldier.Faction;
import project.model.gfx.Weapon;
import project.model.gfx.core.Component;
import project.model.gfx.core.Sprite;
import project.model.gfx.ui.menus.Popup;
import project.model.util.GameOptions;
import project.model.util.Human;
import project.model.util.Match;
import project.model.util.NPC;
import project.model.util.Player;
import project.model.util.PlayerOptions;
import project.model.util.Global;
import project.model.util.Vector2;
import project.model.util.Match.Mode;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Random;

public class OptionsUI implements Component {

    public static int SOLDIER = 0;

    public HashMap<String, Button> buttons;

    public HashMap<Integer, Component> components;

    int offset;

    public Vector2 position;

    public Vector2 velocity;

    public ResourceLoader depot = Global.depot;

    // Options
    
    public Mode mode = Match.Mode.Team_Deathmatch;
    
    public Faction[][] teams = {
			{Faction.Ajax, Faction.Sierra, Faction.SRK},
			{Faction.New_Federation}
	};
    
    
    public int[] playerOrg;
    
    public int[] teamNums;
    
    public int[] playerNums;
    
    public Vector2[][] spawns;
    
    public Image[] modeLogos;
    
    public Players players;
    
    public int maxNumPlayers = 2;
    
    public int[] numPlayers;
    
    public String[] mapNames = {
    		"The_Grid", "Green_Hill", "Hidden_Valley"
    };
    
    public Mode[] modes = Match.Mode.values();
    
    public int mapNum = 0;
        
    public int killsToWin = 3;
    
    public Faction team;

    public StatMetric[][] stats;

    public int wp0, wp1, g;

    public Weapon.Type[][] weapons;

    public Grenade.Type[] grenades;

    public Sprite[] factionLogos;

    public Sprite teamLogo, teamArrow, weapon0, weapon1, grenade;

    public Sprite bg;

    public Popup popup;

    public Soldier soldier;

    public OptionsUI() {
        loadResources();
    }

    @Override
    public void draw(Graphics2D g) {
        bg.draw(g);
        String[] keys = buttons.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            buttons.get(keys[i]).draw(g);
        }
        g.drawImage(factionLogos[team.num].singleImg,
                (int) teamLogo.position.X - 2 * teamLogo.singleImg.getWidth(null), (int) teamLogo.position.Y - 2
                        * factionLogos[team.num].singleImg.getHeight(null), (int) teamLogo.position.X + 2
                        * factionLogos[team.num].singleImg.getWidth(null), (int) teamLogo.position.Y + 2
                        * factionLogos[team.num].singleImg.getHeight(null), 0, 0,
                factionLogos[team.num].singleImg.getWidth(null), factionLogos[team.num].singleImg.getHeight(null), null);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        AffineTransform x = AffineTransform.getTranslateInstance(teamArrow.position.X, teamArrow.position.Y);
        x.concatenate(AffineTransform.getRotateInstance(-Math.PI / 2));
        g.drawImage(teamArrow.singleImg, x, null);
        for (int i = 0; i < factionLogos.length; i++) {
            factionLogos[i].draw(g);
        }

        g.drawImage(weapon0.singleImg, (int) (weapon0.position.X - 1 * weapon0.singleImg.getWidth(null)),
                (int) (weapon0.position.Y - 1 * weapon0.singleImg.getHeight(null)),
                (int) (weapon0.position.X + 1 * weapon0.singleImg.getWidth(null)),
                (int) (weapon0.position.Y + 1 * weapon0.singleImg.getHeight(null)), 0, 0,
                weapon0.singleImg.getWidth(null), weapon0.singleImg.getHeight(null), null);

        g.drawImage(weapon1.singleImg, (int) (weapon1.position.X - 1 * weapon1.singleImg.getWidth(null)),
                (int) (weapon1.position.Y - 1 * weapon1.singleImg.getHeight(null)),
                (int) (weapon1.position.X + 1 * weapon1.singleImg.getWidth(null)),
                (int) (weapon1.position.Y + 1 * weapon1.singleImg.getHeight(null)), 0, 0,
                weapon1.singleImg.getWidth(null), weapon1.singleImg.getHeight(null), null);
        g.drawImage(grenade.singleImg, (int) (grenade.position.X - 1 * grenade.singleImg.getWidth(null)),
                (int) (grenade.position.Y - 1 * grenade.singleImg.getHeight(null)),
                (int) (grenade.position.X + 1 * grenade.singleImg.getWidth(null)),
                (int) (grenade.position.Y + 1 * grenade.singleImg.getHeight(null)), 0, 0,
                grenade.singleImg.getWidth(null), grenade.singleImg.getHeight(null), null);
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics(g.getFont());
        java.awt.geom.Rectangle2D rect = fm.getStringBounds(team.toString().replace('_', ' ') + " Soldier", g);
        java.awt.geom.Rectangle2D rect2 = fm.getStringBounds(weapons[0][wp0].toString().replace('_', ' '), g);
        java.awt.geom.Rectangle2D rect3 = fm.getStringBounds(weapons[1][wp1].toString().replace('_', ' '), g);
        java.awt.geom.Rectangle2D rect4 = fm.getStringBounds(grenades[this.g].toString() + " " + "Grenade", g);

        g.drawString(team.toString().replace("_", " ") + " Soldier", teamLogo.position.X - (float) rect.getWidth() / 2,
                teamLogo.position.Y + 150);
        g.drawString(weapons[0][wp0].toString().replace('_', ' '), weapon0.position.X - (float) rect2.getWidth() / 2,
                weapon0.position.Y + 50);
        g.drawString(weapons[1][wp1].toString().replace('_', ' '), weapon1.position.X - (float) rect3.getWidth() / 2,
                weapon1.position.Y + 50);
        g.drawString(grenades[this.g].toString() + " " + "Grenade", grenade.position.X - (float) rect4.getWidth() / 2,
                grenade.position.Y + 50);
        for (int i = 0; i < stats[0].length; i++) {
            if (stats != null && stats[0] != null && stats[1] != null && stats[0][i] != null && stats[1][i] != null) {
                stats[0][i].draw(g);
            }
            if (stats != null && stats[0] != null && stats[1] != null && stats[0][i] != null && stats[1][i] != null) {
                stats[1][i].draw(g);
            }
        }
        
        g.setColor(Color.white);
        java.awt.geom.Rectangle2D rect5 = fm.getStringBounds(mapNames[mapNum].toString().replace('_', ' '), g);
        g.drawString(mapNames[mapNum].toString().replace('_', ' '), position.X + Global.leftInset - 3 + Global.screenWidth + 562 - (float) rect5.getWidth() / 2,
                position.Y + Global.topInset - 29 + 360);
        Rectangle mapBounds = new Rectangle((int)(position.X + Global.leftInset - 3 + Global.screenWidth + 388), (int)(position.Y + Global.topInset - 29 + 80), 350, 250);
        //g.drawRect(mapBounds.x, mapBounds.y, mapBounds.width, mapBounds.height);
        Image mapBG = Global.depot.maps.get(mapNames[mapNum]).bg;
        Image mapImg = Global.depot.maps.get(mapNames[mapNum]).layers[0];
        g.drawImage(mapBG, mapBounds.x, mapBounds.y, mapBounds.x + mapBounds.width, mapBounds.y + mapBounds.height, 0, 0, mapBG.getWidth(null), mapBG.getHeight(null), null);
        g.drawImage(mapImg, mapBounds.x, mapBounds.y, mapBounds.x + mapBounds.width, mapBounds.y + mapBounds.height, (mapImg.getWidth(null) - mapBounds.width)/2, mapImg.getHeight(null) - mapBounds.height, ((mapImg.getWidth(null) - mapBounds.width)/2) + mapBounds.width, mapImg.getHeight(null), null);
        
        java.awt.geom.Rectangle2D rect6 = fm.getStringBounds(modes[mode.num].toString().replace('_', ' '), g);
        g.drawString(modes[mode.num].toString().replace('_', ' '), position.X + Global.leftInset - 3 + Global.screenWidth + 562 - (float) rect6.getWidth() / 2,
                position.Y + Global.topInset - 29 + 558);
        g.drawImage(modeLogos[mode.num], (int)(position.X + Global.leftInset - 3 + Global.screenWidth + 532), (int)(position.Y + Global.topInset - 29 + 450), null);
        
        for(int i = 0; i < numPlayers.length; i++){
        	for(int j = 0; j < numPlayers[i]; j++){
        		g.drawString((j + 1) + ": CPU " + (j + 1), position.X + Global.leftInset - 3 + Global.screenWidth + 80, position.Y + Global.topInset - 29 + 70 + 150*i + 20 * j);	 
        	}
        }
        
        for(int i = 0; i < teamNums.length; i++){
        	java.awt.geom.Rectangle2D rect7;
        	String m = teamNums[i] + "";
        	if(teamNums[i] == 0)m = "";
        	rect7 = fm.getStringBounds(m, g);
            g.drawString(m, (int)(position.X + Global.leftInset - 3 + Global.screenWidth + 282 - rect7.getWidth()/2), (int)(Global.topInset - 29 + 90 + 150*i));
        }
        
        AffineTransform orig = (AffineTransform) g.getTransform().clone();
        AffineTransform t = AffineTransform.getScaleInstance(1.75, 1.75);
        t.translate(-soldier.getPosition().X / 1.75 + 15, -soldier.getPosition().Y / 1.75 + 50);
        g.setTransform(t);
        soldier.draw(g);
        g.setTransform(orig);
        if (popup != null)
            popup.draw(g);

    }

    @Override
    public void update(long timePassed) {
        if (popup == null || !popup.visible) {
            position.X += velocity.X * timePassed;
            position.Y += velocity.Y * timePassed;
            String[] keys = buttons.keySet().toArray(new String[0]);
            for (int i = 0; i < keys.length; i++) {
                buttons.get(keys[i]).update(timePassed);
            }
            for (int i = 0; i < factionLogos.length; i++) {
                factionLogos[i].update(timePassed);
            }
            soldier.update(timePassed);
            teamLogo.update(timePassed);

            teamArrow.update(timePassed);
            weapon0.update(timePassed);
            weapon1.update(timePassed);
            grenade.update(timePassed);
            for (int i = 0; i < stats[0].length; i++) {
                if (stats != null && stats[0] != null && stats[1] != null && stats[0][i] != null && stats[1][i] != null) {
                    stats[0][i].update(timePassed);
                    stats[1][i].update(timePassed);
                }
            }
            bg.update(timePassed);
            if (velocity.X == 0 && popup != null) {
                popup.visible = true;
            }
        }
    }

    @Override
    public void loadResources() {
        offset = 125;

        bg = new Sprite(Global.depot.ui.get("menuBG"));

        Button next1 = new Button(depot.ui.get("button"), "Next", 75, true);
        Button play = new Button(depot.ui.get("button"), "Play!", 50, true);
        Button back = new Button(depot.ui.get("button"), "Back", 50, true);
        Button LW1 = new Button(depot.ui.get("arrowButton"), "LW1", true, false);
        Button RW1 = new Button(depot.ui.get("arrowButton"), "RW1", true, true);
        Button LW2 = new Button(depot.ui.get("arrowButton"), "LW2", true, false);
        Button RW2 = new Button(depot.ui.get("arrowButton"), "RW2", true, true);
        Button LG = new Button(depot.ui.get("arrowButton"), "LG", true, false);
        Button RG = new Button(depot.ui.get("arrowButton"), "RG", true, true);
        
        Button LM = new Button(depot.ui.get("arrowButton"), "LM", true, false);
        Button RM = new Button(depot.ui.get("arrowButton"), "RM", true, true);
        Button LMT = new Button(depot.ui.get("arrowButton"), "LMT", true, false);
        Button RMT = new Button(depot.ui.get("arrowButton"), "RMT", true, true);

        Button LT1 = new Button(depot.ui.get("arrowButton"), "LT1", true, false);
        Button RT1 = new Button(depot.ui.get("arrowButton"), "RT1", true, true);
        Button LT2 = new Button(depot.ui.get("arrowButton"), "LT2", true, false);
        Button RT2 = new Button(depot.ui.get("arrowButton"), "RT2", true, true);
        Button LT3 = new Button(depot.ui.get("arrowButton"), "LT3", true, false);
        Button RT3 = new Button(depot.ui.get("arrowButton"), "RT3", true, true);
        Button LT4 = new Button(depot.ui.get("arrowButton"), "LT4", true, false);
        Button RT4 = new Button(depot.ui.get("arrowButton"), "RT4", true, true);
        
        Button DT1 = new Button(depot.ui.get("arrowButtonVert"), "DT1", true, false, true);
        Button UT1 = new Button(depot.ui.get("arrowButtonVert"), "UT1", true, false);
        Button DT2 = new Button(depot.ui.get("arrowButtonVert"), "DT2", true, false, true);
        Button UT2 = new Button(depot.ui.get("arrowButtonVert"), "UT2", true, false);
        Button DT3 = new Button(depot.ui.get("arrowButtonVert"), "DT3", true, false, true);
        Button UT3 = new Button(depot.ui.get("arrowButtonVert"), "UT3", true, false);
        Button DT4 = new Button(depot.ui.get("arrowButtonVert"), "DT4", true, false, true);
        Button UT4 = new Button(depot.ui.get("arrowButtonVert"), "UT4", true, false);
        
        buttons = new HashMap<String, Button>();
        buttons.put("Next", next1);
        buttons.put("Play", play);
        buttons.put("Back", back);
        buttons.put("LW1", LW1);
        buttons.put("RW1", RW1);
        buttons.put("LW2", LW2);
        buttons.put("RW2", RW2);
        buttons.put("LG", LG);
        buttons.put("RG", RG);
        buttons.put("LM", LM);
        buttons.put("RM", RM);
        buttons.put("LMT", LMT);
        buttons.put("RMT", RMT);
        
        buttons.put("LT1", LT1);
        buttons.put("RT1", RT1);
        buttons.put("LT2", LT2);
        buttons.put("RT2", RT2);
        buttons.put("LT3", LT3);
        buttons.put("RT3", RT3);
        buttons.put("LT4", LT4);
        buttons.put("RT4", RT4);
        
        buttons.put("UT1", UT1);
        buttons.put("DT1", DT1);
        buttons.put("UT2", UT2);
        buttons.put("DT2", DT2);
        buttons.put("UT3", UT3);
        buttons.put("DT3", DT3);
        buttons.put("UT4", UT4);
        buttons.put("DT4", DT4);

        team = Faction.New_Federation;
        wp0 = Weapon.Type.values()[0].num;
        wp1 = Weapon.Type.values()[Weapon.pCutoff + 1].num;
        g = Grenade.Type.Frag.num;
        Weapon.Type[] types = { Weapon.Type.values()[wp0], Weapon.Type.values()[wp1] };
        soldier = new Soldier(team, types, Grenade.Type.values()[g], 3, new NPC("UI", team));
        soldier.torso.player = false;

        components = new HashMap<Integer, Component>();

        factionLogos = new Sprite[Faction.values().length];
        for (int i = 0; i < factionLogos.length; i++) {
            factionLogos[i] = new Sprite(Global.depot.ui.get(Faction.values()[i].toString()));
        }
        teamLogo = new Sprite(factionLogos[team.num].singleImg);

        teamArrow = new Sprite(Global.depot.ui.get("arrow"));

        weapons = new Weapon.Type[2][Weapon.Type.values().length];
        grenades = Grenade.Type.values();

        for (int i = 0; i < weapons.length; i++) {
            for (int j = 0; j < weapons[i].length; j++) {
                weapons[i][j] = Weapon.Type.values()[j];
            }
        }

        modeLogos = new Image[Mode.values().length];
        for(int i = 0; i < modeLogos.length; i++){
        	modeLogos[i] = Global.depot.ui.get(Mode.values()[i].toString());
        }
        teamNums = new int[Faction.values().length];
        for(int i = 0; i < teamNums.length; i++)
        	teamNums[i] = 0;
        teamNums[0] = 1;
        
        playerNums = new int[Faction.values().length];
        for(int i = 0; i < teamNums.length; i++)
        	playerNums[i] = 0;
        playerNums[team.num] = 1;
        
        numPlayers = new int[Faction.values().length];
        
        //Really bad hacky way of doing this...
        spawns = new Vector2[3][5];
        spawns[0][0] = new Vector2(805, 174);
        spawns[0][1] = new Vector2(982, 142);
        spawns[0][2] = new Vector2(1159, 151);
        spawns[0][3] = new Vector2(612, 110);
        spawns[0][4] = new Vector2(476, 147);
        spawns[1][0] = new Vector2(805, 174);
        spawns[1][1] = new Vector2(982, 142);
        spawns[1][2] = new Vector2(1159, 151);
        spawns[1][3] = new Vector2(612, 110);
        spawns[1][4] = new Vector2(476, 147);
        
        spawns[2][0] = new Vector2(1020, 110);
        spawns[2][1] = new Vector2(1550, 110);
        spawns[2][2] = new Vector2(2130, 110);
        spawns[2][3] = new Vector2(520, -274);
        spawns[2][4] = new Vector2(235, -146);
        
        popup = new Popup(Global.depot.ui.get("popup"),
                "Use the WASD keys and the mouse! $  $ Select your soldier and his weapons, and then select a game mode and a map!");
        components.put(SOLDIER, soldier);
        refreshSoldier();

    }

    public void refreshSoldier() {
        weapon0 = new Sprite(Global.depot.weapons.get(weapons[0][wp0].toString()));
        weapon1 = new Sprite(Global.depot.weapons.get(weapons[1][wp1].toString()));
        grenade = new Sprite(Global.depot.grenades.get(grenades[g].toString()));

        String[] wNames = { "Accuracy", "Damage", "Bullets/Clip", "Rate Of Fire", "Reload Time" };
        int[][] wVals = new int[2][5];
        for (int i = 0; i < wVals.length; i++) {
            int w = 0;
            if (i == 0)
                w = wp0;
            else
                w = wp1;
            wVals[i][0] = (int) ((Global.depot.weaponData.get(weapons[i][w].toString()).accuracy / 1f) * 100);
            wVals[i][1] = (int) ((Global.depot.weaponData.get(weapons[i][w].toString()).damage / 5f) * 100);
            wVals[i][2] = ((Global.depot.weaponData.get(weapons[i][w].toString()).bulletsPerClip));
            wVals[i][3] = (int) ((1 - Global.depot.weaponData.get(weapons[i][w].toString()).rateOfFire / 500f) * 100);
            wVals[i][4] = (int) ((1 - Global.depot.weaponData.get(weapons[i][w].toString()).reloadTime / 5000f) * 100);
        }
        stats = new StatMetric[2][wNames.length];
        for (int i = 0; i < stats[0].length; i++) {
            stats[0][i] = new StatMetric(wVals[0][i], 100, 5, wNames[i]);
            stats[1][i] = new StatMetric(wVals[1][i], 100, 5, wNames[i]);
        }
        Weapon.Type[] types = { Weapon.Type.values()[wp0], Weapon.Type.values()[wp1] };
        soldier = new Soldier(team, types, Grenade.Type.values()[g], 3, new NPC("UI", team));
        soldier.torso.player = false;
        if(playerNums != null)
        	playerNums[team.num] = 1;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        Button next1 = buttons.get("Next");
        Button play = buttons.get("Play");
        Button back = buttons.get("Back");
        Button LW1 = buttons.get("LW1");
        Button RW1 = buttons.get("RW1");
        Button LW2 = buttons.get("LW2");
        Button RW2 = buttons.get("RW2");
        Button LG = buttons.get("LG");
        Button RG = buttons.get("RG");
        Button LM = buttons.get("LM");
        Button RM = buttons.get("RM");
        Button LMT = buttons.get("LMT");
        Button RMT = buttons.get("RMT");
        
        Button LT1 = buttons.get("LT1");
        Button RT1 = buttons.get("RT1");
        Button LT2 = buttons.get("LT2");
        Button RT2 = buttons.get("RT2");
        Button LT3 = buttons.get("LT3");
        Button RT3 = buttons.get("RT3");
        Button LT4 = buttons.get("LT4");
        Button RT4 = buttons.get("RT4");
        
        Button UT1 = buttons.get("UT1");
        Button DT1 = buttons.get("DT1");
        Button UT2 = buttons.get("UT2");
        Button DT2 = buttons.get("DT2");
        Button UT3 = buttons.get("UT3");
        Button DT3 = buttons.get("DT3");
        Button UT4 = buttons.get("UT4");
        Button DT4 = buttons.get("DT4");
        next1.setPosition(new Vector2(position.X + Global.leftInset - 3 + 580 + offset, position.Y + 540
                + Global.topInset - 29));
        play.setPosition(new Vector2(position.X + Global.leftInset - 3 + 580 + offset + 20 + Global.screenWidth, position.Y + 540
                + Global.topInset - 29));
        back.setPosition(new Vector2(position.X + Global.leftInset - 3 + 580 + offset + 20 + Global.screenWidth, position.Y + 470
                + Global.topInset - 29));
        LW1.setPosition(new Vector2(position.X + Global.leftInset - 3 + 445, position.Y + 132 + Global.topInset - 29));
        RW1.setPosition(new Vector2(position.X + Global.leftInset - 3 + 635, position.Y + 132 + Global.topInset - 29));
        LW2.setPosition(new Vector2(position.X + Global.leftInset - 3 + 445, position.Y + 282 + Global.topInset - 29));
        RW2.setPosition(new Vector2(position.X + Global.leftInset - 3 + 635, position.Y + 282 + Global.topInset - 29));
        LG.setPosition(new Vector2(position.X + Global.leftInset - 3 + 445, position.Y + 432 + Global.topInset - 29));
        RG.setPosition(new Vector2(position.X + Global.leftInset - 3 + 635, position.Y + 432 + Global.topInset - 29));
        LM.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + 345, position.Y + 150 + Global.topInset - 29));
        RM.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + 738, position.Y + 150 + Global.topInset - 29));
        LMT.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + 420, position.Y + 450 + Global.topInset - 29));
        RMT.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + 663, position.Y + 450 + Global.topInset - 29));
        
        int lX = 225, rX = 300;
        LT1.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + lX, position.Y + 70 + Global.topInset - 29));
        RT1.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + rX, position.Y + 70 + Global.topInset - 29));
        LT2.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + lX, position.Y + 220 + Global.topInset - 29));
        RT2.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + rX, position.Y + 220 + Global.topInset - 29));
        LT3.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + lX, position.Y + 370 + Global.topInset - 29));
        RT3.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + rX, position.Y + 370 + Global.topInset - 29));
        LT4.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + lX, position.Y + 520 + Global.topInset - 29));
        RT4.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + rX, position.Y + 520 + Global.topInset - 29));
        
        int x = 190, dy = 50;
        UT1.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + x, position.Y + 54 + Global.topInset - 29));
        DT1.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + x, position.Y + 54 + dy + Global.topInset - 29));
        UT2.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + x, position.Y + 204 + Global.topInset - 29));
        DT2.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + x, position.Y + 204 + dy + Global.topInset - 29));
        UT3.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + x, position.Y + 354 + Global.topInset - 29));
        DT3.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + x, position.Y + 354 + dy + Global.topInset - 29));
        UT4.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + x, position.Y + 504 + Global.topInset - 29));
        DT4.setPosition(new Vector2(position.X + Global.leftInset - 3 + Global.screenWidth + x, position.Y + 504 + dy + Global.topInset - 29));
        
        for (int i = 0; i < factionLogos.length; i++) {
            factionLogos[i].position.X = position.X + Global.leftInset - 3 + 50 + (i * 80) - factionLogos[i].getWidth()
                    / 2;
            factionLogos[i].position.Y = position.Y + 150 - factionLogos[i].getHeight() / 2 + Global.topInset - 29 + 20;
        }
        soldier.setPosition(new Vector2(150 + position.X + Global.leftInset - 3, 275 + position.Y + Global.topInset
                - 29 + 30));

        teamLogo.position.X = position.X + 168 + Global.leftInset - 3;
        teamLogo.position.Y = position.Y + 350 + Global.topInset - 29 + 20;

        teamArrow.position.X = position.X + 40 + (team.num * 80) + Global.leftInset - 3;
        teamArrow.position.Y = position.Y + 205 + Global.topInset - 29 + 20;

        weapon0.position.X = position.X + 560 + Global.leftInset - 3;
        weapon0.position.Y = position.Y + 150 + Global.topInset - 29;

        weapon1.position.X = position.X + 560 + Global.leftInset - 3;
        weapon1.position.Y = position.Y + 300 + Global.topInset - 29;

        grenade.position.X = position.X + 560 + Global.leftInset - 3;
        grenade.position.Y = position.Y + 450 + Global.topInset - 29;

        for (int i = 0; i < stats[0].length; i++) {
            stats[0][i].position.X = position.X + 690 + Global.leftInset - 3;
            stats[0][i].position.Y = position.Y + 125 + (i * 20) + Global.topInset - 29;
            stats[1][i].position.X = position.X + 690 + Global.leftInset - 3;
            stats[1][i].position.Y = position.Y + 275 + (i * 20) + Global.topInset - 29;
        }
        bg.position.X = position.X + Global.leftInset - 3 + 3;
        bg.position.Y = position.Y + Global.topInset - 29 + 29;

    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
        String[] keys = buttons.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            buttons.get(keys[i]).setVelocity(velocity);
        }
        for (int i = 0; i < factionLogos.length; i++) {
            factionLogos[i].velocity = velocity;
        }
        soldier.velocity = velocity;
        teamLogo.velocity = velocity;

        teamArrow.velocity = velocity;
        weapon0.velocity = velocity;
        weapon1.velocity = velocity;
        grenade.velocity = velocity;
        for (int i = 0; i < stats[0].length; i++) {
            stats[0][i].velocity = velocity;
            stats[1][i].velocity = velocity;
        }
        bg.velocity = velocity;
    }

    public PlayerOptions getPlayerOptions() {
        Weapon.Type[] weapons = { Weapon.Type.values()[wp0], Weapon.Type.values()[wp1] };
        return new PlayerOptions(team, weapons, Grenade.Type.values()[g], 3);
    }
    
    public GameOptions getGameOptions(PlayerOptions playerOptions){

    	processTeams();
    	Players p = processPlayers(playerOptions);
    	return new GameOptions(mode, teams, p, mapNames[mapNum], killsToWin);
    }
    
    private void processTeams(){
    	int numTeams = 1;
    	for(int i = 0; i < teamNums.length; i++)
    		if(numTeams < teamNums[i])
    			numTeams = teamNums[i];
    	teams = new Faction[numTeams][Faction.values().length];
    	
    	
    	for(int i = 0; i < teamNums.length; i++){
    		if(teamNums[i] - 1 >= 0){
    			for(int j = 0; j < teams[teamNums[i] - 1].length; j++){
    				if(teams[teamNums[i] - 1][j] == null){
    					teams[teamNums[i] - 1][j] = Faction.values()[i];
    					break;
    				}
    			}
    		}
    	}
    	//printArray(teams);
    	for(int i = 0; i < teams.length; i++){
    		if(teams[i] == null || teams[i].length == 0 || teams[i][0] == null){
    			for(int j = i + 1; j < teams.length; j++){
    				if(i > teams.length - 1) break;
    				if(teams[j] != null && teams[j].length != 0){
    					for(int k = 0; k < teams[j].length; k++){
    						teams[i][k] = teams[j][k];
    						teams[j][k] = null;
    					}
    				}
    			}
    		}
    	}
    	int firstEmpty = -1;
    	for(int i = 0; i < teams.length; i++)
    		if(teams[i] == null || teams[i].length == 0 || teams[i][0] == null){
    			firstEmpty = i;
    			break;
    		}
    	if(firstEmpty != -1){
    		Faction[][] temp = new Faction[firstEmpty][Faction.values().length];
    		for(int i = 0; i < temp.length; i++){
    			for(int j = 0; j < teams[i].length; j++){
    				temp[i][j] = teams[i][j];
    			}
    		}
    		teams = temp;
    	}
    }
    
    private Players processPlayers(PlayerOptions playerOptions){
    	Players players = new Players();

    	Player player = new Human("Local Player", playerOptions.faction,
				playerOptions.weapons, playerOptions.grenade,
				playerOptions.numGrenades);
    	Vector2 p = spawns[mapNum][(new Random()).nextInt(spawns[mapNum].length)];
		player.soldier.setPosition(p);
		player.soldier.torso.player = true;
		players.add(player);
    	
		Weapon.Type[] wpns = new Weapon.Type[2];
		for(int i = 0; i < numPlayers.length; i++){
			if(playing(i, playerOptions)){
				for(int j = 0; j < numPlayers[i]; j++){
					Random r = new Random();
					int k = r.nextInt(Weapon.pCutoff + 1);
					wpns[0] = Weapon.Type.values()[k];
					k = r.nextInt(Weapon.pCutoff + 1) + 4;
					wpns[1] = Weapon.Type.values()[k];
					k = r.nextInt(Grenade.Type.values().length);
					NPC s = new NPC(Soldier.Faction.values()[i] + " CPU " + j, Soldier.Faction.values()[i], wpns,
						Grenade.Type.values()[k], 3);
					Vector2 pos = spawns[mapNum][r.nextInt(spawns.length)].copy();
					s.soldier.setPosition(pos);
					players.add(s);
				}
			}
		}
		
		return players;
    }
    private boolean playing(int p, PlayerOptions po){
    	if(po.faction.num == p)
    		return true;
    	for(int i = 0; i < teams.length; i++){
    		for(int j = 0; j < teams[i].length; j++){
    			if(teams[i][j] != null && teams[i][j].num == p){
    				return true;
    			}
    		}
    	}
    	return false;
    }
}
