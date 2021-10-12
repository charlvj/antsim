/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.sim;

import com.charlware.ants.MapDirections;
import com.charlware.ants.World;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author charlvj
 */
public class MatrixLocationSystem extends LocationSystem {

    private int size = 0;
    
    @Override
    public Location getCenter() {
        return new MatrixLocation(size / 2, size / 2);
    }

//    @Override
//    public Location randomLocation() {
//        return new MatrixLocation(
//                World.random.nextInt(size),
//                World.random.nextInt(size)
//        );
//    }

    @Override
    public MapDirections getMapDirections() {
        return new MapDirections(MatrixLocation.class);
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }
    
    @Override
    public int getSize() {
        return size;
    }

//    @Override
//    public List<Location> getMapBorder() {
//        List<Location> border = new ArrayList<>();
//        for(int x = 0; x < size; x++) {
//            border.add(new MatrixLocation(x, 0));
//            border.add(new MatrixLocation(x, size));
//        }
//        for(int y = 0; y < size; y++) {
//            border.add(new MatrixLocation(0, y));
//            border.add(new MatrixLocation(size, y));
//        }
//        return border;
//    }
    
}
