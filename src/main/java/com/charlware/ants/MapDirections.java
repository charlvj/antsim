/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import com.charlware.ants.sim.CubeLocation;
import com.charlware.ants.sim.CubeMapDirection;
import com.charlware.ants.sim.MatrixLocation;
import com.charlware.ants.sim.MatrixMapDirection;

/**
 *
 * @author charlvj
 */
public class MapDirections {

    public static final MapDirection[] CUBE_DIRECTIONS = new MapDirection[]{
                new CubeMapDirection(+1, -1, 0),
                new CubeMapDirection(+1, 0, -1),
                new CubeMapDirection(0, +1, -1),
                new CubeMapDirection(-1, +1, 0),
                new CubeMapDirection(-1, 0, +1),
                new CubeMapDirection(0, -1, +1)
            };
    public static final MapDirection CUBE_NOWHERE = new CubeMapDirection(0, 0, 0);
    
    public static final MapDirection[] MATRIX_DIRECTIONS = new MapDirection[]{
                new MatrixMapDirection(0, -1),
                new MatrixMapDirection(0, +1),
                new MatrixMapDirection(-1, 0),
                new MatrixMapDirection(+1, 0)
            };
    public static final MapDirection MATRIX_NOWHERE = new MatrixMapDirection(0, 0);
    
    private final MapDirection[] directions;
    private final MapDirection nowhere;

    public MapDirections(Class locationClass) {
        if (locationClass == MatrixLocation.class) {
            directions = MATRIX_DIRECTIONS;
            nowhere = MATRIX_NOWHERE;
        } else if (locationClass == CubeLocation.class) {
            directions = CUBE_DIRECTIONS;
            nowhere = CUBE_NOWHERE;
        } else {
            directions = new MapDirection[0];
            nowhere = null;
        }
    }
    
    public MapDirection[] getAllDirections() {
        return directions;
    }
    
    public MapDirection getDirectionToCircleStart() {
        if(nowhere instanceof MatrixMapDirection) {
            return directions[3];
        }
        else if(nowhere instanceof CubeMapDirection) {
            return directions[4];
        }
        else {
            throw new RuntimeException("Invalid map direction.");
        }
    }

    public MapDirection anywhereBut(final MapDirection except) {
        MapDirection[] dirs = new MapDirection[directions.length - 1];
        int i = 0;
        for(MapDirection dir : directions) {
            if(!dir.equals(except)) {
                dirs[i++] = dir;
            }
        }
        return dirs[World.random.nextInt(dirs.length)];
    }

    public MapDirection randomActualDirection() {
        return directions[World.random.nextInt(directions.length)];
    }
}
