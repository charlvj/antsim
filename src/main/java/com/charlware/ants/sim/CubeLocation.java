/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.sim;

import com.charlware.ants.MapDirection;
import com.charlware.ants.MapDirections;

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
        
        public CubeLocation(final int x, final int y) {
            this.x = x;
            this.y = y;
            this.z = -x - y;
        }
	
	public CubeLocation(final MatrixLocation matrix) {
		this.x = matrix.getX();
		this.y = matrix.getY();
		this.z = -x - y;
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
		return new MatrixLocation(x, y);
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
		return "[" + x + "; " + y + "; " + z + "]";
	}

    @Override
    public Location[] getNeighbors() {
        MapDirection[] directions = MapDirections.CUBE_DIRECTIONS;
        Location[] neighbors = new CubeLocation[directions.length];
        
        for(int i = 0; i < directions.length; i++) {
            neighbors[i] = directions[i].applyTo(this);
        }
        
        return neighbors;
    }

    @Override
    public MapDirection[] getDirections() {
        return MapDirections.CUBE_DIRECTIONS;
    }

    @Override
    public Location add(Location location) {
        CubeLocation l = (CubeLocation) location;
        return new CubeLocation(x + l.x, y + l.y, z + l.z);
    }

    @Override
    public Location subtract(Location location) {
        CubeLocation l = (CubeLocation) location;
        return new CubeLocation(x - l.x, y - l.y, z - l.z);
    }

    @Override
    public Location multiply(int scalar) {
        return new CubeLocation(x * scalar, y * scalar, z * scalar);
    }
	
	
}
