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
public class DirectionFinder {
    private static boolean useRandomHorVer = true;
    public static MapDirection getDirection(Location fromLocation, Location toLocation) {
        if(useRandomHorVer)
            return getDirectionWithRandomHorVer(fromLocation, toLocation);
        else
            return getDirectionWithPredictableDirection(fromLocation, toLocation);
    }
    
    public static MapDirection getDirectionWithRandomHorVer(Location fromLocation, Location toLocation) {
        if (!(fromLocation instanceof MatrixLocation)) {
            throw new RuntimeException("Not implemented!");
        }
        if (!(toLocation instanceof MatrixLocation)) {
            throw new RuntimeException("Not implemented!");
        }
        
        MatrixLocation from = (MatrixLocation) fromLocation;
        MatrixLocation to   = (MatrixLocation) toLocation;
        
        MapDirection horDir = MapDirection.Nowhere;
        MapDirection verDir = MapDirection.Nowhere;
        
        System.out.println(Integer.compare(from.getX(), to.getX()));
        System.out.println(Integer.compare(from.getY(), to.getY()));
        switch(Integer.compare(from.getX(), to.getX())) {
            case -1: horDir = MapDirection.Right; break;
            case  1: horDir = MapDirection.Left;
        }
        switch(Integer.compare(from.getY(), to.getY())) {
            case -1: verDir = MapDirection.Down; break;
            case  1: verDir = MapDirection.Up;
        }
        
        System.out.println("DirectionFInder: " + horDir + " - " + verDir);
        
        if(horDir == MapDirection.Nowhere) return verDir;
        if(verDir == MapDirection.Nowhere) return horDir;
        
        // Now we have a combination, randomly choose which one to use
        return SimUtils.random.nextBoolean() ? horDir : verDir;
    }
    
    public static MapDirection getDirectionWithPredictableDirection(Location fromLocation, Location toLocation) {
         if (!(fromLocation instanceof MatrixLocation)) {
            throw new RuntimeException("Not implemented!");
        }
        if (!(toLocation instanceof MatrixLocation)) {
            throw new RuntimeException("Not implemented!");
        }
        
        MatrixLocation from = (MatrixLocation) fromLocation;
        MatrixLocation to   = (MatrixLocation) toLocation;
        
        MapDirection horDir = MapDirection.Nowhere;
        MapDirection verDir = MapDirection.Nowhere;
        
        System.out.println(Integer.compare(from.getX(), to.getX()));
        System.out.println(Integer.compare(from.getY(), to.getY()));
        switch(Integer.compare(from.getX(), to.getX())) {
            case -1: horDir = MapDirection.Right; break;
            case  1: horDir = MapDirection.Left;
        }
        switch(Integer.compare(from.getY(), to.getY())) {
            case -1: verDir = MapDirection.Down; break;
            case  1: verDir = MapDirection.Up;
        }
        
        System.out.println("DirectionFInder: " + horDir + " - " + verDir);
        
        if(horDir == MapDirection.Nowhere) return verDir;
        if(verDir == MapDirection.Nowhere) return horDir;
       
        return null;
    }
}
