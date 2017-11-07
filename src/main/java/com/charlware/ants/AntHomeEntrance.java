/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import com.charlware.ants.sim.MatrixMappableEntity;

/**
 *
 * @author CVanJaarsveldt
 */
public class AntHomeEntrance extends MatrixMappableEntity {
	private final World world;
	private final AntHome antHome;
	
	public AntHomeEntrance(World world, AntHome antHome) {
		this.world = world;
		this.antHome = antHome;
	}
	
	public FoodStorage getFoodStorage() {
		return antHome.getFoodStorage();
	}
	
	@Override
	public String toString() {
		return "AntHome Entrance";
	}

}
