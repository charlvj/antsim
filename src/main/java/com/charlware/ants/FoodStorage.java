/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import com.charlware.ants.sim.DynamicEntity;
import com.charlware.ants.sim.Location;
import com.charlware.ants.sim.MatrixMappableEntity;

/**
 *
 * @author CVanJaarsveldt
 */
public class FoodStorage extends MatrixMappableEntity implements DynamicEntity {

    private final World world;
    private final int max;
    private int current = 0;
    private int restockPeriod = 0;
    private int restockRate = 0;
    private int stepsSinceRestock = 0;
    private boolean removeWhenEmpty = false;

    public FoodStorage(World world, int max) {
        this(world, max, 0);
    }

    public FoodStorage(World world, int max, int current) {
        this.world = world;
        this.max = max;
        this.current = current;
    }

    public FoodStorage(World world, int max, int current, int x, int y) {
        this(world, max, current);
        setLocation(x, y);
    }

    public FoodStorage(World world, int max, int current, Location loc) {
        this(world, max, current);
        setLocation(loc);
    }

    public FoodStorage setRestock(int period, int rate) {
        this.restockPeriod = period;
        this.restockRate = rate;
        return this;
    }
    
    public boolean isTemporary() {
        return restockPeriod == 0;
    }

    public FoodStorage setRemoveWhenEmpty(boolean remove) {
        this.removeWhenEmpty = remove;
        return this;
    }

    public boolean hasSpace() {
        return current < max;
    }

    public int getCapacity() {
        return max;
    }

    public int getSpaceLeft() {
        return max - current;
    }

    public int getCurrent() {
        return current;
    }

    public boolean hasFood() {
        return current > 0;
    }

    public boolean isFull() {
        return getSpaceLeft() == 0;
    }

    public boolean isEmpty() {
        return !hasFood();
    }

    public int add(int amount) {
        current += amount;
        int overflow = 0;
        if (current > max) {
            overflow = current - max;
            current = max;
        }
        return overflow;
    }

    public int take(int amount) {
        if (current >= amount) {
            current -= amount;
            return amount;
        }

        int temp = current;
        current = 0;
        if (removeWhenEmpty) {
            world.removeFoodStorage(this);
        }
        return temp;
    }

    @Override
    public void step() {
        if (restockPeriod > 0) {
            stepsSinceRestock++;
            if (stepsSinceRestock > restockPeriod && hasSpace()) {
                add(restockRate);
                stepsSinceRestock = 0;
            }
        }
    }

    @Override
    public String toString() {
        return "FoodStorage  current:" + current;
    }
}
