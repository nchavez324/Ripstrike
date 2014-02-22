package project.model.gfx.ui;

import java.awt.Graphics2D;
import project.model.gfx.core.Component;
import project.model.util.Vector2;

public abstract class Cursor implements Component {

	public Vector2 position;
	public boolean down;
	
	@Override
	public abstract void draw(Graphics2D g);
	@Override
	public abstract void update(long timePassed);

	@Override
	public abstract void loadResources();

}
