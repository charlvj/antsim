/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants;

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
}
