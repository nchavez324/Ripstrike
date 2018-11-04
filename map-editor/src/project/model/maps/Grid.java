package project.model.maps;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import project.model.gfx.core.Component;
import project.model.util.Global;
import project.model.util.Vector2;

public class Grid implements Component {

    public Vector2 offset;

    public Vector2 realOff;

    public Grid(Vector2 offset) {
        this.offset = offset;
        realOff = new Vector2(offset.X, offset.Y);
    }

    @Override
    public void draw(Graphics2D g) {
        int w = Global.screenWidth / CollisionMap.SIZE + 2;
        int h = Global.screenHeight / CollisionMap.SIZE + 2;
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(1));
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                g.drawRect(x * CollisionMap.SIZE + (int) realOff.X, y * CollisionMap.SIZE + (int) realOff.Y,
                        CollisionMap.SIZE, CollisionMap.SIZE);

            }
        }
    }

    public void shiftX(int off) {
        offset.X -= off;
        realOff.X -= off;
        if (realOff.X >= CollisionMap.SIZE)
            realOff.X -= CollisionMap.SIZE;
        if (realOff.X < 0) {
            realOff.X += CollisionMap.SIZE;
        }
    }

    public void shiftY(int off) {
        offset.Y -= off;
        realOff.Y -= off;
        if (realOff.Y >= CollisionMap.SIZE)
            realOff.Y -= CollisionMap.SIZE;
        if (realOff.Y < 0) {
            realOff.Y += CollisionMap.SIZE;
        }
    }

    @Override
    public void update(long timePassed) {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadResources() {
        // TODO Auto-generated method stub

    }

}
