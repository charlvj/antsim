/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.sim;

import com.charlware.ants.MapDirection;
import java.util.Random;

/**
 *
 * @author CVanJaarsveldt
 */
public class MatrixLocation implements Location {

    private final int x;
    private final int y;
    private int hash = -1;
    private Random random = new Random();

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

    public MatrixLocation derive(int xAdj, int yAdj) {
        return new MatrixLocation(x + xAdj, y + yAdj);
    }

    public static MatrixLocation at(int x, int y) {
        return new MatrixLocation(x, y);
    }

    private int sqr(int i) {
        return i * i;
    }

    @Override
    public double getDistanceTo(Location otherLocation) {
        if (!(otherLocation instanceof MatrixLocation)) {
            throw new RuntimeException("Not implemented!");
        }

        MatrixLocation other = (MatrixLocation) otherLocation;
        return Math.sqrt(sqr(other.x - x) + sqr(other.y + y));
    }

    public MapDirection getDirectionTo(Location otherLocation) {
        return DirectionFinder.getDirection(this, otherLocation);
        
//        if (!(otherLocation instanceof MatrixLocation)) {
//            throw new RuntimeException("Not implemented!");
//        }
//
//        MatrixLocation other = (MatrixLocation) otherLocation;
//        int dx = other.x - x;
//        int dy = other.y - y;
//        int sx = Integer.compare(dx, 0);
//        int sy = Integer.compare(dy, 0);
//
//        MapDirection dir = MapDirection.Nowhere;
//
//        if (dx == 0 && dy == 0) {
//            // We're there!
//        } else if (dx == 0 && dy > 0) {
//            // Move down
//            dir = MapDirection.Down;
//        } else if (dx == 0 && dy < 0) {
//            // Move up
//            dir = MapDirection.Up;
//        } else if (dx > 0 && dy == 0) {
//            // Move right
//            dir = MapDirection.Right;
//        } else if (dx < 0 && dy == 0) {
//            // Move left
//            dir = MapDirection.Left;
//        } else {
//            if (Math.abs(dx) * 0.5 > Math.abs(dy) * 0.5) {
//                // Move horizontally
//                switch (sx) {
//                    case -1:
//                        dir = MapDirection.Left;
//                        break;
//                    case 1:
//                        dir = MapDirection.Right;
//                        break;
//                }
//            } else {
//                // Move vertically
//                switch (sy) {
//                    case -1:
//                        dir = MapDirection.Up;
//                        break;
//                    case 1:
//                        dir = MapDirection.Down;
//                        break;
//                }
//            }
//        }
//        return dir;
    }

    @Override
    public int hashCode() {
        if (hash == -1) {
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
