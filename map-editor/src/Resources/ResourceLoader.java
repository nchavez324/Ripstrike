package Resources;

import project.model.MainScene;
import project.model.util.Global;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import project.model.maps.CollisionMap;
import project.model.maps.CollisionTile;
import project.model.maps.CollisionTile.Type;
import project.model.util.Vector2;

public class ResourceLoader {
	
    public HashMap<String, Image> ui;

    public CollisionMap loadedMap;

    public long startTime;

    public static String[] etcFiles = {};

    public static String[] uiFiles = { "opening", "PressEnter", "16icon", "32icon", "button", "stdbg", "smallLogo",
            "blueRet", "redRet", "backColor", "frontColor", "arrow", "arrowButton", "popup", "transBlk", "uiBase" };

    public static String[] tilesetsFiles = {};

    public ResourceLoader() {
        startTime = System.currentTimeMillis();
        init();
        load();
    }

    private void init() {
        ui = new HashMap<String, Image>();
    }

    public void load() {
        loadFolder(ui, uiFiles, "Interface");
    }

    public void loadFolder(HashMap<String, Image> folder, String[] names, String srcHead) {
        for (int i = 0; i < names.length; i++) {
            folder.put(names[i],
                    Global.loadImage(getClass().getResource("Images/" + srcHead + "/" + names[i] + ".png")));
        }
    }

    public void loadMap(String path) {
        try {
            FileInputStream f_in = new FileInputStream(path);
            ObjectInputStream obj_in = new ObjectInputStream(f_in);
            Object obj = obj_in.readObject();
            if (obj instanceof CollisionMap) {
                loadedMap = (CollisionMap) obj;
                loadedMap.position = new Vector2(150, 242 - loadedMap.height * CollisionMap.SIZE + CollisionMap.SIZE);
                Global.game.main.map = loadedMap;
                Global.game.main.components.put(MainScene.MAP, loadedMap);
                Global.game.main.ui.map = loadedMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMap(String path) {
        if (path.substring(path.length() - 4).compareTo(".cmp") != 0) {
            path += ".cmp";
        }
        try {
            FileOutputStream f_out = new FileOutputStream(path);
            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
            obj_out.writeObject(loadedMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePNG(String path) {
        int width = loadedMap.width * CollisionMap.SIZE;
        int height = loadedMap.height * CollisionMap.SIZE;
        BufferedImage bi = Global.game.view.screenmanager.createCompatibleImage(width, height, 1);
        bi.createGraphics();
        Graphics2D g = (Graphics2D) bi.getGraphics();
        drawMap(g, width, height);
        try {
            ImageIO.write(bi, "png", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void drawMap(Graphics2D g, int width, int height){
        Composite orig = g.getComposite();
        g.setColor(Color.ORANGE);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
        g.fillRect(0, 0, width * CollisionMap.SIZE, height * CollisionMap.SIZE);
        g.setComposite(orig);
        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(3));
        for (int x = 0; x < loadedMap.tiles.length; x++) {
            for (int y = 0; y < loadedMap.tiles[x].length; y++) {
                CollisionTile t = loadedMap.tiles[x][y];
                if (t.type == Type.SLOPE) {
                    Vector2 p1 = new Vector2(CollisionMap.SIZE * x, CollisionMap.SIZE * y);
                    Vector2 p2 = new Vector2(CollisionMap.SIZE * x, CollisionMap.SIZE * y);
                    t.calcPts(p1, p2);
                    g.drawLine((int) (p1.X), (int) (p1.Y), (int) (p2.X), (int) (p2.Y));
                } else if (t.type == Type.BLOCK) {
                    g.drawRect((int) (CollisionMap.SIZE * x), (int) (CollisionMap.SIZE * y), CollisionMap.SIZE,
                            CollisionMap.SIZE);
                }
            }
        }
    }
    
    public ArrayList<String> parse(String dir, boolean abs) {
        ArrayList<String> ans = new ArrayList<String>();
        String strLine = "";
        InputStream is = null;
        FileInputStream fis = null;
        InputStreamReader isr;
        BufferedReader br;
        try {
            if (abs) {
                File file = new File(dir);
                fis = new FileInputStream(file);
                isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);
            } else {
                is = getClass().getResourceAsStream(dir);
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
            }
            try {
                while ((strLine = br.readLine()) != null) {
                    if (!(strLine.compareTo("") == 0)) {
                        if (!(strLine.length() >= 2 && (strLine.substring(0, 2).compareTo("//") == 0))) {
                            strLine = strLine.trim();
                            ans.add(strLine);
                        }
                    }
                }
                br.close();
                isr.close();
                if (is != null)
                    is.close();
                if (fis != null)
                    fis.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    public int numOccurences(String s, char c) {
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                ans++;
            }
        }
        return ans;
    }
}
