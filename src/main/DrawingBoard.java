package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import objects.*;

public class DrawingBoard extends JPanel {

	private MouseAdapter mouseAdapter; 
	private List<GObject> gObjects;
	private GObject target;
	
	private int gridSize = 10;
	
	public DrawingBoard() {
		gObjects = new ArrayList<GObject>();
		mouseAdapter = new MAdapter();
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setPreferredSize(new Dimension(800, 600));
	}
	
	public void addGObject(GObject gObject) {
		gObjects.add(gObject);
		repaint();
	}
	
	public void groupAll() {
		CompositeGObject composit = new CompositeGObject();
		for (GObject g : gObjects) {
			g.deselected();
			composit.add(g);
		}
		clear();
		composit.recalculateRegion();
		gObjects.add(composit);
		repaint();
	}

	public void deleteSelected() {
		gObjects.remove(target);
		repaint();
	}
	
	public void clear() {
		gObjects.clear();
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintBackground(g);
		paintGrids(g);
		paintObjects(g);
	}

	private void paintBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private void paintGrids(Graphics g) {
		g.setColor(Color.lightGray);
		int gridCountX = getWidth() / gridSize;
		int gridCountY = getHeight() / gridSize;
		for (int i = 0; i < gridCountX; i++) {
			g.drawLine(gridSize * i, 0, gridSize * i, getHeight());
		}
		for (int i = 0; i < gridCountY; i++) {
			g.drawLine(0, gridSize * i, getWidth(), gridSize * i);
		}
	}

	private void paintObjects(Graphics g) {
		for (GObject go : gObjects) {
			go.paint(g);
		}
	}

	class MAdapter extends MouseAdapter {
		
		int dX = 0, dY = 0;
		
		private void deselectAll() {
			target = null;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			boolean hit = false;
			for (GObject g : gObjects) {
				if (g.pointerHit(e.getX(), e.getY())) {
					g.selected();
					target = g;
					hit = true;
				} else g.deselected();
			}
			if (!hit) {
				deselectAll();
			} else {
				dX = e.getX();
				dY = e.getY();
			}
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int x, y;
			if (target != null) {
				x = e.getX() - this.dX;
				y = e.getY() - this.dY;
				this.dX = e.getX();
				this.dY = e.getY();
				target.move(x, y);
			}
			repaint();
		}
	}
	
}