/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import com.charlware.ants.ant.Ant;
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

/**
 *
 * @author CVanJaarsveldt
 */
public final class World {
	private final int size;
	private final Random random = new Random();
	
	private final AntHome antHome;
	private int antId = 0;
	private final List<Ant> ants = new ArrayList<>(10);
	private final List<FoodStorage> foodSources = new ArrayList<>(5);
	private final Map<MatrixLocation, Marker> markers = new HashMap<>();
	
	private final ArrayDeque<Runnable> actionQueue = new ArrayDeque<>();
	
	public World(int size) {
		this.size = size;
		antHome = new AntHome(this);
		antHome.setLocation(size / 2, size / 2);
		
		
		createAnt();
		
//		int numFoodSources = size * 2;
//		for(int i = 0; i < numFoodSources; i++) {
//			addStableFoodSource();
//		}
		
	}
	
	public int getSize() {
		return size;
	}
	
	public void createAnt() {
		actionQueue.add(() -> {
			ants.add(new Ant(this, antId++, antHome.getX(), antHome.getY()));
		});
	}
	
	public void removeAnt(Ant ant) {
		actionQueue.add(() -> {
			ants.remove(ant);
		});
	}
	
	public void antDied(Ant ant) {
		// Have the ant drop its payload
		actionQueue.add(() -> {
			FoodStorage fs;
			if(antHome.getLocation().equals(ant.getLocation())) {
				fs = antHome.getFoodStorage();
			}
			else {
				fs = getFoodStorage(ant);
			}
			int f = Ant.FOOD_VALUE;
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
		});
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
		if(antHome.getLocation().equals(loc)) {
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
		antHome.step();
		foodSources.forEach(fs -> fs.step());
		
		while(true) {
			Runnable r = actionQueue.poll();
			if(r == null) break;
		
			r.run();
		}
	}
	
	public AntHome getAntHome() {
		return antHome;
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
	
	public static void main(String[] args) throws InterruptedException {
		World world = new World(20);
		

		
		while(true) {
			world.step();
			Thread.sleep(100);
		}
	}
}
