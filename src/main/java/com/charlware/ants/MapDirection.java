/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

import java.util.Arrays;

/**
 *
 * @author CVanJaarsveldt
 */
public enum MapDirection {
	Up,
	Down,
	Left,
	Right,
	Nowhere;
	
	public MapDirection opposite() {
		switch(this) {
			case Up: return Down;
			case Down: return Up;
			case Left: return Right;
			case Right: return Left;
			default: return Nowhere;
		}
	}
	
	public static MapDirection anywhereBut(final MapDirection except) {
		MapDirection[] dirs = new MapDirection[3];
		int i = 0;
		for(MapDirection dir: values()) {
			if(dir != Nowhere && dir != except) {
				dirs[i++] = dir;
			}
		}
		return dirs[World.random.nextInt(3)];
	}
	
	public static MapDirection[] actualDirections() {
		return new MapDirection[] {Up, Down, Left, Right};
	}
	
	public static MapDirection randomActualDirection() {
		return actualDirections()[World.random.nextInt(4)];
	}
}
