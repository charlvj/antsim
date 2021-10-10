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
public class CubeMapDirection implements MapDirection {

    private final int x;
    private final int y;
    private final int z;
    
    public CubeMapDirection(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public Location applyTo(Location location) {
        CubeLocation loc = (CubeLocation) location;
        return new CubeLocation(loc.getX() + x, loc.getY() + y, loc.getZ() + z);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.x;
        hash = 71 * hash + this.y;
        hash = 71 * hash + this.z;
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
        final CubeMapDirection other = (CubeMapDirection) obj;
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
}
