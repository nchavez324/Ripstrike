package project.model.gfx.ui.menus;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import project.model.gfx.core.Component;
import project.model.util.Global;

public class Popup implements Component {

    public Image sheet;

    public String message;

    public Rectangle textBounds;

    public boolean visible = false;

    public static Image bg = Global.depot.ui.get("transBlk");

    public Popup() {

    }

    public Popup(Image sheet, String message) {
        this.sheet = sheet;
        this.message = message;
        textBounds = new Rectangle(10, 20, sheet.getWidth(null) - 10, sheet.getHeight(null) - 20);
    }

    @Override
    public void draw(Graphics2D g) {
        if (visible) {
            g.drawImage(bg, 0, 0, Global.screenWidth, Global.screenHeight, 0, 0, 1, 1, null);
            g.drawImage(sheet, Global.screenWidth / 2 - sheet.getWidth(null) / 2,
                    Global.screenHeight / 2 - sheet.getHeight(null) / 2, Global.screenWidth / 2 + sheet.getWidth(null)
                            / 2, Global.screenHeight / 2 + sheet.getHeight(null) / 2, 0, 0, sheet.getWidth(null),
                    sheet.getHeight(null), null);
            drawText(g);

        }
    }

    @Override
    public void update(long timePassed) {
        // TODO Auto-generated method stub

    }

    public void drawText(Graphics2D g) {
        g.setColor(Color.WHITE);
        String txtCpy = message;
        ArrayList<String> ans = new ArrayList<String>();
        String temp = "";
        String[] split = txtCpy.split(" ");
        for (int i = 0; i < split.length; i++) {
            if (!fits(temp + split[i], g) && !(split[i].compareTo("$") == 0)) {
                ans.add(temp);
                temp = "";
            } else if (split[i].compareTo("$") == 0) {
                ans.add(temp);
                temp = "";
                i++;
            }
            temp += split[i] + " ";
        }
        ans.add(temp);
        FontMetrics fm = g.getFontMetrics(g.getFont());
        java.awt.geom.Rectangle2D rect = fm.getStringBounds(ans.get(0), g);
        int linespace = (int) rect.getHeight();
        int i = 0;
        for (String line : ans) {
            g.drawString(line, Global.screenWidth / 2 - sheet.getWidth(null) / 2 + textBounds.x, Global.screenHeight
                    / 2 - sheet.getHeight(null) / 2 + textBounds.y + (linespace * i));
            i++;
        }
        java.awt.geom.Rectangle2D rect2 = fm.getStringBounds("Press Enter", g);
        g.drawString("Press Enter", Global.screenWidth / 2 - (int) rect2.getWidth() / 2, Global.screenHeight / 2
                + sheet.getHeight(null) / 2 + 20);
    }

    public boolean fits(String line, Graphics2D g) {
        FontMetrics fm = g.getFontMetrics(g.getFont());
        java.awt.geom.Rectangle2D rect = fm.getStringBounds(line, g);
        return (rect.getWidth() < textBounds.width);
    }

    @Override
    public void loadResources() {
        // TODO Auto-generated method stub

    }

}
