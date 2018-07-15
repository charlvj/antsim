
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
import com.charlware.ants.sim.MatrixLocation;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import java.util.Random;

/**
 *
 * @author CVanJaarsveldt
 */
public class WorkerAnt extends Ant {
	
	public static int FOOD_VALUE = 500;
	
	public static int STORAGE_CAPACITY = 75;
	
	public static int COLLECT_SPEED = 7;
	public static int STORE_SPEED = 20;
	
	private int deadCounter = 0;
	
	private static Random random = new Random();
	
	public static final StateMachineConfig<AntState,AntTrigger> smconfig = new StateMachineConfig<>();
	
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
		super(id, home, STORAGE_CAPACITY, new StateMachine<>(AntState.Wandering, smconfig));
	}
	
	public WorkerAnt(AntHome home, int id, int x, int y) {
		this(home, id);
		setLocation(x, y);
	}
	
	
	public void setGoal() {
		
	}
	
	public void step() {
		super.step();
		
		switch(state.getState()) {
			case Wandering:
				if(isHungry()) {
					println("Getting Hungry");
					state.fire(AntTrigger.GotHungry);
				}
				else if(isStarved()) {
					println("Starved!");
					state.fire(AntTrigger.Starved);
				}
				else if(world.didIFindFood(this)) {
					println("Found Food!");
					state.fire(AntTrigger.FoundFood);
				}
				else if(world.didIFindAMarker(this)) {
					println("Found a Marker!");
					state.fire(AntTrigger.FoundMarker);
				}
				else {
					wander();
				}
				break;
			case CollectingFood:
				collectFood();
				break;
			case GoingHomeWithMarkers:
				if(home.getLocation().equals(getLocation())) {
					// I'm home
					state.fire(AntTrigger.ReachHome);
				}
				else {
					goHome(true);
				}
				break;
			case GoingHomeNoMarkers:
				if(isStarved()) {
					println("Starved!");
					state.fire(AntTrigger.Starved);
				}
				else if(world.getClosestAntHomeEntrance(this).equals(getLocation())) {
					// I'm home
					state.fire(AntTrigger.ReachHome);
				}
				else {
					goHome(false);
				}
				break;
			case StoringFood:
				storeFood();
				break;
			case Eating:
				if(isStarved()) {
					println("Starved!");
					state.fire(AntTrigger.Starved);
				}
				else {
					eat();
				}
				break;
			case FollowingMarker:
				if(world.didIFindFood(this)) {
					println("Found Food!");
					state.fire(AntTrigger.FoundFood);
				}
				else {
					followMarker(world.getMarker(this));
				}
				break;
			case DesperateForFood:
				if(world.didIFindFood(this)) {
					println("Found Food!");
					state.fire(AntTrigger.FoundFood);
				}
				else if(world.didIFindAMarker(this)) {
					println("Found a Marker!");
					state.fire(AntTrigger.FoundMarker);
				}
				else if(amIHome() && home.getFoodStorage().hasFood()) {
					println("Found AntHome");
					state.fire(AntTrigger.ReachHome);
				}
				else if(isStarved()) {
					println("Starved!");
					state.fire(AntTrigger.Starved);
				}
				else {
					wander();
				}
				break;
			case Dead:
				if(deadCounter == 0) world.antDied(this);
				deadCounter++;
				if(deadCounter > 100) {
					world.removeAnt(this);
				}
				break;
		}
	}
	
	

	
	private void wander() {
		int directionInt = random.nextInt(4);
		MapDirection direction = MapDirection.values()[directionInt];
		print("Trying to move " + direction + "... ");
		try {
			move(direction);
			println("Moved to " + getX() + "; " + getY());
		} catch (HitAWallException ex) {
			// Just sit here and wonder what to do for now...
			println("Staying put: I hit a wall.");
		}
	}
	
	private void collectFood() {
		println("Collecting Food");
		FoodStorage fs = world.getFoodStorage(this);
		if(fs == null) {
			state.fire(AntTrigger.NoFoodLeft);
			return;
		}
		int f = fs.take(COLLECT_SPEED);
		if(f == 0) {
			state.fire(AntTrigger.NoFoodLeft);
			return;
		}
		storage.add(f);
		if(!storage.hasSpace()) {
			state.fire(AntTrigger.OutOfSpace);
		}
	}
	
	private void storeFood() {
		println("Storing Food");
		int f = storage.take(STORE_SPEED);
		if(f == 0) {
			state.fire(AntTrigger.Done);
			return;
		}
		home.getFoodStorage().add(f);
	}

	public void goHome(boolean leaveMarkers) {
		MatrixLocation antHomeLocation = (MatrixLocation) world.getClosestAntHomeEntrance(this);
//		MapDirection direction = ((MatrixLocation) getLocation()).getDirectionTo(antHomeLocation);
                MapDirection direction = DirectionFinder.getDirection(getLocation(), antHomeLocation);
//		MapDirection direction = MapDirection.Nowhere;
//		
//		int dx = antHomeLocation.getX() - getX();
//		int dy = antHomeLocation.getY() - getY();
//		
//		int newX = getX();
//		int newY = getY();
//		
//		if(dx == 0 && dy == 0) {
//			// We're there!
//		}
//		else if(dx == 0 && dy > 0) {
//			// Move down
//			direction = MapDirection.Down;
//		}
//		else if(dx == 0 && dy < 0) {
//			// Move up
//			direction = MapDirection.Up;
//		}
//		else if(dx > 0 && dy == 0) {
//			// Move right
//			direction = MapDirection.Right;
//		}
//		else if(dx < 0 && dy == 0) {
//			// Move left
//			direction = MapDirection.Left;
//		}
//		else if(dx < 0 && dy < 0) {
//			if(dx < dy) direction = MapDirection.Left;
//			else direction = MapDirection.Up;
//		}
//		else if(dx < 0 && dy > 0) {
//			if(Math.abs(dx) > dy) direction = MapDirection.Left;
//			else direction = MapDirection.Down;
//		}
//		else if(dx > 0 && dy < 0) {
//			if(dx > Math.abs(dy)) direction = MapDirection.Right;
//			else direction = MapDirection.Up;
//		}
//		else if(dx > 0 && dy > 0) {
//			if(dx > dy) direction = MapDirection.Right;
//			else direction = MapDirection.Down;
//		}
		
		print("Trying to move " + direction + "... ");
		try {
			move(direction);
			print("\tMoved to " + getX() + "; " + getY());
			if(leaveMarkers && !amIHome()) {
				Marker marker = new Marker(direction.opposite(), getLocation());
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
		if(marker == null) {
			println("Lost Marker. Return to wandering.");
			state.fire(AntTrigger.LostMarker);
			return;
		}
		print("Following Marker " + marker.getDirection() + "... ");
		try {
			move(marker.getDirection());
			println("Moved to " + getX() + "; " + getY());
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
