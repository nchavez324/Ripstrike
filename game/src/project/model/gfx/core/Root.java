package project.model.gfx.core;

import java.awt.Graphics2D;
import java.awt.Image;
import project.model.gfx.Soldier;
import project.model.gfx.Soldier.BodyParts;
import project.model.util.Global;
import project.model.util.Vector2;

public class Root implements Component {

    public Image sheet;

    public Vector2[] anchors;

    public Bone[] nodes;

    public Vector2 position;

    public Vector2 screenPos;

    public boolean player = false;

    public int off = 6;

    public int yCut;

    public Soldier soldier;

    public Root(Image image, Soldier soldier, int yCut) {
        sheet = image;
        this.soldier = soldier;
        anchors = new Vector2[BodyParts.values().length];
        position = new Vector2();
        nodes = new Bone[BodyParts.values().length];
        screenPos = new Vector2((Global.camera.width - getWidth()) / 2, (Global.camera.height - getHeight()) / 2);
        this.yCut = yCut;
        loadResources();
    }

    @Override
    public void draw(Graphics2D g) {
        if (player) {
            g.drawImage(sheet, (int) screenPos.X, (int) screenPos.Y, (int) screenPos.X + sheet.getWidth(null),
                    (int) screenPos.Y + yCut, 0, 0, sheet.getWidth(null), yCut, null);
        } else {
            g.drawImage(sheet, (int) position.X - Global.camera.x, (int) position.Y - Global.camera.y, (int) position.X
                    + sheet.getWidth(null) - Global.camera.x, (int) position.Y + yCut - Global.camera.y, 0, 0,
                    sheet.getWidth(null), yCut, null);
        }
    }

    public void drawHip(Graphics2D g) {
        if (player) {
            g.drawImage(sheet, (int) screenPos.X, (int) screenPos.Y + yCut, (int) screenPos.X + sheet.getWidth(null),
                    (int) screenPos.Y + sheet.getHeight(null), 0, yCut, sheet.getWidth(null), sheet.getHeight(null),
                    null);
        } else {
            g.drawImage(sheet, (int) position.X - Global.camera.x, (int) position.Y + yCut - Global.camera.y,
                    (int) position.X + sheet.getWidth(null) - Global.camera.x, (int) position.Y + sheet.getHeight(null)
                            - Global.camera.y, 0, yCut, sheet.getWidth(null), sheet.getHeight(null), null);
        }
    }

    @Override
    public void update(long timePassed) {
        //
    }

    @Override
    public void loadResources() {
        //
    }

    public int getWidth() {
        return sheet.getWidth(null);
    }

    public int getHeight() {
        return sheet.getHeight(null);
    }
}
