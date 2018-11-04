package project.model;

import Resources.ResourceLoader;
import project.Runner.Scene;
import project.model.gfx.core.Component;
import project.model.gfx.core.Sprite;
import project.model.gfx.ui.Cursor;
import project.model.gfx.ui.OptionsUI;
import project.model.gfx.ui.Reticule;
import project.model.util.GameOptions;
import project.model.util.PlayerOptions;
import project.model.util.Global;
import project.model.util.Vector2;
import java.awt.Graphics2D;
import java.util.HashMap;

public class MenuScene extends GameScene {

	public static final int LOGO = 0;
	public static final int OPTIONSUI = 1;
	public static final int CURSOR = 2;
	public HashMap<Integer, Component> components;
	public ResourceLoader depot;
	public Cursor cursor;
	public Sprite logo;
	public OptionsUI options;

	public MenuScene() {
		type = Scene.MENU;
		components = new HashMap<Integer, Component>();
		initComponents();
	}

	@Override
	public void draw(Graphics2D g) {
		Integer[] keys = components.keySet().toArray(new Integer[0]);
		for (int i = 0; i < keys.length; i++) {
			components.get(keys[i]).draw(g);
		}
	}

	@Override
	public void update(long timePassed) {
		Integer[] keys = components.keySet().toArray(new Integer[0]);
		for (int i = 0; i < keys.length; i++) {
			components.get(keys[i]).update(timePassed);
		}
		if (logo.position.Y >= Global.topInset - 4) {
			logo.velocity.Y = 0;
		}

		if (options.position.X <= 0 && options.velocity.X == -2.4f) {
			options.setVelocity(new Vector2(0, 0));
			options.setPosition(new Vector2(0, 0));
		}
		if (options.position.X >= 0 && options.velocity.X == 2.5f) {
			options.setVelocity(new Vector2(0, 0));
			options.setPosition(new Vector2(0, 0));
		}
		if (options.position.X <=  - Global.screenWidth
				&& options.velocity.X == -2.5f) {
			options.setVelocity(new Vector2(0, 0));
			options.setPosition(new Vector2(-Global.screenWidth, 0));
		}

	}

	public void initComponents() {
		depot = Global.depot;
		logo = new Sprite(depot.ui.get("smallLogo"));
		logo.position.X = (Global.screenWidth - logo.getWidth()) / 2;
		logo.position.Y = -logo.getHeight();
		logo.velocity.Y = .25f;
		components.put(LOGO, logo);
		options = new OptionsUI();
		options.setPosition(new Vector2(Global.screenWidth, 0));
		options.setVelocity(new Vector2(-2.4f, 0));
		components.put(OPTIONSUI, options);
		cursor = new Reticule();
		components.put(CURSOR, cursor);
	}

	public PlayerOptions getPlayerOptions() {
		return options.getPlayerOptions();
	}

	public GameOptions getGameOptions() {
		return options.getGameOptions(options.getPlayerOptions());
	}
}
