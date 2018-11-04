package project.model.maps;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import project.model.gfx.core.Component;
import project.model.util.Global;
import project.model.util.Vector2;

public class CollisionMap implements java.io.Serializable, Component {

    private static final long serialVersionUID = 5185642390754378602L;

    public static int SIZE = 32;

    public static boolean DEBUG = true;

    public Vector2 position;

    public int width, height;

    public int numLayers = 1;

    public int compLayer = 0;

    public CollisionTile[][] tiles;

    public CollisionMap(CollisionTile[][] tiles) {
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.tiles = tiles;
        position = new Vector2();
    }

    public CollisionTile getTile(Vector2 reg, Vector2 position) {
        int x = (int) (reg.X - position.X) / (SIZE);
        int y = (int) (reg.Y - position.Y) / (SIZE);
        if (x >= 0 && y >= 0 && x < tiles.length && y < tiles[0].length) {
            return tiles[x][y];
        } else {
            return null;
        }
    }

    public Rectangle getRectangle(Vector2 position, int x, int y) {
        return new Rectangle((int) position.X + x * SIZE, (int) position.Y + y * SIZE, SIZE, SIZE);
    }

    public CollisionTile getTile(int x, int y) {
        return tiles[x][y];
    }

    @Override
    public void draw(Graphics2D g) {
        if (DEBUG) {
            Composite orig = g.getComposite();
            g.setColor(Color.ORANGE);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
            g.fillRect((int) (position.X - Global.camera.x), (int) (position.Y - Global.camera.y), width * SIZE, height
                    * SIZE);
            g.setComposite(orig);
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

    public void resizeX(int dim) {
        int newLength = width + 1;
        int destPos = 0;
        if (dim == -1) {
            destPos = 1;
        } else if (dim == 1) {
            destPos = 0;
        }
        CollisionTile[][] newTiles = new CollisionTile[newLength][height];
        for (int x = 0; x < newLength; x++) {
            for (int y = 0; y < height; y++) {
                if (x >= destPos && x < width) {
                    newTiles[x][y] = tiles[x - destPos][y];
                } else {
                    newTiles[x][y] = new CollisionTile(CollisionTile.Type.EMPTY);
                }
            }
        }
        tiles = newTiles;
        width = newLength;
        if (dim == -1)
            position.X -= (SIZE);
    }

    public void resizeY(int dim) {
        int newLength = height + 1;
        int destPos = 0;
        if (dim == -1) {
            destPos = 1;
        } else if (dim == 1) {
            destPos = 0;
        }
        CollisionTile[][] newTiles = new CollisionTile[width][newLength];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < newLength; y++) {
                if (y >= destPos && y < height) {
                    newTiles[x][y] = tiles[x][y - destPos];
                } else {
                    newTiles[x][y] = new CollisionTile(CollisionTile.Type.EMPTY);
                }
            }
        }
        tiles = newTiles;
        height = newLength;
        if (dim == -1)
            position.Y -= (SIZE);
    }

    public void shrinkX(int dim) {
        int newLength = width - 1;
        int startPos = 0;
        if (dim == -1) {
            startPos = 0;
        } else if (dim == 1) {
            startPos = 1;
        }
        CollisionTile[][] newTiles = new CollisionTile[newLength][height];
        for (int x = 0; x < newLength; x++) {
            for (int y = 0; y < height; y++) {
                newTiles[x][y] = tiles[x + startPos][y];
            }
        }
        tiles = newTiles;
        width = newLength;
    }

    public void shrinkY(int dim) {
        int newLength = height - 1;
        int startPos = 0;
        if (dim == -1) {
            startPos = 0;
        } else if (dim == 1) {
            startPos = 1;
        }
        CollisionTile[][] newTiles = new CollisionTile[width][newLength];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < newLength; y++) {
                newTiles[x][y] = tiles[x][y + startPos];
            }
        }
        tiles = newTiles;
        height = newLength;
    }

}
