/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import com.charlware.ants.ant.Ant;
import com.charlware.ants.sim.DynamicEntity;
import com.charlware.ants.sim.MatrixMappableEntity;

/**
 *
 * @author CVanJaarsveldt
 */
public class AntHome extends MatrixMappableEntity implements DynamicEntity {
	
	public static int BIRTH_PERIOD = 200;
	public static int BIRTH_FOOD_THRESHOLD = 500;
	
	private final World world;
	private FoodStorage foodStorage;
	private int stepsTillNextBirth = BIRTH_PERIOD;
	
	public AntHome(World world) {
		this.world = world;
		this.foodStorage = new FoodStorage(world, 100_000, 1_000);
	}
	
	public FoodStorage getFoodStorage() {
		return foodStorage;
	}

	private boolean enoughFoodForBirth() {
		int numAnts = world.getAnts().size();
		return foodStorage.getCurrent() > BIRTH_FOOD_THRESHOLD * numAnts;
	}
	
	@Override
	public void step() {
		stepsTillNextBirth--;
		if(stepsTillNextBirth < 0 && enoughFoodForBirth()) {
			world.createAnt();
			stepsTillNextBirth = BIRTH_PERIOD;
		}
	}
	
	@Override
	public String toString() {
		return "AntHome! Food:" + foodStorage.getCurrent() + ";  NextBirth:" + stepsTillNextBirth;
	}
}
