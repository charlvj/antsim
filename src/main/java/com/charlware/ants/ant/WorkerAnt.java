
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
import com.charlware.ants.Marker;
import com.charlware.ants.sim.DirectionFinder;
import com.charlware.ants.sim.Location;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import java.util.Random;

/**
 *
 * @author CVanJaarsveldt
 */
public class WorkerAnt extends Ant {

    private int deadCounter = 0;

    private static Random random = new Random();

    public static final StateMachineConfig<AntState, AntTrigger> smconfig = new StateMachineConfig<>();

    static {
        // Setup the state machine
        smconfig.configure(AntState.Wandering)
                .permit(AntTrigger.FoundFood, AntState.CollectingFood)
                .permit(AntTrigger.FoundMarker, AntState.FollowingMarker)
                .permit(AntTrigger.GotHungry, AntState.GoingHomeNoMarkers)
                .permit(AntTrigger.Starved, AntState.Dead)
                .permit(AntTrigger.DieOfOldAge, AntState.Dead);

        smconfig.configure(AntState.CollectingFood)
                .permit(AntTrigger.OutOfSpace, AntState.GoingHomeWithMarkers)
                .permit(AntTrigger.NoFoodLeft, AntState.Wandering)
                .permit(AntTrigger.DieOfOldAge, AntState.Dead);

        smconfig.configure(AntState.GoingHomeWithMarkers)
                .permit(AntTrigger.ReachHome, AntState.StoringFood)
                .permit(AntTrigger.DieOfOldAge, AntState.Dead);

        smconfig.configure(AntState.StoringFood)
                .permit(AntTrigger.Done, AntState.Wandering);

        smconfig.configure(AntState.FollowingMarker)
                .permit(AntTrigger.FoundFood, AntState.CollectingFood)
                .permitReentry(AntTrigger.FoundMarker)
                .permit(AntTrigger.GotHungry, AntState.GoingHomeNoMarkers)
                .permit(AntTrigger.LostMarker, AntState.Wandering)
                .permit(AntTrigger.DieOfOldAge, AntState.Dead);

        smconfig.configure(AntState.GoingHomeNoMarkers)
                .permit(AntTrigger.ReachHome, AntState.Eating)
                .permit(AntTrigger.Starved, AntState.Dead)
                .permit(AntTrigger.DieOfOldAge, AntState.Dead);

        smconfig.configure(AntState.Eating)
                .permit(AntTrigger.Done, AntState.Wandering)
                .permit(AntTrigger.AntHomeOutOfFood, AntState.DesperateForFood)
                .permit(AntTrigger.Starved, AntState.Dead)
                .permit(AntTrigger.DieOfOldAge, AntState.Dead);

        smconfig.configure(AntState.DesperateForFood)
                .permit(AntTrigger.FoundFood, AntState.CollectingFood)
                .permit(AntTrigger.ReachHome, AntState.Eating)
                .permit(AntTrigger.FoundMarker, AntState.FollowingMarker)
                .permit(AntTrigger.Starved, AntState.Dead)
                .permit(AntTrigger.DieOfOldAge, AntState.Dead);
    }

    public WorkerAnt(AntHome home, int id) {
        super(id, 
                home, 
                home.getWorld().getSimSettings().workerant_foodstore_capacity, 
                new StateMachine<>(AntState.Wandering, smconfig));
    }

    public WorkerAnt(AntHome home, int id, Location location) {
        this(home, id);
        setLocation(location);
    }

    public AntState getState() {
        return state.getState();
    }

    public void setGoal() {

    }

