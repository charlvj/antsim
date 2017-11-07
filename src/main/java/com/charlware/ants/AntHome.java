/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import com.charlware.ants.ant.Ant;
import com.charlware.ants.sim.DynamicEntity;
import com.charlware.ants.sim.Location;
import com.charlware.ants.sim.MatrixLocation;
import com.charlware.ants.sim.MatrixMappableEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CVanJaarsveldt
 */
public class AntHome extends MatrixMappableEntity implements DynamicEntity {
	
	public static int BIRTH_PERIOD = 200;
	public static int BIRTH_FOOD_THRESHOLD = 500;
	
	private static int nextId = 1;
	private final int id;
	private final World world;
	private final List<AntHomeEntrance> entrances = new ArrayList<>();
	private final List<Ant> ants = new ArrayList<>();
	private FoodStorage foodStorage;
	private int stepsTillNextBirth = BIRTH_PERIOD;
	private int births = 0;
	
	private List<AntHomeListener> listeners = new ArrayList<>();
	
	public AntHome(World world) {
		this.world = world;
		this.foodStorage = new FoodStorage(world, 100_000, 1_000);
		this.id = nextId++;
	}
	
	public int getId() {
		return id;
	}
	
	public AntHome addEntrance(Location location) {
		AntHomeEntrance entrance = new AntHomeEntrance(world, this);
		entrance.setLocation(location);
		entrances.add(entrance);
		return this;
	}
	
	public AntHome addEntrance(int x, int y) {
		return addEntrance(new MatrixLocation(x, y));
	}
	
	public List<AntHomeEntrance> getEntrances() {
		return entrances;
	}
	
	public void addAnt(final Ant ant) {
		ants.add(ant);
		fireAntBorn(ant);
	}
	
	public boolean removeAnt(Ant ant) {
		boolean result = ants.remove(ant);
		fireAntDied(ant);
		
		if(ants.isEmpty()) {
			world.removeAntHome(this);
		}
		return result;
	}
	
	public List<Ant> getAnts() {
		return ants;
	}
	
	public FoodStorage getFoodStorage() {
		return foodStorage;
	}

	private boolean enoughFoodForBirth() {
		int numAnts = ants.size();
		return foodStorage.getCurrent() > BIRTH_FOOD_THRESHOLD * numAnts;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void addAntHomeListener(AntHomeListener listener) {
		listeners.add(listener);
	}
	
	public void removeAntHomeListener(AntHomeListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireAntBorn(Ant ant) {
		listeners.forEach(l -> l.antBorn(this, ant));
	}
	
	protected void fireAntDied(Ant ant) {
		listeners.forEach(l -> l.antDied(this, ant));
	}
	
	protected void fireFoodStorageEmpty() {
		listeners.forEach(l -> l.foodStorageEmpty(this));
	}
	
	protected void fireFoodStorageFull() {
		listeners.forEach(l -> l.foodStorageFull(this));
	}
	
	@Override
	public void step() {
		stepsTillNextBirth--;
		if(stepsTillNextBirth < 0 && enoughFoodForBirth()) {
			births++;
			if(births % 10 == 0) {
				world.createQueenAnt(this);
			}
			else {
				world.createAnt(this);
			}
			stepsTillNextBirth = BIRTH_PERIOD;
		}
		
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + this.id;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AntHome other = (AntHome) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}
	
	
	@Override
	public String toString() {
		return "AntHome! Food:" + foodStorage.getCurrent() + ";  NextBirth:" + stepsTillNextBirth;
	}
}
