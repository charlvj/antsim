/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.sim;

import com.charlware.ants.MapDirection;

/**
 *
 * @author CVanJaarsveldt
 */
public interface Location {
	public static Location at(int ... c) {
		switch(c.length) {
			case 2: return new MatrixLocation(c[0], c[1]);
			default: throw new UnsupportedOperationException("A location with " + c.length + " dimensions are not supported through this factory method.");
		}
	}
	
        public MapDirection[] getDirections();
        public Location[] getNeighbors();
	public double getDistanceTo(Location other);
        
        public Location add(Location location);
        public Location subtract(Location location);
        public Location multiply(int scalar);
        
        public default MapDirection getDirectionTo(Location other) {
            return DirectionFinder.getDirection(this, other);
        }
}
