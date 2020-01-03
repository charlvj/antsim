/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.sim;

import com.charlware.ants.World;
import com.charlware.ants.ui.SimulationStepListener;

/**
 *
 * @author charlvj
 */
public class RandomFoodGenerator implements SimulationStepListener {

    private final World world;
    private int probability = 10_000;
    
    public RandomFoodGenerator(World world) {
        this.world = world;
    }
    
    @Override
    public void simulationStepped() {
        if(SimUtils.random.nextInt(probability) == 1) {
            world.addTempFoodSource();
        }
    }
    
}
