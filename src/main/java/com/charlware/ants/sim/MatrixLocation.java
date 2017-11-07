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
public class MatrixLocation implements Location {
	private final int x;
	private final int y;
	private int hash = -1;

	public MatrixLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public static MatrixLocation at(int x, int y) {
		return new MatrixLocation(x, y);
	}

	@Override
	public int hashCode() {
		if(hash == -1) {
			hash = 7;
			hash = 47 * hash + this.x;
			hash = 47 * hash + this.y;
		}
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
		final MatrixLocation other = (MatrixLocation) obj;
		return x == other.x && y == other.y;
	}
	
	
}
