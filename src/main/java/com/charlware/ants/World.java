/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import com.charlware.ants.ant.Ant;
import com.charlware.ants.ant.QueenAnt;
import com.charlware.ants.ant.WorkerAnt;
import com.charlware.ants.logs.EventLog;
import com.charlware.ants.logs.EventType;
import com.charlware.ants.sim.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.charlware.ants.sim.MappableEntity;
import com.charlware.ants.sim.MatrixLocation;
import com.charlware.ants.sim.MatrixMappableEntity;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 *
 * @author CVanJaarsveldt
 */
public final class World {
	private final int size;
	public static final Random random = new Random();
	
	private final Map<Location, AntHome> antHomeLocations = new HashMap<>();
	private final List<AntHome> antHomes = new ArrayList<>();
	private int antId = 0;
	private final List<Ant> ants = new ArrayList<>(10);
	private final List<FoodStorage> foodSources = new ArrayList<>(100);
	private final Map<MatrixLocation, Marker> markers = new HashMap<>();
	
	private final ArrayDeque<Runnable> actionQueue = new ArrayDeque<>();
	
	private final List<WorldListener> listeners = new ArrayList<>();
        
        private Configuration worldConfig;
        private SimSettings simSettings = new SimSettings();
        
        private final EventLog eventLog = new EventLog();
        
        private int failedAntHomes = 0;
	
	public World(int size) {
		this.size = size;
		
		// Create initial anthome
		createAntHome(size/2, size/2);
		
		
	}
        
        public void setConfigFile(String filename) throws ConfigurationException {
            Configurations configs = new Configurations();
            worldConfig = configs.properties(filename);
        }
        
        public Configuration getWorldConfig() {
            if(worldConfig == null) {
                worldConfig = new BaseConfiguration();
            }
            return worldConfig;
        }
	
        public SimSettings getSimSettings() {
            return simSettings;
        }
        
        public EventLog getEventLog() {
            return eventLog;
        }
        
	public int getSize() {
		return size;
	}
	
	
	public void createAntHome() {
		createAntHome(random.nextInt(size), random.nextInt(size));
	}
	
	public AntHome getAntHomeAt(Location location) {
		return antHomeLocations.get(location);
	}
	
	public List<AntHome> getAntHomes() {
		return antHomes;
	}
	
	public void createAntHome(final int x, final int y) {
		createAntHome(Location.at(x, y));
	}
	
	public void createAntHome(final Location location) {
		actionQueue.add(() -> {
			AntHome home = new AntHome(this);
			home.setLocation(location);
			
			antHomes.add(home);
			antHomeLocations.put(location, home);
			createAnt(home);
			
			fireAntHomeCreated(home);
		});
	}
	
	public void removeAntHome(AntHome home) {
		actionQueue.add(() -> {
			antHomeLocations.remove(home.getLocation());
			fireAntHomeRemoved(home);
		});	
	}
	
	public void createAnt(AntHome antHome) {
		actionQueue.add(() -> {
			Ant ant = new WorkerAnt(antHome, antId++, antHome.getX(), antHome.getY());
			ants.add(ant);
			antHome.addAnt(ant);
                        eventLog.log(antHome, EventType.ANT_BORN);
		});
	}
	
	public void createQueenAnt(AntHome antHome) {
		actionQueue.add(() -> {
			Ant queen = new QueenAnt(antHome, antId++);
			ants.add(queen);
			antHome.addAnt(queen);
		});
	}
	
	public void removeAnt(Ant ant) {
		actionQueue.add(() -> {
			ants.remove(ant);
			ant.getAntHome().removeAnt(ant);
		});
	}
	
	public void antDied(WorkerAnt ant) {
		// Have the ant drop its payload
		actionQueue.add(() -> {
			FoodStorage fs;
			AntHome antHome = getAntHomeAt(ant.getLocation());
			if(antHome != null) {
				fs = antHome.getFoodStorage();
			}
			else {
				fs = getFoodStorage(ant);
			}
			int f = simSettings.workerant_foodValue;
			int c = ant.getFoodStorage().getCurrent();
			int t = f + c;
			System.out.print("Ant Died. Available food: " + t + ". ");
			if(fs == null) {
				fs = new FoodStorage(
						this, 
						t, 
						t,
						ant.getLocation())
					.setRemoveWhenEmpty(true);
				foodSources.add(fs);
				System.out.println("Created new food source.");
			}
			else {
				fs.add(t);
				System.out.println("Added to existing food source: " + fs);
			}
                        
                        eventLog.log(ant.getAntHome(), EventType.ANT_DIED);
                        
                        // Check if this was the last ant of its antHome
                        antHome = ant.getAntHome();
                        if(antHome.isDead()) {
                            System.out.println("Ant Home died: " + antHome);
                            fs = antHome.getFoodStorage();
                            fs.setLocation(antHome.getLocation());
                            fs.setRemoveWhenEmpty(true);
                            fs.setRestock(0, 0);
                            removeAntHome(antHome);
                            addFoodSource(fs);
                            failedAntHomes++;
                        }
		});
	}
	
        public void addFoodSource(FoodStorage fs) {
            actionQueue.add(() -> foodSources.add(fs));
        }
        
