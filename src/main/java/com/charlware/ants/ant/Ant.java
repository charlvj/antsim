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
import com.charlware.ants.World;
import com.charlware.ants.sim.DynamicEntity;
import com.charlware.ants.sim.MatrixMappableEntity;
import com.github.oxo42.stateless4j.StateMachine;
import static com.charlware.ants.ant.WorkerAnt.DEFAULT_EAT_SPEED;

/**
 *
 * @author CVanJaarsveldt
 */
public abstract class Ant extends MatrixMappableEntity implements DynamicEntity {

    public static int DEFAULT_FULL_TUMMY = 50;
    public static int DEFAULT_HUNGRY_TUMMY = 0;
    public static int DEFAULT_DEAD_TUMMY = -100;
    public static int DEFAULT_EAT_SPEED = 4;
    /*
	Give the ant a long life. This should help to get the size of the
	colony more proportional to the amount of food available. Age helps
	to "recycle" the ants when the colony reaches its limit, ensuring 
	that queens keeps coming forth.
     */
    public static int DEFAULT_AGE_LIMIT = 20_000; // steps

    protected final int id;
    protected final FoodStorage storage;
    protected final AntHome home;
    protected final World world;
    protected final StateMachine<AntState, AntTrigger> state;

    protected int tummyLevel = DEFAULT_FULL_TUMMY;
    private boolean showMsgs = false;
    protected int currentAge = 0;

    public Ant(final int id, final AntHome home, final int storageCapacity, StateMachine<AntState, AntTrigger> stateMachine) {
        this.id = id;
        this.home = home;
        this.world = home.getWorld();
        this.storage = new FoodStorage(world, storageCapacity);
        this.state = stateMachine;
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
        int x = getX();
        int y = getY();
        switch (direction) {
            case Up:
                y--;
                break;
            case Left:
                x--;
                break;
            case Right:
                x++;
                break;
            case Down:
                y++;
                break;
        }
        world.setMyLocation(this, x, y);
    }

    protected int getEatSpeed() {
        return DEFAULT_EAT_SPEED;
    }

    protected int getFullTummy() {
        return DEFAULT_FULL_TUMMY;
    }

    protected int getHungryTummy() {
        return DEFAULT_HUNGRY_TUMMY;
    }

    protected int getDeadTummy() {
        return DEFAULT_DEAD_TUMMY;
    }

    protected int getAgeLimit() {
        return DEFAULT_AGE_LIMIT;
    }

    protected void eat() {
        print("Eating   ");
        int f = home.getFoodStorage().take(getEatSpeed());
        if (f == 0) {
            println("No food in AntHome!");
            state.fire(AntTrigger.AntHomeOutOfFood);
        } else {
            tummyLevel += f;
            println("+1 = " + tummyLevel);
            if (tummyLevel >= getFullTummy()) {
                state.fire(AntTrigger.Done);
            }
        }
    }

    protected boolean isTooOld() {
        return currentAge > getAgeLimit();
    }

    public AntState getCurrentState() {
        return state.getState();
    }

    public FoodStorage getFoodStorage() {
        return storage;
    }

    public boolean isHungry() {
        return tummyLevel <= getHungryTummy();
    }

    public boolean isStarved() {
        return tummyLevel <= getDeadTummy();
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
