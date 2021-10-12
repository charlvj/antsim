/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.sim;

/**
 *
 * @author CVanJaarsveldt
 */
public class MappableEntity {
    protected Location location = null;
    
    public MappableEntity() {
        
    }
    
    public MappableEntity(Location loc) {
        this.location = loc;
    }
    
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
