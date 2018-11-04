package project.model.gfx.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import project.model.gfx.core.Component;
import project.model.gfx.core.Sprite;
import project.model.util.Vector2;

public class StatMetric extends Sprite implements Component {

	public int percentage;

	public String name;

	public int width, height;

	public StatMetric(int percentage, int width, int height, String name) {
		super();
		this.percentage = percentage;
		this.position = new Vector2();
		this.width = width;
		this.height = height;
		this.name = name;
		if (percentage < 0)
			this.percentage = 0;
		if (percentage > 100)
			this.percentage = 100;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		FontMetrics fm = g.getFontMetrics(g.getFont());
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(percentage + "", g);
		Font f = g.getFont();
		Font n = new Font(f.getFontName(), Font.PLAIN,
				(int) ((f.getSize()) / 1.5));
		g.setFont(n);
		g.drawString(name, position.X,
				(int) (position.Y - rect.getHeight() + 18));
		/*g.drawString(percentage + "",
				position.X + width - (int) (rect.getWidth()), position.Y
						- (float) (rect.getHeight()) + 18);*/
		g.setColor(Color.BLACK);
		g.fillRoundRect((int) (position.X), (int) (position.Y), width, height,
				5, 5);
		g.setColor(Color.RED);
		g.fillRoundRect((int) (position.X), (int) (position.Y),
				(int) (width * ((float) percentage / 100)), height, 5, 5);
		g.setFont(f);
	}
}
