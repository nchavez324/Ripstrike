package project.model.gfx.core;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;

import project.model.util.Directions;
import project.model.util.Global;
import project.model.util.Vector2;

public class Sprite implements Component {

    public Animation animation; // The current (or only) animation the sprite has.

    public HashMap<String, Animation> animations; // In a more complex game, a sprite could have more than one
                                                  // Animation.

    public Vector2 position; // A vector for the coordinates in the window.

    public Vector2 velocity; // Velocity, not speed.

    public Vector2 acceleration; // Sounds groovy, but hard to use. Imo.

    public String currentAnimation; // This would hold a key to that HashMap up there.

    public Image singleImg; // If the sprite does not have an animation, it is just one picture. This holds that.

    public Vector2 accelTime; // Time since acceleration started.

    public boolean hud;

    public boolean visible = true;

    // Kind of obvious.
    public Sprite() {
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        accelTime = new Vector2(0, 0);
        hud = false;
    }

    // Still pretty ovious.
    public Sprite(Animation animation) {
        this.animation = animation;
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        accelTime = new Vector2(0, 0);
        hud = false;

    }

    // Even more so.
    public Sprite(Image singleImg) {
        this.singleImg = singleImg;
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        accelTime = new Vector2(0, 0);
        hud = false;
    }

    // Takes a set of animations.
    public Sprite(HashMap<String, Animation> animations) {
        this.animations = animations;
        animation = animations.get(Directions.SOUTH);

        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        accelTime = new Vector2(0, 0);
        hud = false;
    }

    // Updates. No shit. Tell me more.
    @Override
    public void update(long timePassed) {
        // Real acceleration. This would work if something never stopped accelerating.
        // I just put it in here so I wouldn't have to google it later.
        // velocity.X += acceleration.X * accelTime.X;
        // velocity.Y += acceleration.Y * accelTime.Y;
        position.X += velocity.X * timePassed + (acceleration.X * accelTime.X * accelTime.X); // Updates position based
        position.Y += velocity.Y * timePassed + (acceleration.Y * accelTime.Y * accelTime.Y);// on velocity,
                                                                                             // acceleration,
        // And time.
        if (animation != null)// Checks to see if this Sprite has an animation to potentially update it.
        {
            animation.update(timePassed);
        }
    }

    public int getHeight() {// Gets height, which never really changes.
        if (animation != null) {
            if (animation.rows == -1) {
                return animation.sheet.getHeight(null); // Returns the height of the spritesheet in animation.
            } else {
                return animation.frameHeight;
            }
        }
        return singleImg.getHeight(null);// Returns the height of the lonely image.
    }

    public int getWidth() {// Gets width, which changes in some cases, like when there are multiple animations.
        if (animation != null) {
            return animation.frameWidth; // Returns the width of the spritesheet in animation.
        }
        return singleImg.getWidth(null); // Returns the width of the lonely image.
    }

    // Draws itself onto screen.
    @Override
    public void draw(Graphics2D g) {
        if (visible) {
            if (animation != null) {
                // Draws according to Animation's rectangle.
                if (hud) {
                    g.drawImage(animation.sheet, (int) position.X, (int) position.Y, (int) position.X
                            + animation.frameWidth, (int) position.Y + getHeight(), animation.getRectangle().x,
                            animation.getRectangle().y, animation.getRectangle().x + animation.frameWidth,
                            animation.getRectangle().y + getHeight(), null); // Basically it takes an image, a
                                                                             // destination rectangle, a source
                                                                             // rectangle and an image observer.
                } else {
                    
                    g.drawImage(animation.sheet, (int) position.X - Global.camera.x,
                            (int) position.Y - Global.camera.y, (int) position.X + animation.frameWidth
                                    - Global.camera.x, (int) position.Y + getHeight() - Global.camera.y,
                            animation.getRectangle().x, animation.getRectangle().y, animation.getRectangle().x
                                    + animation.frameWidth, animation.getRectangle().y + getHeight(), null);
                }
            } else {
                if (hud) {
                    g.drawImage(singleImg, (int) position.X, (int) position.Y, null); // Draws straight from the lonely
                                                                                      // image.
                } else {
                    g.drawImage(singleImg, (int) position.X - Global.camera.x, (int) position.Y - Global.camera.y, null); // Draws
                                                                                                                          // straight
                                                                                                                          // from
                                                                                                                          // the
                                                                                                                          // lonely
                                                                                                                          // image.
                }
            }
        }
    }

    // Returns the animation.
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public void loadResources() {
        //
    }

}
