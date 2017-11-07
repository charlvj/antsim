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
public interface Location {
	public static Location at(int ... c) {
		switch(c.length) {
			case 2: return new MatrixLocation(c[0], c[1]);
			default: throw new UnsupportedOperationException("A location with " + c.length + " dimensions are not supported through this factory method.");
		}
	}
	
	public double getDistanceTo(Location other);
}
