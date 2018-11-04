package project.model.gfx.ui.menus;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;

import project.model.gfx.ui.Button;
import project.model.util.Global;
import project.model.util.Vector2;

public class PauseMenu extends Popup {

    public HashMap<String, Button> buttons;

    public static Image frame = Global.depot.ui.get("popup");
    public static Image logo = Global.depot.ui.get("smallLogo");

    public PauseMenu() {
        super();
        int x = (Global.screenWidth - 200) / 2;
        buttons = new HashMap<String, Button>();
        buttons.put("Resume", new Button(Global.depot.ui.get("button"), "Resume", 200, true, new Vector2(x, 175)));
        buttons.put("Options", new Button(Global.depot.ui.get("button"), "Options", 200, true, new Vector2(x, 230)));
        buttons.put("Restart", new Button(Global.depot.ui.get("button"), "Restart", 200, true, new Vector2(x, 285)));
        buttons.put("Exit", new Button(Global.depot.ui.get("button"), "Exit", 200, true, new Vector2(x, 340)));
    }

    @Override
	public void draw(Graphics2D g) {
        if (visible) {
            g.drawImage(bg, 0, 0, Global.screenWidth, Global.screenHeight, 0, 0, 1, 1, null);
            g.drawImage(frame, (Global.screenWidth - frame.getWidth(null)) / 2, 205, null);
            g.drawImage(logo, (Global.screenWidth - logo.getWidth(null))/2, 100, null);
            String[] keys = buttons.keySet().toArray(new String[0]);
            for (int i = 0; i < keys.length; i++) {
                buttons.get(keys[i]).draw(g);
            }
            g.setColor(Color.WHITE);
            FontMetrics fm = g.getFontMetrics(g.getFont());
            java.awt.geom.Rectangle2D rect = fm.getStringBounds("Nicolas Chavez, Copyright 2012", g);
            g.drawString("Nicolas Chavez, Copyright 2012", (int)(Global.screenWidth - rect.getWidth())/2 + 5, 425);
        }
    }

    @Override
	public void update(long timePassed) {
        super.update(timePassed);
        String[] keys = buttons.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            buttons.get(keys[i]).update(timePassed);
        }
    }

    public void setVisible(boolean state) {
        visible = state;
        String[] keys = buttons.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            buttons.get(keys[i]).enabled = state;
        }
    }
}
