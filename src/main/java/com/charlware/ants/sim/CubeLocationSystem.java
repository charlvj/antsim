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
public class CubeLocationSystem extends LocationSystem {

    int size = 0;
    
    @Override
    public void setSize(int size) {
        this.size = size;
    }
    
    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Location getCenter() {
        return new CubeLocation(size / 2, size / 2, -size);
    }

//    @Override
//    public Location randomLocation() {
//        int q = World.random.nextInt(size);
//        int r = World.random.nextInt(size);
//        int i = World.random.nextInt(3);
//        CubeLocation loc;
//        switch(i) {
//            case 0: loc = new CubeLocation(q, r, -q -r); break;
//            case 1: loc = new CubeLocation(q, -q -r, r); break;
//            case 2: loc = new CubeLocation(-q -r, q, r); break;
//            default: loc = new CubeLocation(q, r);
//        }
//        return loc;
////        return new CubeLocation(
////                World.random.nextInt(size),
////                World.random.nextInt(size)
////        );
//    }

    @Override
    public MapDirections getMapDirections() {
        return new MapDirections(CubeLocation.class);
    }

}
