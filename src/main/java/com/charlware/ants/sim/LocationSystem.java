/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.sim;

import com.charlware.ants.MapDirections;

/**
 *
 * @author charlvj
 */
public interface LocationSystem {
    void setSize(int size);    
    Location getCenter();
    Location randomLocation();
    MapDirections getMapDirections();
}
