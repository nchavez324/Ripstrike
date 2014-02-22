package project.model.gfx;

import java.awt.Graphics2D;

import java.awt.Image;
import project.model.gfx.core.Component;
import project.model.gfx.core.Sprite;
import project.model.util.Global;

public class Background extends Sprite implements Component {

    String name;
    
    public Background(Image img){
        super();
        singleImg = img;
        loadResources();
    }
    
    @Override
    public void draw(Graphics2D g) {
       g.drawImage(singleImg, 
               Global.leftInset, Global.topInset, 
               Global.screenWidth + Global.leftInset, Global.screenHeight + Global.topInset,
               0, 0,
               getWidth(), getHeight(),
               null);
    }

    @Override
    public void update(long timePassed) {
        //
    }

    @Override
    public void loadResources() {
        //
    }

}
