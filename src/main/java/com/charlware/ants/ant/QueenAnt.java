/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ant;

import com.charlware.ants.AntHome;
import com.charlware.ants.HitAWallException;
import com.charlware.ants.MapDirection;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;

/**
 *
 * @author CVanJaarsveldt
 */
public class QueenAnt extends Ant {
	public static final StateMachineConfig<AntState,AntTrigger> smconfig = new StateMachineConfig<>();
	
	private final MapDirection homeDirection;
	
	static {
		// Setup the state machine
		smconfig.configure(AntState.PreparingForDeparture)
			.permit(AntTrigger.Done, AntState.SearchingForNestingSite);
		
		smconfig.configure(AntState.SearchingForNestingSite)
			.permit(AntTrigger.FoundNestingSite, AntState.Nesting)
			.permit(AntTrigger.Starved, AntState.Dead);
		
	}
	
	public QueenAnt(AntHome antHome, int id) {
		super(id, antHome, 300, new StateMachine<>(AntState.PreparingForDeparture, smconfig));
		homeDirection = MapDirection.randomActualDirection();
	}

	@Override
	public void step() {
		super.step();
		
		switch(state.getState()) {
			case PreparingForDeparture:
				// We eat until we have enough food for departure
				eat();
				break;
			case SearchingForNestingSite:
				if(isHungry()) {
					state.fire(AntTrigger.FoundNestingSite);
				}
				else {
					try {
						move(MapDirection.anywhereBut(homeDirection));
					} catch (HitAWallException ex) {
						// Stand around looking silly for this step.
					}
				}
				break;
			case Nesting:
				world.createAntHome(getLocation());
				world.removeAnt(this);
				break;
		}
	}
}
