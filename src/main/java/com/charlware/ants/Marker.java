/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import com.charlware.ants.sim.DynamicEntity;
import com.charlware.ants.sim.Location;
import com.charlware.ants.sim.MappableEntity;

/**
 *
 * @author CVanJaarsveldt
 */
public class Marker extends MappableEntity implements DynamicEntity {
	private static int INITIAL_STRENGTH = 200;

	private final MapDirection direction;
	private int strength = INITIAL_STRENGTH;
	
	public Marker(MapDirection direction) {
		this.direction = direction;
	}
	
	public Marker(MapDirection direction, Location location) {
		this.direction = direction;
		setLocation(location);
	}
	
	public void step() {
		if(strength > 0) {
			strength--;
		}
	}
	
	public int getStrength() {
		return strength;
	}
	
	public MapDirection getDirection() {
		return direction;
	}
	
	public String toString() {
		return "Marker + " + direction + " @ (" + getLocation() + "), strength: " + strength;
	}
}
