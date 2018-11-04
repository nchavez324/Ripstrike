package project.model.maps;

import java.awt.Graphics2D;
import java.awt.Image;

import project.model.gfx.core.Component;
import project.model.util.Global;
import project.model.util.Vector2;

public class Map implements Component {
	public Image[] layers;

	public Image bg;

	public int compLayer;

	public CollisionMap collisionMap;

	public Vector2 position;

	public Vector2 offset;

	public int w, h;

	public Map(Image[] layers, Image bg, CollisionMap collisionMap,
			int compLayer) {
		this.layers = layers;
		this.bg = bg;
		this.collisionMap = collisionMap;
		this.compLayer = compLayer;
		position = new Vector2();
		w = collisionMap.width;
		h = collisionMap.height;
		collisionMap.position.X = position.X;
		collisionMap.position.Y = position.Y;
		offset = new Vector2(-1, 0);
	}

	public void drawUnder(Graphics2D g) {
    	g.drawImage(bg, Global.leftInset, Global.topInset, Global.screenWidth + Global.leftInset, Global.screenHeight + Global.topInset, 0, 0, bg.getWidth(null), bg.getHeight(null), null);
        collisionMap.position.X = position.X;
        collisionMap.position.Y = position.Y;
        for (int i = 0; i <= compLayer; i++) {
            g.drawImage(layers[i], (int) (position.X + offset.X - Global.camera.x),
                    (int) (position.Y + offset.Y - Global.camera.y), null);
        }
    }

	public void drawOver(Graphics2D g) {
		for (int i = compLayer + 1; i < layers.length; i++) {
			g.drawImage(layers[i],
					(int) (position.X + offset.X - Global.camera.x),
					(int) (position.Y + offset.Y - Global.camera.y), null);
		}
		collisionMap.position.X = position.X;
		collisionMap.position.Y = position.Y;
		collisionMap.draw(g);
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub

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