    public void step() {
        super.step();

        switch (state.getState()) {
            case Wandering:
                if (isHungry()) {
                    println("Getting Hungry");
                    state.fire(AntTrigger.GotHungry);
                } else if (isStarved()) {
                    println("Starved!");
                    state.fire(AntTrigger.Starved);
                } else if (world.didIFindFood(this)) {
                    println("Found Food!");
                    state.fire(AntTrigger.FoundFood);
                } else if (world.didIFindAMarker(this)) {
                    println("Found a Marker!");
                    state.fire(AntTrigger.FoundMarker);
                } else {
                    wander();
                }
                break;
            case CollectingFood:
                collectFood();
                break;
            case GoingHomeWithMarkers:
                if (home.getLocation().equals(getLocation())) {
                    // I'm home
                    state.fire(AntTrigger.ReachHome);
                } else {
                    goHome(true);
                }
                break;
            case GoingHomeNoMarkers:
                if (isStarved()) {
                    println("Starved!");
                    state.fire(AntTrigger.Starved);
                } else if (world.getClosestAntHomeEntrance(this).equals(getLocation())) {
                    // I'm home
                    state.fire(AntTrigger.ReachHome);
                } else {
                    goHome(false);
                }
                break;
            case StoringFood:
                storeFood();
                break;
            case Eating:
                if (isStarved()) {
                    println("Starved!");
                    state.fire(AntTrigger.Starved);
                } else {
                    eat();
                }
                break;
            case FollowingMarker:
                if (world.didIFindFood(this)) {
                    println("Found Food!");
                    state.fire(AntTrigger.FoundFood);
                } else {
                    followMarker(world.getMarker(this));
                }
                break;
            case DesperateForFood:
                if (world.didIFindFood(this)) {
                    println("Found Food!");
                    state.fire(AntTrigger.FoundFood);
                } else if (world.didIFindAMarker(this)) {
                    println("Found a Marker!");
                    state.fire(AntTrigger.FoundMarker);
                } else if (amIHome() && home.getFoodStorage().hasFood()) {
                    println("Found AntHome");
                    state.fire(AntTrigger.ReachHome);
                } else if (isStarved()) {
                    println("Starved!");
                    state.fire(AntTrigger.Starved);
                } else {
                    wander();
                }
                break;
            case Dead:
                if (deadCounter == 0) {
                    world.antDied(this);
                }
                deadCounter++;
                if (deadCounter > 100) {
                    world.removeAnt(this);
                }
                break;
        }
    }

    private void wander() {
        MapDirection direction = world.mapDirections.randomActualDirection();
        print("Trying to move " + direction + "... ");
        try {
            move(direction);
            println("Moved to " + getLocation());
        } catch (HitAWallException ex) {
            // Just sit here and wonder what to do for now...
            println("Staying put: I hit a wall.");
        }
    }

    private void collectFood() {
        println("Collecting Food");
        FoodStorage fs = world.getFoodStorage(this);
        if (fs == null) {
            state.fire(AntTrigger.NoFoodLeft);
            return;
        }
        int f = fs.take(simSettings.workerant_food_collectSpeed);
        if (f == 0) {
            state.fire(AntTrigger.NoFoodLeft);
            return;
        }
        storage.add(f);
        if (!storage.hasSpace()) {
            state.fire(AntTrigger.OutOfSpace);
        }
    }

    private void storeFood() {
        println("Storing Food");
        int f = storage.take(simSettings.workerant_food_storeSpeed);
        if (f == 0) {
            state.fire(AntTrigger.Done);
            return;
        }
        home.getFoodStorage().add(f);
    }

    public void goHome(boolean leaveMarkers) {
        Location antHomeLocation = world.getClosestAntHomeEntrance(this);
//		MapDirection direction = ((MatrixLocation) getLocation()).getDirectionTo(antHomeLocation);
        MapDirection direction = getLocation().getDirectionTo(antHomeLocation);

        print("Trying to move " + direction + "... ");
        try {
            Location oldLocation = getLocation();
            move(direction);
            print("\tMoved to " + getLocation());
            if (leaveMarkers && !amIHome()) {
                Marker marker = new Marker(getLocation().getDirectionTo(oldLocation), getLocation());
                world.placeMarker(marker);
                print(".\tPlaced Marker (" + marker + "). ");
            }
            println("");
        } catch (HitAWallException ex) {
            // Just sit here and wonder what to do for now...
            println("Staying put: I hit a wall.");
        }
    }

    private void followMarker(Marker marker) {
        if (marker == null) {
            println("Lost Marker. Return to wandering.");
            state.fire(AntTrigger.LostMarker);
            return;
        }
        print("Following Marker " + marker.getDirection() + "... ");
        try {
            move(marker.getDirection());
            println("Moved to " + getLocation());
        } catch (HitAWallException ex) {
            // Just sit here and wonder what to do for now...
            println("Staying put: I hit a wall. Return to wandering.");
//			state.fire(AntTrigger.);
        }

    }

    @Override
    public String toString() {
        return "Ant  #" + id + "; " + getCurrentState() + "; food:" + storage.getCurrent();
    }
}
