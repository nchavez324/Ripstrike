package project.model.maps;

import java.awt.Graphics2D;
import java.awt.Image;

import project.model.gfx.core.Component;
import project.model.util.Global;
import project.model.util.Vector2;

@SuppressWarnings("unused")
public class Map implements Component{
    public Image[] layers;
    public int compLayer;
    public CollisionMap collisionMap;
    public Vector2 position;
    public int w, h;
    
    public Map(Image[] layers, CollisionMap collisionMap, int compLayer){
       this.layers = layers;
       this.collisionMap = collisionMap;
       this.compLayer = compLayer;
       position = new Vector2();
       w = collisionMap.width;
       h = collisionMap.height;
       collisionMap.position.X = position.X - CollisionMap.SIZE;
       collisionMap.position.Y = position.Y;
    }

	@Override
	public void draw(Graphics2D g) {
	    collisionMap.draw(g);
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
