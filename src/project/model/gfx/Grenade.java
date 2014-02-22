package project.model.gfx;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import project.model.gfx.core.Component;
import project.model.util.Global;
import project.model.util.Vector2;

public class Grenade implements Component {

    public enum Type {

        Frag(0), Freeze(1), Flash(2);
        public int num;

        Type(int num) {
            this.num = num;
        }
    }

    public Grenade.Type type;

    public Image sheet;

    public Vector2 position, velocity, acceleration;

    public double theta;

    public static double omega = .01;

    public long timeToBlow = -1;

    public long fuseTime = 0;

    public boolean dead = false;

    public Soldier soldier;
    
    public Grenade(Grenade.Type type, Soldier soldier, Vector2 velocity, Vector2 position) {
        this.type = type;
        this.soldier = soldier;
        sheet = Global.depot.grenades.get(type.toString());
        this.position = position;
        this.velocity = velocity;
        acceleration = new Vector2(0, Global.GACCEL);
    }

    public void explode() {
        velocity.Y = 0;
        velocity.X = 0;
        acceleration.Y = 0;
        omega = 0;
        timeToBlow = 0;
    }
    @Override
    public void draw(Graphics2D g) {
        AffineTransform t = new AffineTransform();
        t.translate(position.X - Global.camera.x, position.Y - Global.camera.y);
        t.rotate(theta, sheet.getWidth(null) / 2, sheet.getHeight(null) / 2);
        g.drawImage(sheet, t, null);
    }

    @Override
    public void update(long timePassed) {
        velocity.X += acceleration.X * timePassed;
        velocity.Y += acceleration.Y * timePassed;
        position.X += velocity.X * timePassed;
        position.Y += velocity.Y * timePassed;
        theta += omega * timePassed;
        if (timeToBlow != -1) {
            timeToBlow += timePassed;
            if (timeToBlow >= fuseTime) {
                dead = true;
            }
        }
    }

    @Override
    public void loadResources() {
    }

    public int getWidth() {
        return sheet.getWidth(null);
    }

    public int getHeight() {
        return sheet.getHeight(null);
    }

}
