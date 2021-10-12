/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.sim;

import com.charlware.ants.MapDirection;
import com.charlware.ants.MapDirections;
import com.charlware.ants.World;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author charlvj
 */
public abstract class LocationSystem {
    
    
    private List<Location> border = null;
    private List<Location> allLocations = null;
    
    public abstract void setSize(int size);  
    public abstract int getSize();
    public abstract Location getCenter();
    public abstract MapDirections getMapDirections();
    
    public Location randomLocation() {
        int i = World.random.nextInt(getAllLocations().size());
        
        return allLocations.get(i);
    }
    
    public List<Location> getCircle(Location center, int radius) {
        border = new ArrayList<>();
        MapDirection[] directions = getMapDirections().getAllDirections();
        MapDirection toCircleStart = getMapDirections().getDirectionToCircleStart();
        Location start = toCircleStart.applyTo(center, radius);
        for(MapDirection dir : directions) {
            for(int i = 0; i <= radius; i++) {
                border.add(start);
                start = dir.applyTo(start);
            }
        }
        
        return border;
    }
    
    public List<Location> getMapBorder() {
        if(border != null) {
            return border;
        }

        border = getCircle(getCenter(), getSize());
        
        return border;
    }
    
    public List<Location> getAllLocations() {
        if(allLocations == null) {
            allLocations = new ArrayList<>();

            allLocations.add(getCenter());
            for(int r = 1; r < getSize(); r++) {
                allLocations.addAll(getCircle(getCenter(), r));
            }
        }
        
        return allLocations;
    }
    
    public boolean isLocationInBounds(Location location) {
        return allLocations.contains(location);
    }
}
