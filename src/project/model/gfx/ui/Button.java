package project.model.gfx.ui;

import project.model.gfx.core.Component;
import project.model.util.Vector2;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;

public class Button implements Component {

    public static final int UNPRESSED = 0;

    public static final int DEPRESSED = 1;

    public static final int HOVERSELECTED = 2;

    public Vector2 position;

    public Vector2 velocity;

    public boolean hud, flip, flipV = false;

    public boolean enabled;

    public int size;

    public int state;

    public Image sheet;

    public String msg;

    public Button(Image sheet, String msg, int size, boolean hud) {
        this.hud = hud;
        this.position = new Vector2();
        this.msg = msg;
        this.size = size;
        velocity = new Vector2();
        loadResources(sheet);
    }

    public Button(Image sheet, String name, boolean hud, boolean flip) {
        this.hud = hud;
        this.position = new Vector2();
        this.msg = name;
        this.flip = flip;
        velocity = new Vector2();
        loadResources(sheet);
    }
    public Button(Image sheet, String name, boolean hud, boolean flip, boolean flipV) {
        this.hud = hud;
        this.position = new Vector2();
        this.msg = name;
        this.flip = flip;
        this.flipV = flipV;
        velocity = new Vector2();
        loadResources(sheet);
    }

    public Button(Image sheet, String msg, int size, boolean hud, Vector2 position) {
        this.hud = hud;
        this.position = position;
        this.msg = msg;
        this.size = size;
        velocity = new Vector2();
        loadResources(sheet);
    }

    @Override
    public void draw(Graphics2D g) {
        int x1 = state * (sheet.getWidth(null) / 3);
        if (size != 0) {
            // left
            g.drawImage(sheet, (int) position.X, (int) position.Y, (int) position.X + 3,
                    (int) position.Y + sheet.getHeight(null), x1, 0, x1 + 3, sheet.getHeight(null), null);
            // middle
            g.drawImage(sheet, (int) position.X + 3, (int) position.Y, (int) position.X + 3 + size, (int) position.Y
                    + sheet.getHeight(null), x1 + 3, 0, x1 + 4, sheet.getHeight(null), null);
            // right
            g.drawImage(sheet, (int) position.X + 3 + size, (int) position.Y, (int) position.X + 3 + size + 3,
                    (int) position.Y + sheet.getHeight(null), x1 + 4, 0, x1 + 7, sheet.getHeight(null), null);
            drawMsg(g);
        } else {
            if (!flip){
            	if(!flipV){
            		g.drawImage(sheet, (int) position.X, (int) position.Y, (int) position.X + sheet.getWidth(null) / 3,
                            (int) position.Y + sheet.getHeight(null), x1, 0, x1 + sheet.getWidth(null) / 3,
                            sheet.getHeight(null), null);
            	}else{
            		g.drawImage(sheet, (int) position.X, (int) position.Y, (int) position.X + sheet.getWidth(null) / 3,
                            (int) position.Y + sheet.getHeight(null), x1, sheet.getHeight(null), x1 + sheet.getWidth(null) / 3,
                            0, null);
            	}
                
            }else{
            	if(!flipV){
            		g.drawImage(sheet, (int) position.X, (int) position.Y, (int) position.X + sheet.getWidth(null) / 3,
                            (int) position.Y + sheet.getHeight(null), x1 + sheet.getWidth(null) / 3, 0, x1,
                            sheet.getHeight(null), null);
            	}else{
            		g.drawImage(sheet, (int) position.X, (int) position.Y, (int) position.X + sheet.getWidth(null) / 3,
                            (int) position.Y + sheet.getHeight(null), x1 + sheet.getWidth(null) / 3, sheet.getHeight(null), x1,
                            0, null);
            	}
            }
        }
    }

    public void drawMsg(Graphics2D g) {

        g.setColor(new Color(255, 255, 255));
        FontMetrics fm = g.getFontMetrics(g.getFont());
        java.awt.geom.Rectangle2D rect = fm.getStringBounds(msg, g);
        int x = (int) (position.X + ((getWidth()) - rect.getWidth()) / 2);
        int y = (int) (position.Y + ((getHeight()) - rect.getHeight()) / 2) + 15;
        g.drawString(msg, x, y);
    }

    @Override
    public void update(long timePassed) {
        position.X += velocity.X * timePassed;
        position.Y += velocity.Y * timePassed;
    }

    @Override
	public void loadResources() {
        //
    }

    public void loadResources(Image sheet) {

        this.sheet = sheet;
        enabled = true;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public int getWidth() {
    	if(size != 0){
        return size + 6;}
    	else{
    		return sheet.getWidth(null)/3;
    	}
    }

    public int getHeight() {
        return sheet.getHeight(null);
    }

}
