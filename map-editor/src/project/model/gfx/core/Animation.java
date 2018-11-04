package project.model.gfx.core;

import java.awt.Image;
import java.awt.Rectangle;

public class Animation {

    public int sceneIndex; // What frame the animation is on.

    private long movieTime; // How long the animation has been going on for,
                            // resetting at zero every new cycle.

    private long totalTime; // How much time the animation SHOULD go on for.
                            // Does not change.

    public int frameWidth; // The width, in pixels, of each frame/picture.

    public int frameHeight = -1;

    public long frameTime; // How long a single frame should last.

    public int numFrames; // The number of frames.

    public Image sheet; // The image file that has all the images for one
                        // animation. A spritesheet.

    public int rows;

    public int cols;
    public boolean once = false;
    public boolean stopped;

    // Takes in a spritesheet, a number of frames in the sheet, and the time the
    // frameTime.
    public Animation(Image sheet, int numFrames, long frameTime) {
        this.sheet = sheet;
        this.numFrames = numFrames;
        this.frameTime = frameTime;
        frameWidth = sheet.getWidth(null) / numFrames; // Calculates frame width
        rows = -1; // assuming all frames
        cols = -1; // are the same width.
        totalTime = numFrames * frameTime; // Calculates the total time.
        start(); // Begins animation.
    }

    public Animation(Image sheet, int rows, int cols, long frameTime) {
        this.sheet = sheet;
        numFrames = rows * cols;
        this.rows = rows;
        this.cols = cols;
        frameHeight = sheet.getHeight(null) / rows;
        this.frameTime = frameTime;
        frameWidth = sheet.getWidth(null) / cols; // Calculates frame width
        totalTime = numFrames * frameTime; // Calculates the total time.
        start(); // Begins animation.
    }

    // You cannot start the animation in two places.
    public synchronized void start() {
        movieTime = 0; // Sets realtime to zero.
        sceneIndex = 0; // Resets to the first frame in the sequence.
        stopped = false;
    }

    // Checks whether or not the next frame should be moved to. Also, it updates
    // the real time, or movieTime.
    public synchronized void update(long timePassed) {
        if (numFrames > 1 && !stopped) {
            movieTime += timePassed; // Updates movieTime.
            if ((movieTime >= totalTime) && !once) {// Checks to see if the sequence should                         // be restarted.
                start();
            }else if((movieTime >= totalTime) && once){
            	stopped = true;
            }
            while (movieTime > (sceneIndex + 1) * frameTime) { // Checks to move                                              // next frame.
                sceneIndex++;
            }
        }
    }

    // Returns the rectangle that represents the box that surrounds the image on
    // the sheet.
    // Its like what piece of the rectangle we want so that we can crop it later
    // on.
    // Dependent on what frame we're up to. For example, a rectangle would
    // specify this:
    /*
     * -------------++++++++------------- | + I + You're | | + want + next | | +
     * this + bub! | | + part + | | + of + | | + the + | | + sheet+ |
     * -------------++++++++-------------
     */
    public Rectangle getRectangle() {
        Rectangle ans;
        int x1, x2, y1, y2;
        if (rows != -1 && cols != -1) {
            x1 = (sceneIndex % cols);
            x2 = x1 + 1;
            y1 = (sceneIndex / rows);
            x1 *= frameWidth;
            x2 *= frameWidth;
            y1 *= frameHeight;
            y2 = frameHeight;
        } else {
            x1 = sceneIndex * frameWidth;
            y1 = 0;
            x2 = x1 + frameWidth;
            y2 = sheet.getHeight(null);
        }
        ans = new Rectangle(x1, y1, x2, y2);
        return ans;
    }

}
