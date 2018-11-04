package project.model.gfx.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import project.model.util.Global;
import project.model.util.Vector2;

public class Reticule extends Cursor {

    public boolean in = false;

    public int d;

    public Image blue, red;

    public float theta, omega;

    public Reticule() {
        down = false;
        position = new Vector2();
        loadResources();
    }

    @Override
    public void draw(Graphics2D g) {
        if (!down) {
            d = 5;
            drawRet(g, blue, d);
        } else {
            d = -5;
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

        if (!down)
            g.setColor(new Color(45, 137, 207));
        else
            g.setColor(new Color(252, 61, 64));
        g.drawOval((int) (position.X) - 5, (int) (position.Y) - 5, 10, 10);
    }

    @Override
    public void update(long timePassed) {
        theta += (omega) * timePassed;
    }

    @Override
    public void loadResources() {
        blue = Global.depot.ui.get("blueRet");
        red = Global.depot.ui.get("redRet");
        omega = .002f;
    }

}