	public void addStableFoodSource() {
		actionQueue.add(() -> {
			int x = random.nextInt(size);
			int y = random.nextInt(size);
			foodSources.add(new FoodStorage(this, 500, 500, x, y).setRestock(100, 50));
		});
	}
	
	public void addStableFoodSources(int num) {
		for(int i = 0; i < num; i++) {
			addStableFoodSource();
		}
	}
	
	public void addTempFoodSource() {
		actionQueue.add(() -> {
			int x = random.nextInt(size);
			int y = random.nextInt(size);
			foodSources.add(new FoodStorage(this, 10_000, 10_000, x, y).setRemoveWhenEmpty(true));		
		});
	}
	
	public void removeStableFoodSource() {
		actionQueue.add(() -> {
			int idx = random.nextInt(foodSources.size());
			foodSources.remove(idx);
		});
	}
	
	public void removeStableFoodSources(int num) {
		for(int i = 0; i < num; i++) {
			removeStableFoodSource();
		}
	}
	
	public void removeFoodStorage(FoodStorage fs) {
		actionQueue.add(() -> {
			foodSources.remove(fs);
		});
	}
	
	public MappableEntity getEntityAt(int x, int y) {
		Location loc = Location.at(x, y);
		AntHome antHome = getAntHomeAt(loc);
		if(antHome != null) {
			return antHome;
		}
		for(FoodStorage fs: foodSources) {
			if(fs.getLocation().equals(loc)) {
				return fs;
			}
		}
		for(Ant ant: ants) {
			if(ant.getLocation().equals(loc)) {
				return ant;
			}
		}
//		for(Marker marker: markers.values()) {
//			if(marker.getLocation().equals(loc)) {
//				return marker;
//			}
//		}
		return null;
	}
	
	public List<Ant> getAnts() {
		return ants;
	}
	
	public List<FoodStorage> getFoodSources() {
		return foodSources;
	}
	
	public Collection<Marker> getMarkers() {
		return markers.values();
	}
	
	public void step() {
		ants.forEach(ant -> ant.step());
		markers.values().forEach(marker -> marker.step());
		markers.entrySet().removeIf(entry -> entry.getValue().getStrength() == 0);
		antHomeLocations.values().forEach(antHome -> antHome.step());
		foodSources.forEach(fs -> fs.step());
		
		while(true) {
			Runnable r = actionQueue.poll();
			if(r == null) break;
		
			r.run();
		}
		
		fireStepped();
	}
	
	public Location getClosestAntHomeEntrance(WorkerAnt me) {
		AntHome antHome = me.getAntHome();
		Location loc = me.getLocation();
		List<AntHomeEntrance> entrances = antHome.getEntrances();
		MatrixMappableEntity closest = antHome;
		double closestDistance = loc.getDistanceTo(closest.getLocation());
		for(AntHomeEntrance entrance: entrances) {
			double distance = loc.getDistanceTo(entrance.getLocation());
			if(distance < closestDistance) {
				closest = entrance;
				closestDistance = distance;
			}
		}
		return closest.getLocation();
	}
	
	public void setMyLocation(MappableEntity me, int x, int y) throws HitAWallException {
		if(x < 0 || y < 0) throw new HitAWallException("You hit a wall!");
		if(x >= size || y >= size) throw new HitAWallException("You hit a wall!");
		me.setLocation(Location.at(x, y));
	}
	
	public boolean didIFindFood(MatrixMappableEntity me) {
		for(FoodStorage fs: foodSources) {
			if(fs.getLocation().equals(me.getLocation()) && fs.hasFood()) return true;
		}
		
		return false;
	}
	
	public FoodStorage getFoodStorage(MatrixMappableEntity me) {
		for(FoodStorage fs: foodSources) {
			if(fs.getLocation().equals(me.getLocation()) && fs.hasFood()) return fs;
		}
		
		return null;
	}
	
	public boolean didIFindAMarker(MatrixMappableEntity me) {
		return markers.containsKey((MatrixLocation)me.getLocation());
	}

	public Marker getMarker(MatrixMappableEntity me) {
		return markers.get((MatrixLocation)me.getLocation());
	}
	
	public void placeMarker(Marker marker) {
		markers.put((MatrixLocation) marker.getLocation(), marker);
	}
	
	public void addWorldListeners(WorldListener listener) {
		listeners.add(listener);
	}
	
	public void removeWorldListeners(WorldListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireAntHomeCreated(AntHome antHome) {
            eventLog.log(antHome, EventType.ANTHOME_FOUNDED);
            listeners.forEach(l -> l.antHomeCreated(antHome));
	}
	
	protected void fireAntHomeRemoved(AntHome antHome) {
            eventLog.log(antHome, EventType.ANTHOME_ENDED);
            listeners.forEach(l -> l.antHomeRemoved(antHome));
	}
	
	protected void fireStepped() {
		listeners.forEach(l -> l.stepped());
	}
	
	public static void main(String[] args) throws InterruptedException {
		World world = new World(20);
		

		
		while(true) {
			world.step();
			Thread.sleep(100);
		}
	}
}
