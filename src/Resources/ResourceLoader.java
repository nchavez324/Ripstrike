package Resources;

import project.model.util.Global;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import project.model.gfx.Soldier;
import project.model.maps.CollisionMap;
import project.model.maps.Map;
import project.model.util.SoldierPivots;
import project.model.util.Vector2;
import project.model.util.WeaponData;

public class ResourceLoader {
	
    public HashMap<String, Image> characters;

    public HashMap<String, Image> etc;
    
    public HashMap<String, Image> match;

    public HashMap<String, Image> ui;

    public HashMap<String, Image> weapons;

    public HashMap<String, Image> grenades;

    public HashMap<String, WeaponData> weaponData;

    public HashMap<String, Map> maps;

    public SoldierPivots[][] pivots;

    public long startTime;

    public static String[] mapNames;

    public static String[] etcFiles;

    public static String[] matchFiles;
    
    public static String[] uiFiles;

    public static String[] grenadeFiles;

    public static String[] weaponFiles;

    public static String[] characterFiles;

    public ResourceLoader() {
        startTime = System.currentTimeMillis();
        init();
        load();
        System.out.println("Took " + ((System.currentTimeMillis() - startTime) / 1000f) + " seconds to load assets.");
    }

    private void init() {
        characters = new HashMap<String, Image>();
        etc = new HashMap<String, Image>();
        match = new HashMap<String, Image>();
        ui = new HashMap<String, Image>();
        weapons = new HashMap<String, Image>();
        grenades = new HashMap<String, Image>();
        weaponData = new HashMap<String, WeaponData>();
        maps = new HashMap<String, Map>();
    }

    public void load() {
        pullFiles();
        loadFolder(characters, characterFiles, "Characters");
        loadFolder(etc, etcFiles, "Etc");
        loadFolder(match, matchFiles, "Match");
        loadFolder(ui, uiFiles, "Interface");
        loadFolder(grenades, grenadeFiles, "Grenades");
        loadFolder(weapons, weaponFiles, "Weapons");
        loadWeaponData();
        loadPivots();
        loadMaps();
    }

    public void loadFolder(HashMap<String, Image> folder, String[] names, String srcHead) {
        for (int i = 0; i < names.length; i++) {
            folder.put(names[i],
                    Global.loadImage(getClass().getResource("Images/" + srcHead + "/" + names[i] + ".png")));
        }
    }

