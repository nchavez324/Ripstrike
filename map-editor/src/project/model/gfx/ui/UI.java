package project.model.gfx.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import project.model.gfx.core.Component;
import project.model.util.Global;
import project.model.util.Vector2;
import project.model.maps.CollisionMap;
import project.model.maps.CollisionTile;
import project.model.maps.CollisionTile.Type;

public class UI implements Component {

    public Image bg;

    public Image savePNG;

    public Type currentType;

    public Rectangle viewBound;

    public HashMap<String, Button> buttons;

    public Cursor cursor;

    public CollisionMap map;

    public UI() {
        loadResources();
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(bg, Global.leftInset, Global.topInset, null);
        g.setColor(Color.WHITE);
        g.drawString(currentType.toString(), 715, 60);
        String[] keys = buttons.keySet().toArray(new String[0]);
        for (String k : keys) {
            buttons.get(k).draw(g);
        }
        cursor.draw(g);
        g.setColor(Color.RED);
        g.drawRect(viewBound.x, viewBound.y, viewBound.width, viewBound.height);
        
    }

    @Override
    public void update(long timePassed) {
        String[] keys = buttons.keySet().toArray(new String[0]);
        for (String k : keys) {
            buttons.get(k).update(timePassed);
        }
        cursor.update(timePassed);
    }

    @Override
    public void loadResources() {
        bg = Global.depot.ui.get("uiBase");
        buttons = new HashMap<String, Button>();
        cursor = new Reticule();

        Button block = new Button(Global.depot.ui.get("button"), "Block", 75, true);
        block.position = new Vector2(705, 80);
        Button empty = new Button(Global.depot.ui.get("button"), "Empty", 75, true);
        empty.position = new Vector2(705, 140);
        Button slope = new Button(Global.depot.ui.get("button"), "Slope", 75, true);
        slope.position = new Vector2(705, 200);

        Button newMap = new Button(Global.depot.ui.get("button"), "New...", 75, true);
        newMap.position = new Vector2(705, 360);
        Button open = new Button(Global.depot.ui.get("button"), "Open...", 75, true);
        open.position = new Vector2(705, 420);
        Button save = new Button(Global.depot.ui.get("button"), "Save...", 75, true);
        save.position = new Vector2(705, 480);
        Button export = new Button(Global.depot.ui.get("button"), "Export...", 75, true);
        export.position = new Vector2(705, 540);

        buttons.put(block.msg, block);
        buttons.put(empty.msg, empty);
        buttons.put(slope.msg, slope);

        buttons.put(newMap.msg, newMap);
        buttons.put(open.msg, open);
        buttons.put(save.msg, save);
        buttons.put(export.msg, export);

        currentType = Type.BLOCK;
        
        viewBound = new Rectangle(35 + Global.leftInset, 35 + Global.topInset, 640, 531);
    }

    public void openFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Collision Map Files", "cmp");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(Global.game.view.getWindow());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            Global.depot.loadMap(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void saveFile() {
        if (map == null) {
            System.out.println("There's nothing to save!");
        } else {
            try {
                String numLyrS = (String) JOptionPane.showInputDialog(Global.game.view.getWindow(),
                        "How many image layers?", "Saving...", JOptionPane.PLAIN_MESSAGE, null, null, null);
                String compLyrS = (String) JOptionPane.showInputDialog(Global.game.view.getWindow(),
                        "What layer is the component layer?", "Saving...", JOptionPane.PLAIN_MESSAGE, null, null, null);
                numLyrS = numLyrS.trim();
                compLyrS = compLyrS.trim();
                map.numLayers = Integer.parseInt(numLyrS);
                map.compLayer = Integer.parseInt(compLyrS);

                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Collision Map Files", "cmp");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showSaveDialog(Global.game.view.getWindow());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    Global.depot.saveMap(chooser.getSelectedFile().getAbsolutePath());
                }
            } catch (Exception e) {
                //
            }
        }
    }

    public void place(int x, int y) {
        if (map != null
                && (cursor.position.X > viewBound.x && cursor.position.X < viewBound.x + viewBound.width
                        && cursor.position.Y > viewBound.y && cursor.position.Y < viewBound.y + viewBound.width)) {
            int selX = (int) ((x - Global.game.main.grid.offset.X - map.position.X - CollisionMap.SIZE / 2) / CollisionMap.SIZE);
            int selY = (int) ((y - Global.game.main.grid.offset.Y - map.position.Y - CollisionMap.SIZE / 2) / CollisionMap.SIZE);

            if (selX < map.width && selX >= 0 && selY < map.height && selY >= 0) {
                if (currentType == Type.BLOCK) {
                    map.tiles[selX][selY] = new CollisionTile(CollisionTile.Type.BLOCK);
                } else if (currentType == Type.EMPTY) {
                    map.tiles[selX][selY] = new CollisionTile(CollisionTile.Type.EMPTY);
                } else if (currentType == Type.SLOPE) {
                    String mS = (String) JOptionPane.showInputDialog(Global.game.view.getWindow(),
                            "Please specify a slope value:", "Slope specifier", JOptionPane.PLAIN_MESSAGE, null, null,
                            null);
                    String bS = (String) JOptionPane.showInputDialog(Global.game.view.getWindow(),
                            "Please specify a y-intercept from 0 to 1:", "Slope specifier", JOptionPane.PLAIN_MESSAGE,
                            null, null, null);
                    try {
                        mS = mS.trim();
                        bS = bS.trim();
                        float m = Float.parseFloat(mS);
                        float b = Float.parseFloat(bS);
                        map.tiles[selX][selY] = new CollisionTile(-m, 1 - b);
                    } catch (Exception e) {
                        //
                    }
                    cursor.down = false;
                }
            }

        }
    }

    public void export() {
        if (map != null) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showSaveDialog(Global.game.view.getWindow());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                if (path.substring(path.length() - 4).compareTo(".png") != 0) {
                    path += ".png";
                }
                Global.depot.savePNG(path);
            }
        }else{
            System.out.println("No map to export!");
        }
    }
}
