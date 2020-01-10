/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ui;

import com.charlware.ants.AntHome;
import com.charlware.ants.FoodStorage;
import com.charlware.ants.Marker;
import com.charlware.ants.World;
import com.charlware.ants.ant.Ant;
import com.charlware.ants.ant.QueenAnt;
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
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author CVanJaarsveldt
 */
public class WorldPanel extends JPanel {

    public static int multiplier = 12;

    private Timer timer;
    private int baseTimerSpeed = 100;

    private final World world;

    private List<MappableEntity> tracked = new ArrayList<>();

    private List<SimulationStepListener> stepListeners = new ArrayList<>(5);

    private AntHome highlightedAntHome = null;

    private Font small = new Font("Helvetica", Font.PLAIN, multiplier);
    private Font bold = small.deriveFont(Font.BOLD);
    
    private Color DARK_GREEN = Color.GREEN.darker();

    public WorldPanel(World world) {
        this.world = world;

//		addMouseListener(this);
        setupMouseListener();

        createTimer(1);
        
//		timer.start();
        revalidate();
        repaint();
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
    
    public void setSpeedMultiplier(int multiplier) {
        boolean timerStarted = timer == null ? false : timer.isRunning();
        if(timer != null) {
            stop();
            timer = null;
        }
        createTimer(multiplier);
        if(timerStarted) {
            start();
        }
    }

    private void createTimer(int multiplier) {
        timer = new Timer(baseTimerSpeed / multiplier, (e) -> {
            world.step();
            repaint();
            fireStepEvent();
        });
    }
    
    public void setHighlightedAntHome(AntHome antHome) {
        this.highlightedAntHome = antHome;
    }

    private int getScreenX(int x) {
        return x * multiplier;
    }

    private int getScreenY(int y) {
        return y * multiplier + multiplier;
    }

    private int getWorldX(int x) {
        return x / multiplier;
    }

    private int getWorldY(int y) {
        return y / multiplier;
    }

    @Override
    public Dimension getPreferredSize() {
        int width = world.getSize() * multiplier;
        Dimension dim = new Dimension(width, width);
        System.out.println("Dimension: " + dim);
        return dim;
    }

    @Override
    public Dimension getSize() {
        return getPreferredSize();
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

    private void drawAntHome(Graphics2D g, AntHome antHome) {
        // Highlight the whole ant home.
        if (antHome.equals(highlightedAntHome)) {
            g.setFont(bold);
        }

        for (Ant ant : antHome.getAnts()) {
            if (ant.isHungry()) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLUE);
            }
            String ch = ant instanceof QueenAnt ? "q" : "a";
            //g.drawString("a", getScreenX(ant.getX()), getScreenY(ant.getY()));
            drawEntity(g, ch, ant);
        }

        g.setColor(Color.BLACK);
        drawEntity(g, "H", antHome);

        // Reset the font
        g.setFont(small);
    }

    public void drawWorld(Graphics2D g) {
        g.setFont(small);
        int size = world.getSize();

        // Foodstores
        for (FoodStorage fs : world.getFoodSources()) {
            g.setColor(fs.hasFood() ? DARK_GREEN : Color.RED);
            if(fs.isTemporary()) 
                g.setFont(bold);
            drawEntity(g, "F", fs);
        }

        // Markers
        for (Marker marker : world.getMarkers()) {
            g.setFont(small);
            g.setColor(marker.getStrength() > 50 ? Color.BLUE : Color.GRAY);
            drawEntity(g, ".", marker);
        }

        // Ant Homes
        for (AntHome antHome : world.getAntHomes()) {
            if(!antHome.isDead()) {
                g.setFont(small);
                drawAntHome(g, antHome);
            }
        }

        // Labels
        for (MappableEntity entity : tracked) {
            g.setFont(small);
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

    public void setupMouseListener() {
        MouseAdapter ma = new MouseAdapter() {
            private Point origin;

            @Override
            public void mouseClicked(MouseEvent e) {
                int button = e.getButton();
                Point p = e.getPoint();
                int x = getWorldX(p.x);
                int y = getWorldY(p.y);

                //System.out.println("Mouse Clicked: " + p + ":   " + x + ";" + y);
                if (button == MouseEvent.BUTTON1) {
                    MappableEntity o = world.getEntityAt(x, y);
                    if (o != null) {
                        System.out.println(o);
                        if (tracked.contains(o)) {
                            tracked.remove(o);
                        } else {
                            tracked.add(o);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                origin = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, WorldPanel.this);
                    if (viewPort != null) {
                        int deltaX = origin.x - e.getX();
                        int deltaY = origin.y - e.getY();

                        Rectangle view = viewPort.getViewRect();
                        view.x += deltaX;
                        view.y += deltaY;

                        scrollRectToVisible(view);
                    }
                }
            }

        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

}
