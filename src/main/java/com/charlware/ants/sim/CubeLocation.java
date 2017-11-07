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
public class CubeLocation implements Location {
	private final int x;
	private final int y;
	private final int z;
	
	public CubeLocation(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public CubeLocation(final MatrixLocation matrix) {
		this.x = matrix.getX();
		this.z = matrix.getY();
		this.y = -x - z;
	}

	@Override
	public double getDistanceTo(Location other) {
		CubeLocation o = (CubeLocation) other;
		return Math.max(Math.abs(x - o.x), Math.max(Math.abs(y - o.y), Math.abs(z - o.z)));
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
	
	/**
	 * x + y + z = 0
	 * This function assumes y = 0, so only x and z are used.
	 * @return 
	 */
	public MatrixLocation toMatrixLocation() {
		return new MatrixLocation(x, z);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + this.x;
		hash = 23 * hash + this.y;
		hash = 23 * hash + this.z;
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
		final CubeLocation other = (CubeLocation) obj;
		if (this.x != other.x) {
			return false;
		}
		if (this.y != other.y) {
			return false;
		}
		if (this.z != other.z) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "CubeLocation{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
	}
	
	
}
