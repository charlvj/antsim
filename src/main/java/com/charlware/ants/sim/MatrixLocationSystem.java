/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.sim;

import com.charlware.ants.MapDirections;
import com.charlware.ants.World;

/**
 *
 * @author charlvj
 */
public class MatrixLocationSystem implements LocationSystem {

    private int size = 0;
    
    @Override
    public Location getCenter() {
        return new MatrixLocation(size / 2, size / 2);
    }

    @Override
    public Location randomLocation() {
        return new MatrixLocation(
                World.random.nextInt(size),
                World.random.nextInt(size)
        );
    }

    @Override
    public MapDirections getMapDirections() {
        return new MapDirections(MatrixLocation.class);
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }
    
}
