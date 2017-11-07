/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ui;

import com.charlware.ants.FoodStorage;
import com.charlware.ants.Marker;
import com.charlware.ants.World;
import com.charlware.ants.ant.Ant;
import com.charlware.ants.sim.MappableEntity;
import com.charlware.ants.sim.MatrixLocation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author CVanJaarsveldt
 */
public class WorldPanel extends JPanel implements MouseListener {
	
	public static int multiplier = 12;
	
	private Timer timer;
	
	private final World world;
	
	private List<MappableEntity> tracked = new ArrayList<>();
	
	private List<SimulationStepListener> stepListeners = new ArrayList<>(5);
	
	public WorldPanel(World world) {
		this.world = world;
		
		addMouseListener(this);
		
		timer = new Timer(100, (e) -> {
			world.step();
			repaint();
			fireStepEvent();
		});
//		timer.start();
	}
	
	public void start() {
		timer.start();
	}
	
	public void stop() {
		timer.stop();
	}
	
	private int getScreenX(int x) {
		return x*multiplier;
	}

	private int getScreenY(int y) {
		return y*multiplier + multiplier;
	}
	
	private int getWorldX(int x) {
		return x / multiplier;
	}
	
	private int getWorldY(int y) {
		return y / multiplier;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(world.getSize(), world.getSize());
	}
	
	private boolean isTracked(MappableEntity o) {
		return tracked.contains(o);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawWorld((Graphics2D) g);
	}
	
	private void drawEntity(Graphics2D g, String id, MappableEntity entity) {
		MatrixLocation loc = (MatrixLocation) entity.getLocation();
		g.drawString(id, getScreenX(loc.getX()), getScreenY(loc.getY()));
	}
	
	private void drawInfo(Graphics2D g, MappableEntity entity) {
		MatrixLocation loc = (MatrixLocation) entity.getLocation();
		g.drawString(entity.toString(), getScreenX(loc.getX()), getScreenY(loc.getY() + 1));
	}
	
	public void drawWorld(Graphics2D g) {
		Font small = new Font("Helvetica", Font.PLAIN, multiplier);
		FontMetrics mtr = getFontMetrics(small);
		g.setFont(small);
		int size = world.getSize();
//		for(int r = 0; r < size; r++) {
//			for(int c = 0; c < size; c++) {
//				g.drawString(".", getScreenX(c), getScreenY(r));
//			}
//		}

		List<Ant> ants = world.getAnts();
		for(Ant ant: ants) {
			if(ant.isHungry()) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.BLUE);
			}
			//g.drawString("a", getScreenX(ant.getX()), getScreenY(ant.getY()));
			drawEntity(g, "a", ant);
		}

		
		for(FoodStorage fs: world.getFoodSources()) {
			g.setColor(fs.hasFood()? Color.GREEN : Color.RED);
			//g.drawString("F", getScreenX(fs.getX()), getScreenY(fs.getY()));
			drawEntity(g, "F", fs);
		}
		
		for(Marker marker: world.getMarkers()) {
			g.setColor(marker.getStrength() > 50 ? Color.BLUE : Color.GRAY);
//			g.drawString(".", getScreenX(marker.getX()), getScreenY(marker.getY()));
			drawEntity(g, ".", marker);
		}
		
		g.setColor(Color.BLACK);
		//g.drawString("H", getScreenX(antHome.getX()), getScreenY(antHome.getY()));
		drawEntity(g, "H", world.getAntHome());
		
		for(MappableEntity entity: tracked) {
			drawInfo(g, entity);
		}
	}
	
	public void addStepListener(SimulationStepListener l) {
		stepListeners.add(l);
	}
	
	public void removeStepListener(SimulationStepListener l) {
		stepListeners.remove(l);
	}
	
	public void fireStepEvent() {
		stepListeners.forEach(l -> l.simulationStepped());
	}
	
	public static void main(String[] args) {

		World world = new World(20);

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setSize(350, 350);
		
		Container c = f.getRootPane();
		WorldPanel p = new WorldPanel(world);
		c.setLayout(new BorderLayout());
		c.add(p, BorderLayout.CENTER);
		
		
		EventQueue.invokeLater(() -> {
			f.setVisible(true);
		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int button = e.getButton();
		Point p = e.getPoint();
		int x = getWorldX(p.x);
		int y = getWorldY(p.y);
		
		//System.out.println("Mouse Clicked: " + p + ":   " + x + ";" + y);
		
		if(button == MouseEvent.BUTTON1) {
			MappableEntity o = world.getEntityAt(x, y);
			if(o != null) {
				System.out.println(o);
				if(tracked.contains(o)) {
					tracked.remove(o);
				}
				else {
					tracked.add(o);
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	
}