    public void loadMaps() {
        for (int i = 0; i < mapNames.length; i++) {
            String s = mapNames[i];
            String path = "Maps/" + s + ".cmp";
            try {
                ObjectInputStream obj_in = new ObjectInputStream(getClass().getResourceAsStream(path));
                Object obj = obj_in.readObject();
                if (obj instanceof CollisionMap) {
                    CollisionMap cmap = (CollisionMap) obj;
                    cmap.position = new Vector2(150, 242 - cmap.height * CollisionMap.SIZE + CollisionMap.SIZE);
                    Image[] layers = new Image[cmap.numLayers];
                    for (int k = 0; k < cmap.numLayers; k++) {
                        layers[k] = Global.loadImage(getClass().getResource("Maps/" + s + k + ".png"));
                    }
                    Image bg;
                    bg = Global.loadImage(getClass().getResource("Maps/" + s + "@" + ".png"));
                    maps.put(s, new Map(layers, bg, cmap, cmap.compLayer));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadWeaponData() {
        // <name>(handPosition)[weaponPosition](accuracy)(damage)(dispersion){oneHanded}{scope}{explosive}{melee}
        ArrayList<String> data = parse("Images/Weapons/weapons.dat");
        for (String line : data) {
            String name = line.substring(line.indexOf('<') + 1, line.indexOf('>'));
            String wp = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
            int wpX = Integer.parseInt(wp.substring(0, wp.indexOf(',')));
            int wpY = Integer.parseInt(wp.substring(wp.indexOf(',') + 1));
            String feed = line.substring(line.indexOf('('));
            float accuracy = Float.parseFloat(feed.substring(1, feed.indexOf(')')));
            feed = feed.substring(feed.indexOf(')') + 1);
            float damage = Float.parseFloat(feed.substring(1, feed.indexOf(')')));
            int[] ints = new int[6];
            for (int i = 0; i < ints.length; i++) {
                feed = feed.substring(feed.indexOf(')') + 1);
                ints[i] = Integer.parseInt(feed.substring(1, feed.indexOf(')')));
            }
            boolean[] bools = new boolean[3];
            for (int i = 0; i < bools.length; i++) {
                String yn = feed.substring(feed.indexOf('{') + 1, feed.indexOf('}'));
                feed = feed.substring(feed.indexOf('}') + 1);
                if (yn.compareTo("Y") == 0 || yn.compareTo("y") == 0) {
                    bools[i] = true;
                } else {
                    bools[i] = false;
                }
            }
            weaponData.put(name, new WeaponData(new Vector2(wpX, wpY), accuracy, damage, ints[0], ints[1], ints[2],
                    ints[3], ints[4], ints[5], bools[0], bools[1], bools[2]));
        }
    }

    public void loadPivots() {

        int numFactions, numBodyParts;
        numFactions = Soldier.Faction.values().length;
        numBodyParts = Soldier.BodyParts.values().length;
        pivots = new SoldierPivots[numFactions][numBodyParts];
        // (Pivot)[Anchor]<RootAnchor>

        for (Soldier.Faction f : Soldier.Faction.values()) {
            ArrayList<String> input = parse("Images/Characters/" + f.toString() + "/pivots.txt");
            if (input != null) {
                for (int i = 0; i < Soldier.BodyParts.values().length; i++) {
                    SoldierPivots p = new SoldierPivots();
                    String line = input.get(i);
                    int p1, p2, s1, s2, g1, g2, c1, c2;
                    p1 = line.indexOf('(');
                    p2 = line.indexOf(')');
                    s1 = line.indexOf('[');
                    s2 = line.indexOf(']');
                    g1 = line.indexOf('<');
                    g2 = line.indexOf('>');
                    c1 = line.indexOf('{');
                    c2 = line.indexOf('}');
                    String coords;
                    if (p1 != -1 && p2 != -1) {
                        coords = line.substring(p1 + 1, p2);
                        p.pivot = new Vector2();
                        p.pivot.X = Integer.parseInt(coords.substring(0, coords.indexOf(',')));
                        p.pivot.Y = Integer.parseInt(coords.substring(coords.indexOf(',') + 1));
                    }

                    if (s1 != -1 && s2 != -1) {
                        coords = line.substring(s1 + 1, s2);
                        p.anchor = new Vector2();
                        p.anchor.X = Integer.parseInt(coords.substring(0, coords.indexOf(',')));
                        p.anchor.Y = Integer.parseInt(coords.substring(coords.indexOf(',') + 1));
                    }
                    if (g1 != -1 && g2 != -1) {
                        coords = line.substring(g1 + 1, g2);
                        p.rootAnchor = new Vector2();
                        p.rootAnchor.X = Integer.parseInt(coords.substring(0, coords.indexOf(',')));
                        p.rootAnchor.Y = Integer.parseInt(coords.substring(coords.indexOf(',') + 1));
                    }
                    if (c1 != -1 && c2 != -1) {
                        p.flipOff = Integer.parseInt(line.substring(c1 + 1, c2));
                    }

                    pivots[f.num][i] = p;
                }
            }

        }
    }

    public ArrayList<String> parse(String dir) {
        ArrayList<String> ans = new ArrayList<String>();
        String strLine = "";
        InputStream is;
        InputStreamReader isr;
        BufferedReader br;
        try {
            is = getClass().getResourceAsStream(dir);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
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
                is.close();
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

    public void pullFiles() {
        mapNames = this.parse("Maps/assets.load").toArray(new String[0]);
        etcFiles = this.parse("Images/Etc/assets.load").toArray(new String[0]);
        matchFiles = this.parse("Images/Match/assets.load").toArray(new String[0]);
        uiFiles = this.parse("Images/Interface/assets.load").toArray(new String[0]);
        grenadeFiles = this.parse("Images/Grenades/assets.load").toArray(new String[0]);
        weaponFiles = this.parse("Images/Weapons/assets.load").toArray(new String[0]);
        characterFiles = this.parse("Images/Characters/assets.load").toArray(new String[0]);
    }
    
}
