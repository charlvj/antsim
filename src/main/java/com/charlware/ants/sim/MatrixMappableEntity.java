/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.sim;

/**
 *
 * @author CVanJaarsveldt
 */
public class MatrixMappableEntity implements MappableEntity {
	private MatrixLocation location = null;

	@Override
	public Location getLocation() {
		return location;
	}
	
	@Override
	public void setLocation(Location loc) {
		location = (MatrixLocation) loc;
	}
	
	public void setLocation(int x, int y) {
		location = new MatrixLocation(x, y);
	}
	
	public int getX() {
		return location.getX();
	}

	public int getY() {
		return location.getY();
	}

	public void setX(int x) {
		location = new MatrixLocation(x, location.getY());
	}

	public void setY(int y) {
		location = new MatrixLocation(location.getX(), y);
	}
	
}
