package project.model.gfx.core;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import project.model.gfx.Soldier.BodyParts;
import project.model.gfx.core.Component;
import project.model.util.Global;
import project.model.util.Vector2;

public class Bone implements Component {

    public Image sheet;

    public Root root;

    public double theta;
    public double omega = 0;
    public double thetaTerm;
    public boolean anim = false;
    public Vector2 pivot;

    public Vector2 anchor;

    public int nodeNum;

    public int flipOff;

    public Bone(Image sheet, Root root, Vector2 pivot, Vector2 anchor, int flipOff) {
        this.sheet = sheet;
        theta = 0;
        this.pivot = pivot;
        this.root = root;
        this.anchor = anchor;
        this.flipOff = flipOff;
    }

    public void simpleAnim(double theta1, double theta2, double omega){
        theta = theta1;
        this.omega = omega;
        thetaTerm = theta2;
        anim = true;
    }
    
    @Override
    public void draw(Graphics2D g) {
        AffineTransform trans = new AffineTransform();
        if (root.player) {
            trans.translate(root.screenPos.X - pivot.X + root.anchors[nodeNum].X, root.screenPos.Y - pivot.Y
                    + root.anchors[nodeNum].Y);
        } else {
            trans.translate(root.position.X - pivot.X + root.anchors[nodeNum].X - Global.camera.x, root.position.Y
                    - pivot.Y + root.anchors[nodeNum].Y - Global.camera.y);
        }
        if (theta != 0) {
            if ((nodeNum == BodyParts.OverArm.num || nodeNum == BodyParts.UnderArm.num || nodeNum == BodyParts.Head.num)
                    && theta > Math.PI / 2) {
                trans.rotate(-theta + (3 / 2) * Math.PI, pivot.X, pivot.Y);
            } else {
                trans.rotate(theta, pivot.X, pivot.Y);
            }
        }
        g.drawImage(sheet, trans, null);
    }

    @Override
    public void update(long timePassed) {
        if(anim = true){
            theta += omega * timePassed;
            if(omega > 0){
                if(theta >= thetaTerm){
                    omega = 0;
                }
            }else{
                if(theta <= thetaTerm){
                    omega = 0;
                }
            }
        }
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
