/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ant;

import com.charlware.ants.HitAWallException;
import com.charlware.ants.MapDirection;
import com.charlware.ants.MapUtils;
import com.charlware.ants.sim.MatrixLocation;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CVanJaarsveldt
 */
public class GoHomeAction extends AntAction {

	private final Iterator<MapDirection> route;
	
	public GoHomeAction(final Ant ant) {
		super(ant);
		route = MapUtils.plotRoute(
			(MatrixLocation) ant.getLocation(), 
			(MatrixLocation) ant.getAntHome().getLocation())
			.iterator();
	}
	
	@Override
	public boolean stepAction() {
		if(route.hasNext()) {
			try {
				ant.move(route.next());
			} catch (HitAWallException ex) {
				return false;
			}
			return true;
		}
		return false;
	}
	
}
