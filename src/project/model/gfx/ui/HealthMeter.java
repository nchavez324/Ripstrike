/*
 * Copyright (c) 2011 BleepBloop Software, All Rights Reserved
 *
 * Unpublished copyright.  All rights reserved.
 */
package project.model.gfx.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import project.model.util.Global;
import project.model.util.Player;

/**
 * TODO: type comment.
 *
 * @version Aug 17, 2011, submitted by Nick Chavez
 */
public class HealthMeter {

    public Image fill;
    public Image base;
    public Image mask;
    public Player player;
    
    public HealthMeter(Player player){
        base = Global.depot.ui.get("healthbase");
        fill = Global.depot.ui.get("healthfill");
        mask = Global.depot.ui.get("healthmask");
        this.player = player;
    }
    
    public void draw(Graphics2D g){
        g.drawImage(base,
        		Global.leftInset, Global.screenHeight - (base.getHeight(null)),
        		Global.leftInset + (base.getWidth(null)), Global.screenHeight,
                0, 0,
                base.getWidth(null), base.getHeight(null),
                null);
        
      
        if(player.soldier.health > 0){
        g.drawImage(fill,
                (int)((60 - (((100 - player.soldier.health)/100)) * ((fill.getWidth(null)) - 10))) - 1, Global.screenHeight - (base.getHeight(null)) + 17,
                (int)((Global.leftInset + (fill.getWidth(null)) + 60 - (((100 - player.soldier.health)/100)) * ((fill.getWidth(null)) - 10))) - 1, Global.screenHeight - (base.getHeight(null)) + 17 + (fill.getHeight(null)),
                0, 0,
                fill.getWidth(null), fill.getHeight(null),
                null);
        }
        g.drawImage(mask,
        		Global.leftInset, Global.screenHeight - (mask.getHeight(null)),
        		Global.leftInset + (mask.getWidth(null)), Global.screenHeight,
                0, 0,
                mask.getWidth(null), mask.getHeight(null),
                null);
        
        
        if(player.soldier.health > 0 && player.soldier.health <= 100){
            g.setColor(Color.WHITE);
            g.drawString((int)player.soldier.health + "", Global.leftInset + 17, Global.screenHeight - (mask.getHeight(null)) + 30);
        }
    }
    
}

