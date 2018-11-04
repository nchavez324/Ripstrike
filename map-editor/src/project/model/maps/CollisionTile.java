package project.model.maps;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import project.model.util.Global;
import project.model.util.Vector2;

public class CollisionTile implements java.io.Serializable{

    private static final long serialVersionUID = -8164813612942160954L;

    public enum Type {

        SLOPE(-1), EMPTY(0), BLOCK(1);
        public int num;

        Type(int num) {
            this.num = num;
        }
    }

    public float m, b;

    public Type type;

    public CollisionTile(float m, float b) {
        this.m = m;
        this.b = b;
        type = Type.SLOPE;
    }

    public CollisionTile(Type type) {
        if (type == Type.BLOCK || type == Type.EMPTY) {
            this.type = type;
        } else {
            try {
                throw new Exception();
            } catch (Exception e) {
                System.err.println("Tried to pass invalid tile type");
            }
        }
    }

    // DEBUG ONLY
    public void draw(Graphics2D g, Vector2 mapPosition, int x, int y) {
        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(3));
        if (type == Type.SLOPE) {
            Vector2 p1 = new Vector2(mapPosition.X + CollisionMap.SIZE * x - Global.camera.x, mapPosition.Y
                    + CollisionMap.SIZE * y - Global.camera.y);
            Vector2 p2 = new Vector2(mapPosition.X + CollisionMap.SIZE * x - Global.camera.x, mapPosition.Y
                    + CollisionMap.SIZE * y - Global.camera.y);
            calcPts(p1, p2);
            g.drawLine((int) (p1.X), (int) (p1.Y), (int) (p2.X), (int) (p2.Y));
        } else if (type == Type.BLOCK) {
            g.drawRect((int) (mapPosition.X + CollisionMap.SIZE * x - Global.camera.x), (int) (mapPosition.Y
                    + CollisionMap.SIZE * y - Global.camera.y), CollisionMap.SIZE, CollisionMap.SIZE);
        }
    }

    public void calcPts(Vector2 p1, Vector2 p2) {
        float x1, y1, x2, y2;
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
        if (b < 0) {
            x1 = -b / m;
            y1 = 0;
            if (b + m <= 1) {
                x2 = 1;
                y2 = m + b;
            } else {
                x2 = (1 - b) / m;
                y2 = 1;
            }
        } else if (b >= 0 && b <= 1) {
            x1 = 0;
            y1 = b;
            if (b + m < 0) {
                x2 = -b / m;
                y2 = 0;
            } else if (b + m >= 0 && b + m <= 1) {
                x2 = 1;
                y2 = m + b;
            } else {
                x2 = (1 - b) / m;
                y2 = 1;
            }
        } else if (b > 1) {
            x1 = (1 - b) / m;
            y1 = 1;
            if (b + m >= 0) {
                x2 = 1;
                y2 = m + b;
            } else {
                x2 = -b / m;
                y2 = 0;
            }
        }
        x1 *= CollisionMap.SIZE;
        y1 *= CollisionMap.SIZE;
        x2 *= CollisionMap.SIZE;
        y2 *= CollisionMap.SIZE;
        p1.X += x1;
        p1.Y += y1;
        p2.X += x2;
        p2.Y += y2;
    }
}
