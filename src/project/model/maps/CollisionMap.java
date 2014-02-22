package project.model.maps;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import project.model.gfx.core.Component;
import project.model.maps.CollisionTile.Type;
import project.model.util.Vector2;

public class CollisionMap implements java.io.Serializable, Component {

    private static final long serialVersionUID = 5185642390754378602L;

    public static int SIZE = 32;

    public static boolean DEBUG = false;

    public Vector2 position;

    public int numLayers = 1;

    public int compLayer = 0;

    public int width, height;

    public CollisionTile[][] tiles;

    public CollisionMap(CollisionTile[][] tiles) {
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.tiles = tiles;
        position = new Vector2();
    }

    public CollisionTile getTile(Vector2 reg) {
        int x = (int) (reg.X - position.X) / (SIZE);
        int y = (int) (reg.Y - position.Y) / (SIZE);
        if (x >= 0 && y >= 0 && x < tiles.length && y < tiles[0].length) {
            return tiles[x][y];
        } else {
            return null;
        }
    }

    public Rectangle getRectangle(int x, int y) {
        return new Rectangle((int) position.X + x * SIZE, (int) position.Y + y * SIZE, SIZE, SIZE);
    }

    public CollisionTile getTile(int x, int y) {
        return tiles[x][y];
    }

    // DEBUG ONLY
    @Override
	public void draw(Graphics2D g) {
        if (DEBUG) {
            for (int x = 0; x < tiles.length; x++) {
                for (int y = 0; y < tiles[x].length; y++) {
                    tiles[x][y].draw(g, position, x, y);
                }
            }
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

    public boolean collides(Line2D.Float path){
    	
    	return false;
    }
    
    public boolean collides(Vector2 point) {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                CollisionTile t = tiles[x][y];
                if (t.type != Type.EMPTY) {
                    Rectangle r = getRectangle(x, y);
                    if (point.X >= r.x
                            && point.X <= r.x + r.width
                            && point.Y >= r.y
                            && point.Y <= r.y + r.height
                            && (t.type == Type.BLOCK || (t.type == Type.SLOPE && (((point.X - r.x) * t.m + t.b
                                    * CollisionMap.SIZE)
                                    + ((height - y - 1) * CollisionMap.SIZE) <= point.Y)))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean collides(Vector2 point, Vector2 velocity) {
        long timeFrame = 75;
        Line2D.Float trajectory = new Line2D.Float(point.X, point.Y, point.X + velocity.X * timeFrame, point.Y
                + velocity.Y * timeFrame);
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                CollisionTile t = tiles[x][y];
                if (t.type != Type.EMPTY) {
                    Rectangle r = getRectangle(x, y);
                    if (t.type == Type.BLOCK && trajectory.intersects(r)) {
                        return true;
                    }
                    if (t.type == Type.SLOPE) {
                        Vector2 p1 = new Vector2(position.X + CollisionMap.SIZE * x, position.Y + CollisionMap.SIZE * y);
                        Vector2 p2 = new Vector2(position.X + CollisionMap.SIZE * x, position.Y + CollisionMap.SIZE * y);
                        t.calcPts(p1, p2);
                        Line2D.Float slope = new Line2D.Float(p1.X, p1.Y, p2.X, p2.Y);
                        if(slope.intersectsLine(trajectory)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
