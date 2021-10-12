/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.sim;

import com.charlware.ants.MapDirection;

/**
 *
 * @author charlvj
 */
public class MatrixMapDirection implements MapDirection {
    private final int x;
    private final int y;
    
    public MatrixMapDirection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Location applyTo(Location location) {
        MatrixLocation loc = (MatrixLocation) location;
        return new MatrixLocation(loc.getX() + x, loc.getY() + y);
    }

    @Override
    public Location applyTo(Location location, int times) {
        MatrixLocation loc = (MatrixLocation) location;
        return new MatrixLocation(loc.getX() + x * times, loc.getY() + y * times);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.x;
        hash = 37 * hash + this.y;
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
        final MatrixMapDirection other = (MatrixMapDirection) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "[" + x + "; " + y + "]";
    }

}
