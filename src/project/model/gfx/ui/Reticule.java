package project.model.gfx.ui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import project.model.gfx.Soldier;
import project.model.util.Global;
import project.model.util.Player;
import project.model.util.Vector2;

public class Reticule extends Cursor {

    public long blinkTime = 400;

    public long timeCounter;

    public boolean in = false;

    public float distCornersMult = .3f;

    public Player player;

    public Image blue, red;

    public float theta, omega;

    public Reticule() {
        down = false;
        position = new Vector2();
        loadResources();
    }

    public Reticule(Player player) {
        this();
        this.player = player;
        position = new Vector2(640, 560);
    }

    @Override
	public void draw(Graphics2D g) {
        int d = 26;
        if (player != null) {
            Vector2 sight = new Vector2(
                    player.soldier.bones[Soldier.BodyParts.Head.num].pivot.X + player.soldier.torso.screenPos.X,
                    player.soldier.bones[Soldier.BodyParts.Head.num].pivot.Y + player.soldier.torso.screenPos.Y);
            d = (int) ((Global.getDist(sight, position) * distCornersMult) - 100*(Math.pow(player.soldier.currentWeapon.accuracy + .04, 7)));
        }
        if (!down) {
            if (!in && d >= 8)
                d -= 12;
            if(d <= 3)d = 3;
            drawRet(g, blue, d);
        } else {
            d -= 20;
            if (d < 3)
                d = 3;
            drawRet(g, red, d);
        }

    }

    private void drawRet(Graphics2D g, Image corner, int d) {
        AffineTransform t = new AffineTransform();
        t.translate((int) position.X - (corner.getWidth(null) + d / 2), (int) position.Y
                - (corner.getHeight(null) + d / 2));
        t.rotate(theta, corner.getWidth(null) + d / 2, corner.getHeight(null) + d / 2);
        g.drawImage(corner, t, null);

        t = AffineTransform.getScaleInstance(-1, 1);
        t.translate(-(position.X + d / 2 + corner.getWidth(null)), position.Y - (corner.getHeight(null) + d / 2));
        t.rotate(-theta, corner.getWidth(null) + d / 2, corner.getHeight(null) + d / 2);
        g.drawImage(corner, t, null);

        t = AffineTransform.getScaleInstance(1, -1);
        t.translate((int) position.X - (corner.getWidth(null) + d / 2), -(position.Y + d / 2 + corner.getHeight(null)));
        t.rotate(-theta, corner.getWidth(null) + d / 2, corner.getHeight(null) + d / 2);
        g.drawImage(corner, t, null);

        t = AffineTransform.getScaleInstance(-1, -1);
        t.translate(-(position.X + d / 2 + corner.getWidth(null)), -(position.Y + d / 2 + corner.getHeight(null)));
        t.rotate(theta, corner.getWidth(null) + d / 2, corner.getHeight(null) + d / 2);
        g.drawImage(corner, t, null);
    }

    @Override
    public void update(long timePassed) {
        if (!down) {
            timeCounter += timePassed;
            if (timeCounter >= blinkTime) {
                in = !in;
                timeCounter = 0;
            }
        }
        theta += (omega) * timePassed;
    }

    @Override
    public void loadResources() {
        blue = Global.depot.ui.get("blueRet");
        red = Global.depot.ui.get("redRet");
        omega = .002f;
    }

}
