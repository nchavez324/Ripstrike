package project.view;

import Resources.ResourceLoader;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import project.model.GameScene;
import project.model.util.Global;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import project.model.gfx.Background;

public class ViewCore {

    public ScreenManager screenmanager; //The frame wrapper class.
    public Display mode;
    private static DisplayMode modes[] = {
        new DisplayMode(Global.screenWidth, Global.screenHeight, 32, 0),
        new DisplayMode(Global.screenWidth, Global.screenHeight, 24, 0),
        new DisplayMode(Global.screenWidth, Global.screenHeight, 16, 0),};
    public GameScene engine;

    public enum Display {

        FULLSCREEN, WINDOW
    }

    public ViewCore(Display mode, GameScene engine) {
        this.mode = mode;
        this.engine = engine;
        init();
    }

    public void switchScene(GameScene engine){
        this.engine = engine;
    }
    
    public void init() {
        screenmanager = new ScreenManager(mode);
        if (mode == Display.FULLSCREEN) {
            DisplayMode dm = screenmanager.findFirstCompatibleMode(modes);
            screenmanager.setFullScreen(dm);
        }

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        // Set the blank cursor to the JFrame. 
        ((JFrame) screenmanager.getWindow()).getContentPane().setCursor(blankCursor);
        Font f = new Font("Dialog", Font.BOLD, 17);
        ((JFrame) screenmanager.getWindow()).setFont(f);
        
        ResourceLoader temp = new ResourceLoader();
        List<Image> icons  = new ArrayList<Image>();
        icons.add(temp.ui.get("32icon"));
        icons.add(temp.ui.get("16icon"));
        ((JFrame)screenmanager.getWindow()).setIconImages(icons);
        
        Global.topInset = screenmanager.getWindow().getInsets().top;
        Global.leftInset = screenmanager.getWindow().getInsets().left;
        Global.bottomInset = screenmanager.getWindow().getInsets().bottom;
        Global.rightInset = screenmanager.getWindow().getInsets().right;
    }

    public void draw(Background std) {
        Graphics2D g = screenmanager.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        std.draw(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Global.screenWidth + Global.leftInset, Global.screenHeight + Global.topInset);
        engine.draw(g);
        screenmanager.update(); //Refreshes the window/screen.
    }

    public Window getWindow() {
        return screenmanager.getWindow();
    }

    public void restoreScreen() {
        screenmanager.restoreScreen();
    }
}
