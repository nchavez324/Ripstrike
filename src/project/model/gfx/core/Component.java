package project.model.gfx.core;

import java.awt.Graphics2D;

public interface Component {
  
    
    
    public void draw(Graphics2D g);
    public void update(long timePassed);
    public void loadResources();
}
