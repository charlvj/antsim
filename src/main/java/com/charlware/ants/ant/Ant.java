/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ant;

import com.charlware.ants.AntHome;
import com.charlware.ants.FoodStorage;
import com.charlware.ants.HitAWallException;
import com.charlware.ants.MapDirection;
import com.charlware.ants.SimSettings;
import com.charlware.ants.World;
import com.charlware.ants.sim.DynamicEntity;
import com.charlware.ants.sim.Location;
import com.charlware.ants.sim.MappableEntity;
import com.github.oxo42.stateless4j.StateMachine;

/**
 *
 * @author CVanJaarsveldt
 */
public abstract class Ant extends MappableEntity implements DynamicEntity {
    protected final int id;
    protected final FoodStorage storage;
    protected final AntHome home;
    protected final World world;
    protected final SimSettings simSettings;
    protected final StateMachine<AntState, AntTrigger> state;

    protected int tummyLevel;
    private boolean showMsgs = true;
    protected int currentAge = 0;

    public Ant(final int id, final AntHome home, final int storageCapacity, StateMachine<AntState, AntTrigger> stateMachine) {
        this.id = id;
        this.home = home;
        this.world = home.getWorld();
        this.storage = new FoodStorage(world, storageCapacity);
        this.state = stateMachine;
        this.simSettings = world.getSimSettings();
        this.tummyLevel = simSettings.ant_fullTummy;
        setLocation(home.getLocation());
    }

    public int getId() {
        return id;
    }

    public abstract AntState getState();

    protected void print(String msg) {
        if (showMsgs) {
            System.out.print(msg);
        }
    }

    protected void println(String msg) {
        if (showMsgs) {
            System.out.println(msg);
        }
    }

    protected void move(MapDirection direction) throws HitAWallException {
        Location newLocation = direction.applyTo(location);
//        if(!world.locationSystem.isLocationInBounds(newLocation))
//            throw new HitAWallException("Location out of bounds: " + newLocation);
        world.setMyLocation(this, newLocation);
    }

    protected void eat() {
        print("Eating   ");
        int f = home.getFoodStorage().take(simSettings.ant_eatSpeed);
        if (f == 0) {
            println("No food in AntHome!");
            state.fire(AntTrigger.AntHomeOutOfFood);
        } else {
            tummyLevel += f;
            println("+1 = " + tummyLevel);
            if (tummyLevel >= simSettings.ant_fullTummy){
                state.fire(AntTrigger.Done);
            }
        }
    }

    protected boolean isTooOld() {
        return currentAge > simSettings.ageLimit;
    }

    public AntState getCurrentState() {
        return state.getState();
    }

    public FoodStorage getFoodStorage() {
        return storage;
    }

    public boolean isHungry() {
        return tummyLevel <= simSettings.ant_hungryTummy;
    }

    public boolean isStarved() {
        return tummyLevel <= simSettings.ant_deadTummy;
    }

    public boolean amIHome() {
        return home.getLocation().equals(getLocation());
    }

    public AntHome getAntHome() {
        return home;
    }

    public void step() {
        if (state.getState() != AntState.Eating) {
            tummyLevel--;
        }

        currentAge++;
        if (isTooOld() && state.canFire(AntTrigger.DieOfOldAge)) {
            state.fire(AntTrigger.DieOfOldAge);
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Ant)) {
            return false;
        }
        Ant a = (Ant) o;

        return id == a.id;
    }

}

class Tummy {

    private int tummyLevel;
    private int energyToFood = 4;   // steps per unit of food

    public Tummy(int tummyLevel) {
        this.tummyLevel = tummyLevel;
    }

    public void addFood(int food) {
        tummyLevel += food;
    }

    public void step() {

    }
}
