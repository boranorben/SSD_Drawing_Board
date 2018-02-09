package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class CompositeGObject extends GObject {

	private List<GObject> gObjects;

	public CompositeGObject() {
		super(0, 0, 0, 0);
		gObjects = new ArrayList<GObject>();
	}

	public void add(GObject gObject) {
		gObjects.add(gObject);
		recalculateRegion();
	}

	public void remove(GObject gObject) {
		gObjects.remove(gObject);
		recalculateRegion();
	}

	@Override
	public void move(int dX, int dY) {
		super.move(dX, dY);
		for (GObject g : gObjects) {
			g.move(dX, dY);
		}
	}
	
	public void recalculateRegion() {
		int minX = this.gObjects.get(0).x;
		int minY = this.gObjects.get(0).y;
		int maxX = this.gObjects.get(0).x + this.gObjects.get(0).width;
		int maxY = this.gObjects.get(0).y + this.gObjects.get(0).height;
		for (GObject g : gObjects) {
			minX = Math.min(g.x, minX);
			maxX = Math.max(g.x + g.width, maxX);
			minY = Math.min(g.y, minY);
			maxY = Math.max(g.y + g.height, maxY);
		}
		x = minX;
		y = minY;
		width = maxX - minX;
		height = maxY - minY;
	}

	@Override
	public void paintObject(Graphics g) {
		for (GObject gOb : gObjects) {
			gOb.paintObject(g);
		}
	}

	@Override
	public void paintLabel(Graphics g) {
		g.drawString("Composite", x, y);
		g.setColor(Color.BLACK);
	}
	
}
