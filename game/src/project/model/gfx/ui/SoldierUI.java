package project.model.gfx.ui;

import java.awt.Graphics2D;
import java.awt.Image;

import project.model.gfx.Soldier;
import project.model.gfx.core.Component;
import project.model.util.Global;
import project.model.util.Vector2;

public class SoldierUI implements Component {

    public static final int maxNumFills = 25;

    public Soldier soldier;

    public Vector2 offset;

    public Image bar;

    public Image fill;

    public SoldierUI(Soldier soldier) {
        this.soldier = soldier;
        this.offset = new Vector2(-4, -32);
        loadResources();
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(bar, (int) (soldier.torso.position.X + offset.X - Global.camera.x), (int) (soldier.torso.position.Y
                + offset.Y - Global.camera.y), null);
        int numFills = (int) ((soldier.health / 100) * maxNumFills);
        for (int i = 0; i < numFills; i++) {
            g.drawImage(fill, (int) (soldier.torso.position.X + offset.X - Global.camera.x) + (i * 1) + (2),
                    (int) (soldier.torso.position.Y + offset.Y - Global.camera.y + (1)), null);
        }
    }

    @Override
    public void update(long timePassed) {
        if(soldier.flippingUB){
        	this.offset = new Vector2(2, -32);
        }else{
        	this.offset = new Vector2(-4, -32);
        }
    }

    @Override
    public void loadResources() {
        bar = Global.depot.ui.get("otherBar");
        fill = Global.depot.ui.get("enemyFill");
    }

}
